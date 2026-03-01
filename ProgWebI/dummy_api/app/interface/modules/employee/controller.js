import {
    EmployeeSchema,
    ListEmployeesResponse,
    GetEmployeeRequest,
    GetEmployeeResponse,
    CreateEmployeeRequest,
    UpdateEmployeeRequest,
    DeleteEmployeeRequest,
} from "./models.js";

import {
    loadStorage,
    saveStorage,
    createEmployee as storageCreateEmployee,
    updateEmployee as storageUpdateEmployee,
    deleteEmployee as storageDeleteEmployee,
    getEmployee as storageGetEmployee,
} from "./localStorage.js";

function storageUpsertCreatedEmployee(employee) {
    const storage = loadStorage();
    const stId = String(employee.id);
    storage.created = storage.created.filter((e) => String(e.id) !== stId);
    storage.created.push(employee);
    saveStorage(storage);
}

import {
    apiListEmployees,
    apiGetEmployeeById,
    apiCreateEmployee,
    apiUpdateEmployee,
    apiDeleteEmployee,
} from "./apiRequests.js";


export async function listEmployees() {
    const storage = loadStorage();
    const apiRes = await apiListEmployees();

    let apiList = [];
    if (Array.isArray(apiRes?.data)) apiList = apiRes.data;

    const deletedSet = new Set((storage.deleted || []).map(String));
    const apiIds = new Set(apiList.map((e) => String(e.id)));

    const merged = apiList.filter((e) => !deletedSet.has(String(e.id))).map((e) => {
        const key = String(e.id);
        const patch = storage.updated?.[e.id] || storage.updated?.[key];
        return patch ? { ...e, ...patch } : e;
    }).concat(
        (storage.created || [])
            .filter((e) => !deletedSet.has(String(e.id)) && !apiIds.has(String(e.id)))
    );

    const parsed = merged.map((e) => EmployeeSchema.parse(e));
    parsed.sort((a, b) => (Number(a.id) || 0) - (Number(b.id) || 0));

    return ListEmployeesResponse.parse({ status: "success", data: parsed });
}

export async function getEmployeeById(id) {
    const {id: parsedId} = GetEmployeeRequest.parse({ id });

    const local = storageGetEmployee(parsedId);
    if (local) {
        return GetEmployeeResponse.parse({
            status: "success",
            data: EmployeeSchema.parse(local),
        });
    }

    try {
        const apiRes = await apiGetEmployeeById(parsedId);
        return GetEmployeeResponse.parse(apiRes);
    } catch (err) {
        if (err.status === 404) {
            throw new Error("ID não encontrado ou inválido.");
        }
        throw err;
    }
}

export async function createEmployee(payload) {
    const parsed = CreateEmployeeRequest.parse(payload);

    const apiRes = await apiCreateEmployee(parsed);
    const apiId = apiRes?.data?.id;

    if (typeof apiId === "number" && apiId >= 0) {
        const official = EmployeeSchema.parse({
            id: apiId,
            employee_name: parsed.name,
            employee_salary: parsed.salary,
            employee_age: parsed.age,
            profile_image: "",
        });
        // Não adicionar ao storage.created - a API já retornará na listagem
        return { status: "success", data: official };
    }

    const localEmployee = EmployeeSchema.parse({
        id: Date.now(),
        employee_name: parsed.name,
        employee_salary: parsed.salary,
        employee_age: parsed.age,
        profile_image: "",
    });

    storageUpsertCreatedEmployee(localEmployee);
    return { status: "success", data: localEmployee };
}

export async function updateEmployee(id, payload) {
    const parsed = UpdateEmployeeRequest.parse({ id, ...payload });
    try {
        await apiUpdateEmployee(parsed.id, {
            name: parsed.name,
            salary: parsed.salary,
            age: parsed.age,
        });
    } catch (err) {
        if (err.status === 404) {
            throw new Error("ID não encontrado ou inválido.");
        }
        throw err;
    }
    storageUpdateEmployee(parsed.id, {
        employee_name: parsed.name,
        employee_salary: parsed.salary,
        employee_age: parsed.age,
    });
    return { status: "success" };
}


// ---------------- DELETE ----------------

export async function deleteEmployee(id) {
    const { id: parsedId } = DeleteEmployeeRequest.parse({ id });
    try {
        await apiDeleteEmployee(parsedId);
    } catch (err) {
        if (err.status === 404) {
            throw new Error("ID não encontrado ou inválido.");
        }
        throw err;
    }
    storageDeleteEmployee(parsedId);
    return { status: "success" };
}