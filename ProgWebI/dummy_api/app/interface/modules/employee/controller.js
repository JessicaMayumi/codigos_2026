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

    let apiList = [];
    try {
        const apiRes = await apiListEmployees();
        if (Array.isArray(apiRes?.data)) {
            apiList = apiRes.data;
        } else if (Array.isArray(apiRes)) {
            apiList = apiRes;
        }
        console.log("[listEmployees] API retornou", apiList.length, "itens");
    } catch (err) {
        apiList = [];
        console.error("[listEmployees] Erro na API:", err?.message || err);
        if (err?.status === 404 || err?.status >= 500) {
            throw new Error("Não foi possível carregar a lista. Verifique se o servidor está rodando.");
        }
    }

    const deletedSet = new Set((storage.deleted || []).map(String));
    if (deletedSet.size > 0) {
        console.log("[listEmployees] storage.deleted filtra estes IDs:", [...deletedSet]);
    }
    const apiIds = new Set(apiList.map((e) => String(e.id)));

    const applyPatch = (emp) => {
        const key = String(emp.id);
        const patch = storage.updated?.[key];
        return patch ? { ...emp, ...patch } : emp;
    };
    const merged = apiList
        .filter((e) => !deletedSet.has(String(e.id)))
        .map(applyPatch)
        .concat(
            (storage.created || [])
                .filter((e) => !deletedSet.has(String(e.id)) && !apiIds.has(String(e.id)))
                .map(applyPatch)
        );

    const parsed = merged
        .map((e) => {
            const r = EmployeeSchema.safeParse(e);
            if (!r.success) {
                console.warn("[listEmployees] Item ignorado:", e, r.error?.issues);
                return null;
            }
            return r.data;
        })
        .filter((x) => x != null);
    parsed.sort((a, b) => (Number(a.id) || 0) - (Number(b.id) || 0));
    console.log("[listEmployees] merged/parsed:", parsed.length, "itens (após filtrar deleted)");

    return ListEmployeesResponse.parse({ status: "success", data: parsed });
}

/** Limpa o cache de exclusões no localStorage. Use quando a lista está vazia mas a API tem dados. */
export function clearDeletedCache() {
    const storage = loadStorage();
    storage.deleted = [];
    saveStorage(storage);
}

export async function getEmployeeById(id) {
    const {id: parsedId} = GetEmployeeRequest.parse({ id });
    const stId = String(parsedId);

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
            const listRes = await listEmployees();
            const emp = listRes?.data?.find((e) => String(e.id) === stId);
            if (emp) {
                return GetEmployeeResponse.parse({ status: "success", data: emp });
            }
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
    const idNum = Number(id);
    if (Number.isNaN(idNum) || idNum < 0) {
        throw new Error("ID deve ser um número válido.");
    }
    let parsed;
    try {
        parsed = UpdateEmployeeRequest.parse(payload);
    } catch (zodErr) {
        const first = zodErr?.issues?.[0];
        const msg = first?.message || zodErr?.message || "Dados inválidos. Verifique nome, salário e idade.";
        throw new Error(msg);
    }
    try {
        await apiUpdateEmployee(idNum, {
            name: parsed.name,
            salary: parsed.salary,
            age: parsed.age,
        });
    } catch (err) {
        if (err.status === 404) {
        } else {
            throw err;
        }
    }
    storageUpdateEmployee(idNum, {
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
        await getEmployeeById(parsedId);
    } catch {
        throw new Error("ID não encontrado. O funcionário não existe.");
    }
    try {
        await apiDeleteEmployee(parsedId);
    } catch (err) {
        if (err.status !== 404) throw err;
    }
    storageDeleteEmployee(parsedId);
    return { status: "success" };
}