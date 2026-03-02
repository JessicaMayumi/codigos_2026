import {
    ListEmployeesResponse, EmployeeSchema, 
    GetEmployeeRequest, GetEmployeeResponse,
    CreateEmployeeRequest, CreateEmployeeResponse,
    UpdateEmployeeRequest, UpdateEmployeeResponse,
    DeleteEmployeeRequest, DeleteEmployeeResponse,
} from "./models.js";
import {request} from "../http.js";

export async function apiListEmployees() {
    const raw = await request("/employees");
    const parsed = ListEmployeesResponse.safeParse(raw);
    if (parsed.success) return parsed.data;
    const arr = Array.isArray(raw?.data) ? raw.data : Array.isArray(raw) ? raw : [];
    const items = arr.map((e) => {
        const r = EmployeeSchema.safeParse(e);
        if (!r.success) {
            console.warn("[apiListEmployees] Item ignorado (validação):", e, r.error?.issues);
            return null;
        }
        return r.data;
    }).filter((x) => x != null);
    return { status: raw?.status ?? "success", data: items };
}

export async function apiGetEmployeeById(id) {
    const {id: parsedId} = GetEmployeeRequest.parse({id});
    const raw = await request(`/employee/${encodeURIComponent(parsedId)}`);
    return GetEmployeeResponse.parse(raw);
}

export async function apiCreateEmployee(payload) {
    const parsedSchema = CreateEmployeeRequest.parse(payload);
    const raw = await request("/create", {
        method: "POST",
        body: { name: parsedSchema.name, 
                salary: String(parsedSchema.salary), 
                age: String(parsedSchema.age) },
    });
    return CreateEmployeeResponse.parse(raw);
}

export async function apiUpdateEmployee(id, payload) {
    const parsedSchema = CreateEmployeeRequest.parse(payload);
    const raw = await request(`/update/${encodeURIComponent(id)}`, {
        method: "PUT",
        body: { name: parsedSchema.name, 
                salary: String(parsedSchema.salary), 
                age: String(parsedSchema.age) },
    });
    return UpdateEmployeeResponse.parse(raw);
}

export async function apiDeleteEmployee(id) {
    const {id: parsedId} = DeleteEmployeeRequest.parse({id});
    const raw = await request(`/delete/${encodeURIComponent(parsedId)}`, {
        method: "DELETE",
    });
    return DeleteEmployeeResponse.parse(raw);
}

