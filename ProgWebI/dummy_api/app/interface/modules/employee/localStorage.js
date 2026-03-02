import {CONFIG} from "../../configs/settings.js";
import {EmployeeStorageDataSchema, DEFAULT_EMPLOYEE_STORAGE_DATA} from "./models.js";

export function loadStorage() {
    const raw = localStorage.getItem(CONFIG.STORAGE_KEY);
    if (!raw) return DEFAULT_EMPLOYEE_STORAGE_DATA;
    const parsed = JSON.parse(raw);
    const result = EmployeeStorageDataSchema.safeParse(parsed); 
    return result.success ? result.data : DEFAULT_EMPLOYEE_STORAGE_DATA;
};

export function saveStorage(data) {
    localStorage.setItem(CONFIG.STORAGE_KEY, JSON.stringify(data));
}

export function createEmployee(employee) {
    const storage = loadStorage();
    console.log("Creating employee in storage:", employee);
    console.log("Current storage before creation:", storage);
    storage.created.push(employee);
    saveStorage(storage);
    console.log("Employee created in storage:", employee);
    console.log("Current storage after creation:", storage);
    return storage;
}

export function updateEmployee(id, updatedData) {
    const storage = loadStorage();
    const key = String(id);
    storage.updated = storage.updated || {};
    storage.updated[key] = {
        ...(storage.updated[key] || {}),
        ...updatedData
    };
    saveStorage(storage);
    console.log(`Employee ${id} updated in storage with data:`, updatedData);
    console.log("Current storage after update:", storage);
    return storage;
}

export function listEmployees() {
    const storage = loadStorage();
    console.log("Listing employees from storage");
    console.log("Current storage data:", storage);
    return storage;
}

export function deleteEmployee(id) {
    const storage = loadStorage();
    const stId = String(id);
    storage.deleted = storage.deleted || [];
    storage.created = storage.created || [];
    storage.updated = storage.updated || {};

    if (!storage.deleted.some((d) => String(d) === stId)) {
        storage.deleted.push(stId);
    }
    storage.created = storage.created.filter((emp) => String(emp.id) !== stId);
    if (storage.updated[stId]) {
        delete storage.updated[stId];
    }

    saveStorage(storage);
    console.log(`Employee ${id} marked as deleted in storage`);
    console.log("Current storage after deletion:", storage);
    return storage;
}

export function getEmployee(id) {
    const storage = loadStorage();
    const stId = String(id);
    if (storage.deleted.some((d) => String(d) === stId)) {
        return null;
    }

    const created = storage.created.find((emp) => String(emp.id) === stId);

    if (created) {
        return created;
    }

    const patched = storage.updated?.[stId];
    if (patched) {
        return { id, ...patched };
    }
    console.log(`Employee ${id} not found in storage`);
    console.log("Current storage data:", storage);
    return null;
}