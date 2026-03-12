function parseEmployeeId(v) {
    return typeof v === "string" ? parseInt(v, 10) || 0 : Number(v) || 0;
}
function parseEmployeeName(v) {
    return typeof v === "string" ? String(v).trim() : "";
}
function parseEmployeeSalary(v) {
    const n = typeof v === "string" ? parseFloat(v) : Number(v);
    return Number.isNaN(n) || n < 0 ? 0 : n;
}
function parseEmployeeAge(v) {
    if (v === undefined || v === null || v === "") return undefined;
    const n = typeof v === "string" ? parseInt(v, 10) : Number(v);
    return Number.isNaN(n) || n < 0 ? undefined : n;
}
function parseEmployeeProfileImage(v) {
    return v != null && v !== "" ? String(v) : "";
}
function createValidationError(message, issues = [{ message }]) {
    const err = new Error(message);
    err.issues = issues;
    return err;
}
function result(success, data = null, error = null) {
    return success ? { success: true, data } : { success: false, error };
}

// --- EmployeeSchema ---
function parseEmployee(obj) {
    if (!obj || typeof obj !== "object") {
        throw createValidationError("Objeto inválido");
    }
    return {
        id: parseEmployeeId(obj.id),
        employee_name: parseEmployeeName(obj.employee_name),
        employee_salary: parseEmployeeSalary(obj.employee_salary),
        employee_age: parseEmployeeAge(obj.employee_age),
        profile_image: parseEmployeeProfileImage(obj.profile_image),
    };
}
export const EmployeeSchema = {
    parse(obj) { return parseEmployee(obj); },
    safeParse(obj) {
        try { return result(true, parseEmployee(obj)); }
        catch (e) { return result(false, null, e); }
    },
};

// --- API Models ---
export const ListEmployeesResponse = {
    parse(obj) {
        const data = Array.isArray(obj?.data) ? obj.data.map((e) => parseEmployee(e)) : [];
        return { status: String(obj?.status ?? "success"), data };
    },
};
export const GetEmployeeRequest = {
    parse(obj) { return { id: parseEmployeeId(obj?.id) }; },
};
export const GetEmployeeResponse = {
    parse(obj) {
        return {
            status: String(obj?.status ?? "success"),
            data: parseEmployee(obj?.data ?? obj),
        };
    },
};
function parseCreatePayload(obj) {
    const name = parseEmployeeName(obj?.name ?? obj?.employee_name);
    if (!name || name.length < 1) {
        throw createValidationError("Nome é obrigatório", [{ message: "Nome é obrigatório" }]);
    }
    return {
        name,
        salary: parseEmployeeSalary(obj?.salary ?? obj?.employee_salary),
        age: parseEmployeeAge(obj?.age ?? obj?.employee_age),
    };
}
export const CreateEmployeeRequest = {
    parse(obj) { return parseCreatePayload(obj); },
};
export const CreateEmployeeResponse = {
    parse(obj) {
        const d = obj?.data ?? obj;
        return {
            status: String(obj?.status ?? "success"),
            data: {
                name: parseEmployeeName(d?.name ?? d?.employee_name),
                salary: parseEmployeeSalary(d?.salary ?? d?.employee_salary),
                age: parseEmployeeAge(d?.age ?? d?.employee_age),
                id: parseEmployeeId(d?.id),
            },
        };
    },
};
export const UpdateEmployeeRequest = {
    parse(obj) { return parseCreatePayload(obj); },
};
export const UpdateEmployeeResponse = {
    parse(obj) {
        const d = obj?.data ?? obj;
        return {
            status: String(obj?.status ?? "success"),
            data: {
                name: parseEmployeeName(d?.name ?? d?.employee_name),
                salary: parseEmployeeSalary(d?.salary ?? d?.employee_salary),
                age: parseEmployeeAge(d?.age ?? d?.employee_age),
                id: parseEmployeeId(d?.id),
            },
        };
    },
};
export const DeleteEmployeeRequest = {
    parse(obj) { return { id: parseEmployeeId(obj?.id) }; },
};
export const DeleteEmployeeResponse = {
    parse(obj) {
        return {
            status: String(obj?.status ?? "success"),
            message: String(obj?.message ?? ""),
        };
    },
};

// --- Storage ---
function parseEmployeePatch(obj) {
    if (!obj || typeof obj !== "object") return {};
    const out = {};
    if (obj.employee_name !== undefined) out.employee_name = parseEmployeeName(obj.employee_name);
    if (obj.employee_salary !== undefined) out.employee_salary = parseEmployeeSalary(obj.employee_salary);
    if (obj.employee_age !== undefined) out.employee_age = parseEmployeeAge(obj.employee_age);
    if (obj.profile_image !== undefined) out.profile_image = parseEmployeeProfileImage(obj.profile_image);
    return out;
}
function parseStorageData(raw) {
    const created = Array.isArray(raw?.created) ? raw.created.map((e) => parseEmployee(e)) : [];
    const updated = raw?.updated && typeof raw.updated === "object"
        ? Object.fromEntries(Object.entries(raw.updated).map(([k, v]) => [String(k), parseEmployeePatch(v)]))
        : {};
    const deleted = Array.isArray(raw?.deleted) ? raw.deleted.map((v) => parseEmployeeId(v)) : [];
    return { created, updated, deleted };
}
export const EmployeeStorageDataSchema = {
    safeParse(raw) {
        try { return result(true, parseStorageData(raw ?? {})); }
        catch (e) { return result(false, null, e); }
    },
};
export const DEFAULT_EMPLOYEE_STORAGE_DATA = parseStorageData({});
