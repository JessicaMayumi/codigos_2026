import {z} from "https://cdn.jsdelivr.net/npm/zod@3.23.8/+esm";

const EmployeeId = z.coerce.number().int().nonnegative();
const EmployeeName = z.string().trim().min(1, "Nome é obrigatório");
const EmployeeSalary = z.coerce.number().nonnegative("Salário não pode ser negativo");
const EmployeeAge = z.coerce.number().int().nonnegative().optional();
const EmployeeProfileImage = z.string().optional();

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
    name: EmployeeName,
    salary: EmployeeSalary,
    age: EmployeeAge
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
    id: EmployeeId,
    name: EmployeeName,
    salary: EmployeeSalary,
    age: EmployeeAge
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