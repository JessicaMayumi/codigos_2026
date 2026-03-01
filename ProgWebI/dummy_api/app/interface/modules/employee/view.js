// view.js (Vanilla JS - Modo Nativo)
import {
  listEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
  getEmployeeById,
} from "./controller.js";
import { showNotification } from "../notifications.js";

// -------------------- Estado (paginação) --------------------
let currentPage = 1;
const pageSize = 8;
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
      <td>${emp.id}</td>
      <td>${emp.employee_name}</td>
      <td>${emp.employee_salary}</td>
      <td>${emp.employee_age ?? "-"}</td>
      <td>
        <button type="button" class="btn btn-danger btn-sm btnDelete" data-id="${emp.id}">Excluir</button>
      </td>
    `;
    tableBody.appendChild(tr);
  }

  if (infoPagina) infoPagina.textContent = `Página ${currentPage} de ${totalPages}`;
  if (btnAnterior) btnAnterior.disabled = currentPage <= 1;
  if (btnProximo) btnProximo.disabled = currentPage >= totalPages;
}

async function refreshEmployees() {
  try {
    const res = await listEmployees();
    cachedEmployees = res.data || [];
    renderTablePage();
  } catch (err) {
    showNotification(err?.message || "Erro ao carregar funcionários.", "danger");
  }
}

// -------------------- Eventos --------------------

if (formInserir) {
  formInserir.addEventListener("submit", async (e) => {
    e.preventDefault();
    try {
      await createEmployee({
        name: nome.value,
        salary: salario.value,
        age: idade.value,
      });
      formInserir.reset();
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
    const id = idAlterar?.value;
    if (!id) return showNotification("Digite o ID para buscar.", "warning");
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
    try {
      await updateEmployee(idAlterar.value, {
        name: nomeAlterar.value,
        salary: salarioAlterar.value,
        age: idadeAlterar.value,
      });
      formAlterar.reset();
      await refreshEmployees();
      showNotification("Funcionário atualizado com sucesso.", "success");
    } catch (err) {
      showNotification(err?.message || "Erro ao atualizar.", "danger");
    }
  });
}

if (btnRemover) {
  btnRemover.addEventListener("click", async () => {
    const id = codigoExcluir.value;
    if (!id) return showNotification("Digite o ID para excluir.", "warning");
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

// -------------------- Init --------------------
refreshEmployees();
