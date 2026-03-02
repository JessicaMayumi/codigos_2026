import {z} from "https://cdn.jsdelivr.net/npm/zod@3.23.8/+esm";

const EmployeeId = z.union([z.coerce.number(), z.string()]).transform(v => (typeof v === "string" ? parseInt(v, 10) || 0 : v));
const EmployeeName = z.string().trim().default("");
const EmployeeSalary = z.union([z.coerce.number(), z.string()]).transform(v => {
    const n = typeof v === "string" ? parseFloat(v) : v;
    return Number.isNaN(n) || n < 0 ? 0 : n;
});
const EmployeeAge = z.union([z.coerce.number(), z.string()]).optional().transform(v => {
    if (v === undefined || v === null || v === "") return undefined;
    const n = typeof v === "string" ? parseInt(v, 10) : v;
    return Number.isNaN(n) || n < 0 ? undefined : n;
});
const EmployeeProfileImage = z.string().optional().default("");

export const EmployeeSchema = z.object({
    id: EmployeeId,
    employee_name: EmployeeName,
    employee_salary: EmployeeSalary,
    employee_age: EmployeeAge,
    profile_image: EmployeeProfileImage
})

// ------------- API Models -------------
// --- List Employees ---
export const ListEmployeesResponse = z.object({
    status: z.string(),
    data: z.array(EmployeeSchema)
})

// --- Get Employee by ID  ---
export const GetEmployeeRequest = z.object({
    id: EmployeeId
})

export const GetEmployeeResponse = z.object({
    status: z.string(),
    data: EmployeeSchema
})

// --- Create Employee ---
export const CreateEmployeeRequest = z.object({
    name: EmployeeName.refine((s) => s.length >= 1, "Nome é obrigatório"),
    salary: EmployeeSalary,
    age: EmployeeAge.refine(v => v === undefined || (typeof v === "number" && v > 16 && v < 120), "Idade deve ser maior que 16 e menor que 120")
})

export const CreateEmployeeResponse = z.object({
    status: z.string(),
    data: z.object({
        name: EmployeeName,
        salary: EmployeeSalary,
        age: EmployeeAge,
        id: EmployeeId
    })
})

// --- Update Employee ---
export const UpdateEmployeeRequest = z.object({
    name: EmployeeName.refine((s) => s.length >= 1, "Nome é obrigatório"),
    salary: EmployeeSalary,
    age: EmployeeAge.refine(v => v === undefined || (typeof v === "number" && v > 16 && v < 120), "Idade deve ser maior que 16 e menor que 120")
})

export const UpdateEmployeeResponse = z.object({
    status: z.string(),
    data: z.object({
        name: EmployeeName,
        salary: EmployeeSalary,
        age: EmployeeAge,
        id: EmployeeId
    })
})

// --- Delete Employee ---
export const DeleteEmployeeRequest = z.object({
    id: EmployeeId
})

export const DeleteEmployeeResponse = z.object({
    status: z.string(),
    message: z.string()
})

// ------------- local Storage Models -------------
export const EmployeePatchSchema = z.object({
    employee_name: EmployeeName.optional(),
    employee_salary: EmployeeSalary.optional(),
    employee_age: EmployeeAge.optional(),
    profile_image: EmployeeProfileImage.optional(),
});

export const EmployeeStorageDataSchema = z.object({
    created: z.array(EmployeeSchema).default([]),
    updated: z.record(EmployeePatchSchema).default({}),
    deleted: z.array(EmployeeId).default([]),
});

export const DEFAULT_EMPLOYEE_STORAGE_DATA = EmployeeStorageDataSchema.parse({});
