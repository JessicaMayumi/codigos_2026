import {CONFIG} from "../configs/settings.js";
console.log("CONFIG:", CONFIG);
console.log("API_BASE:", CONFIG?.API_BASE_URL);

export async function request(path, { method = "GET", body, retries = 2 } = {}) {
    const response = await fetch(`${CONFIG.API_BASE_URL}${path}`, {
        method,
        headers: { "Content-Type": "application/json" },
        body: body ? JSON.stringify(body) : undefined,
    });

    if (response.status === 429 && retries > 0) {
        await new Promise((r) => setTimeout(r, 1000));
        return request(path, { method, body, retries: retries - 1 });
    }

    const text = await response.text();
    let data;
    try {
        data = text ? JSON.parse(text) : null;
    } catch (e) {
        throw new Error(`Resposta não-JSON (HTTP ${response.status}): ${text.slice(0, 80)}`);
    }

    if (!response.ok) {
        const err = new Error(data?.detail || `Erro HTTP ${response.status}`);
        err.status = response.status;
        err.data = data;
        throw err;
    }
    return data;
}