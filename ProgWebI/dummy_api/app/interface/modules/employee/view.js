
import {
  listEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
  getEmployeeById,
  clearDeletedCache,
} from "./controller.js";
import { showNotification } from "../notifications.js";
import {CONFIG} from "../../configs/settings.js";

// -------------------- Estado (paginação) --------------------
let currentPage = 1;
const pageSize = CONFIG.PAGE_SIZE;
let cachedEmployees = [];

// -------------------- Elementos DOM --------------------
const tableBody = document.getElementById("employeeTableBody");
const btnAnterior = document.getElementById("btnAnterior");
const btnProximo = document.getElementById("btnProximo");
const infoPagina = document.getElementById("infoPagina");
const formInserir = document.getElementById("formInserir");
const nome = document.getElementById("nome");
const salario = document.getElementById("salario");
const idade = document.getElementById("idade");
const formAlterar = document.getElementById("formAlterar");
const idAlterar = document.getElementById("idAlterar");
const nomeAlterar = document.getElementById("nomeAlterar");
const salarioAlterar = document.getElementById("salarioAlterar");
const idadeAlterar = document.getElementById("idadeAlterar");
const btnBuscar = document.getElementById("btnBuscar");
const codigoExcluir = document.getElementById("codigoExcluir");
const btnRemover = document.getElementById("btnRemover");
const btnRefreshList = document.getElementById("btnRefreshList");
const btnClearCache = document.getElementById("btnClearCache");
const msgListaVaziaCache = document.getElementById("msgListaVaziaCache");

