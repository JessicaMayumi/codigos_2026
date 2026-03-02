import {CONFIG} from "../configs/settings.js";
import { showNotification } from "./notifications.js";

export async function request(path, { method = "GET", body, retries = 2 } = {}) {
    const response = await fetch(`${CONFIG.API_BASE_URL}${path}`, {
        method,
        headers: { "Content-Type": "application/json" },
        body: body ? JSON.stringify(body) : undefined,
    });

    if (response.status === 429) {
        if (retries > 0) {
            await new Promise((r) => setTimeout(r, 1000));
            return request(path, { method, body, retries: retries - 1 });
        }
        const msg = "Muitas requisições. Aguarde um momento e tente novamente.";
        showNotification(msg, "warning");
        const err = new Error(msg);
        err.status = 429;
        throw err;
    }

    const text = await response.text();
    let data;
    try {
        data = text ? JSON.parse(text) : null;
    } catch (e) {
        throw new Error("Resposta inválida do servidor.");
    }

    if (!response.ok) {
        const msg = typeof data?.detail === "string" ? data.detail : `Erro do servidor (${response.status})`;
        const err = new Error(msg);
        err.status = response.status;
        err.data = data;
        throw err;
    }
    return data;
}