// -------------------- Render --------------------
function renderTablePage() {
  if (!tableBody) return;

  const total = cachedEmployees.length;
  const totalPages = Math.max(1, Math.ceil(total / pageSize));
  if (currentPage > totalPages) currentPage = totalPages;
  if (currentPage < 1) currentPage = 1;

  const start = (currentPage - 1) * pageSize;
  const end = start + pageSize;
  const pageItems = cachedEmployees.slice(start, end);

  tableBody.innerHTML = "";
  for (const emp of pageItems) {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td data-label="ID">${emp.id}</td>
      <td data-label="Nome">${emp.employee_name}</td>
      <td data-label="Salário">${emp.employee_salary}</td>
      <td data-label="Idade">${emp.employee_age ?? "-"}</td>
      <td data-label="Ações">
        <md-filled-button class="btnDelete" type="button" data-id="${emp.id}">Excluir</md-filled-button>
      </td>
    `;
    tableBody.appendChild(tr);
  }

  if (infoPagina) infoPagina.textContent = `Página ${currentPage} de ${totalPages}`;
  if (btnAnterior) btnAnterior.disabled = currentPage <= 1;
  if (btnProximo) btnProximo.disabled = currentPage >= totalPages;

  if (msgListaVaziaCache) {
    msgListaVaziaCache.classList.toggle("hidden", total > 0);
  }
}

async function refreshEmployees() {
  try {
    const res = await listEmployees();
    cachedEmployees = res?.data ?? [];
    console.log("[refreshEmployees] recebidos:", cachedEmployees.length, "funcionários");
    if (!tableBody) {
      console.error("[refreshEmployees] tableBody (employeeTableBody) não encontrado no DOM");
    }
    renderTablePage();
  } catch (err) {
    console.error("[refreshEmployees] erro:", err);
    showNotification(err?.message || "Erro ao carregar funcionários.", "danger");
  }
}

// -------------------- Eventos --------------------

if (formInserir) {
  formInserir.addEventListener("submit", async (e) => {
    e.preventDefault();
    const nomeVal = String(nome?.value ?? "").trim();
    const salarioVal = String(salario?.value ?? "").trim();
    if (!nomeVal) {
      showNotification("Nome é obrigatório.", "warning");
      return;
    }
    if (!salarioVal) {
      showNotification("Salário é obrigatório.", "warning");
      return;
    }
    const salNum = parseFloat(salarioVal);
    if (Number.isNaN(salNum) || salNum < 0) {
      showNotification("Salário deve ser um número válido e não negativo.", "warning");
      return;
    }
    try {
      await createEmployee({
        name: nomeVal,
        salary: salarioVal,
        age: idade?.value ?? "",
      });
      if (nome) nome.value = '';
      if (salario) salario.value = '';
      if (idade) idade.value = '';
      currentPage = 1;
      await refreshEmployees();
      showNotification("Funcionário inserido com sucesso.", "success");
    } catch (err) {
      showNotification(err?.message || "Erro ao inserir.", "danger");
    }
  });
}

if (btnBuscar) {
  btnBuscar.addEventListener("click", async () => {
    const id = (idAlterar?.value ?? "").toString().trim();
    if (!id) {
      showNotification("Digite o ID para buscar.", "warning");
      return;
    }
    const idNum = parseInt(id, 10);
    if (Number.isNaN(idNum) || idNum < 0) {
      showNotification("ID deve ser um número válido.", "warning");
      return;
    }
    try {
      const res = await getEmployeeById(id);
      const emp = res?.data;
      if (emp) {
        nomeAlterar.value = emp.employee_name || "";
        salarioAlterar.value = emp.employee_salary ?? "";
        idadeAlterar.value = emp.employee_age ?? "";
        showNotification("Funcionário encontrado.", "success");
      }
    } catch (err) {
      showNotification(err?.message || "ID não encontrado ou inválido.", "danger");
      nomeAlterar.value = "";
      salarioAlterar.value = "";
      idadeAlterar.value = "";
    }
  });
}

if (formAlterar) {
  formAlterar.addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = (idAlterar?.value ?? "").toString().trim();
    if (!id) {
      showNotification("Digite o ID do funcionário para alterar.", "warning");
      return;
    }
    const idNum = Number(id);
    if (Number.isNaN(idNum) || idNum < 0) {
      showNotification("ID deve ser um número válido.", "warning");
      return;
    }
    const nomeVal = String(nomeAlterar?.value ?? "").trim();
    const salarioVal = String(salarioAlterar?.value ?? "").trim();
    if (!nomeVal) {
      showNotification("Nome é obrigatório para alterar.", "warning");
      return;
    }
    if (!salarioVal) {
      showNotification("Salário é obrigatório para alterar.", "warning");
      return;
    }
    const salNum = parseFloat(salarioVal);
    if (Number.isNaN(salNum) || salNum < 0) {
      showNotification("Salário deve ser um número válido e não negativo.", "warning");
      return;
    }
    try {
      await updateEmployee(idNum, {
        name: nomeVal,
        salary: salarioVal,
        age: idadeAlterar?.value ?? "",
      });
      if (idAlterar) idAlterar.value = '';
      if (nomeAlterar) nomeAlterar.value = '';
      if (salarioAlterar) salarioAlterar.value = '';
      if (idadeAlterar) idadeAlterar.value = '';
      await refreshEmployees();
      showNotification("Funcionário atualizado com sucesso.", "success");
    } catch (err) {
      showNotification(err?.message || "Erro ao atualizar.", "danger");
    }
  });
}

if (btnRemover) {
  btnRemover.addEventListener("click", async () => {
    const id = (codigoExcluir?.value ?? "").toString().trim();
    if (!id) {
      showNotification("Digite o ID para excluir.", "warning");
      return;
    }
    const idNum = parseInt(id, 10);
    if (Number.isNaN(idNum) || idNum < 0) {
      showNotification("ID deve ser um número válido.", "warning");
      return;
    }
    try {
      await deleteEmployee(id);
      codigoExcluir.value = "";
      await refreshEmployees();
      showNotification("Funcionário excluído com sucesso.", "success");
    } catch (err) {
      showNotification(err?.message || "Erro ao excluir.", "danger");
    }
  });
}

if (tableBody) {
  tableBody.addEventListener("click", async (e) => {
    const btn = e.target.closest(".btnDelete");
    if (!btn) return;
    const id = btn.dataset.id;
    try {
      await deleteEmployee(id);
      await refreshEmployees();
      showNotification("Funcionário excluído com sucesso.", "success");
    } catch (err) {
      showNotification(err?.message || "Erro ao excluir.", "danger");
    }
  });
}

if (btnRefreshList) {
  btnRefreshList.addEventListener("click", () => refreshEmployees());
}

if (btnClearCache) {
  btnClearCache.addEventListener("click", async () => {
    clearDeletedCache();
    await refreshEmployees();
    showNotification("Cache de exclusões limpo.", "success");
  });
}

if (btnAnterior) {
  btnAnterior.addEventListener("click", () => {
    currentPage -= 1;
    renderTablePage();
  });
}
if (btnProximo) {
  btnProximo.addEventListener("click", () => {
    currentPage += 1;
    renderTablePage();
  });
}

const tabList = document.getElementById("employeesTab");
if (tabList) {
  tabList.addEventListener("click", (e) => {
    const link = e.target.closest(".nav-link");
    if (!link || link.getAttribute("role") !== "tab") return;
    e.preventDefault();
    const targetId = link.getAttribute("href")?.slice(1);
    if (!targetId) return;
    document.querySelectorAll(".tab-pane").forEach((p) => p.classList.remove("show", "active"));
    document.querySelectorAll(".nav-link").forEach((l) => l.classList.remove("active"));
    const pane = document.getElementById(targetId);
    if (pane) pane.classList.add("show", "active");
    link.classList.add("active");
  });
}

refreshEmployees();
