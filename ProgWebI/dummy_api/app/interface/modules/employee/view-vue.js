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

const { createApp } = window.Vue;

const pageSize = CONFIG.PAGE_SIZE;

const app = createApp({
  data() {
    return {
      tabAtivo: 1,
      funcionarios: [],
      paginaAtual: 1,
      formInserir: { nome: "", salario: "", idade: "" },
      formAlterar: { id: "", nome: "", salario: "", idade: "" },
      codigoExcluir: "",
    };
  },
  computed: {
    totalPaginas() {
      return Math.max(1, Math.ceil(this.funcionarios.length / pageSize));
    },
    funcionariosPagina() {
      const totalPages = this.totalPaginas;
      let page = this.paginaAtual;
      if (page > totalPages) page = totalPages;
      if (page < 1) page = 1;
      const start = (page - 1) * pageSize;
      const end = start + pageSize;
      return this.funcionarios.slice(start, end);
    },
  },
  watch: {
    funcionarios() {
      if (this.paginaAtual > this.totalPaginas) this.paginaAtual = this.totalPaginas;
      if (this.paginaAtual < 1) this.paginaAtual = 1;
    },
  },
  methods: {
    async refreshEmployees() {
      try {
        const res = await listEmployees();
        this.funcionarios = res.data || [];
      } catch (err) {
        showNotification(err?.message || "Erro ao carregar funcionários.", "danger");
      }
    },
    async limparCache() {
      clearDeletedCache();
      await this.refreshEmployees();
      showNotification("Cache de exclusões limpo.", "success");
    },
    async inserir() {
      const nome = String(this.formInserir.nome ?? "").trim();
      const salario = String(this.formInserir.salario ?? "").trim();
      if (!nome) {
        showNotification("Nome é obrigatório.", "warning");
        return;
      }
      if (salario === "") {
        showNotification("Salário é obrigatório.", "warning");
        return;
      }
      const salNum = parseFloat(salario);
      if (Number.isNaN(salNum) || salNum < 0) {
        showNotification("Salário deve ser um número válido e não negativo.", "warning");
        return;
      }
      try {
        await createEmployee({
          name: nome,
          salary: salario,
          age: this.formInserir.idade ?? "",
        });
        this.formInserir = { nome: "", salario: "", idade: "" };
        this.paginaAtual = 1;
        await this.refreshEmployees();
        showNotification("Funcionário inserido com sucesso.", "success");
      } catch (err) {
        showNotification(err?.message || "Erro ao inserir.", "danger");
      }
    },
    async buscarPorId() {
      const id = (this.$refs.idAlterarRef?.value ?? this.formAlterar.id ?? "").toString().trim();
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
          this.formAlterar.id = emp.id ?? id;
          this.formAlterar.nome = emp.employee_name || "";
          this.formAlterar.salario = emp.employee_salary ?? "";
          this.formAlterar.idade = emp.employee_age ?? "";
          this.$nextTick(() => {
            if (this.$refs.idAlterarRef) this.$refs.idAlterarRef.value = String(emp.id ?? id);
          });
          showNotification("Funcionário encontrado.", "success");
        }
      } catch (err) {
        showNotification(err?.message || "ID não encontrado ou inválido.", "danger");
        this.formAlterar.nome = "";
        this.formAlterar.salario = "";
        this.formAlterar.idade = "";
      }
    },
    async alterar() {
      const idRaw = this.$refs.idAlterarRef?.value ?? this.formAlterar.id;
      const idStr = (idRaw === null || idRaw === undefined ? "" : String(idRaw)).trim();
      if (!idStr) {
        showNotification("Digite o ID do funcionário para alterar.", "warning");
        return;
      }
      const idNum = parseInt(idStr, 10);
      if (Number.isNaN(idNum) || idNum < 0) {
        showNotification("ID deve ser um número válido.", "warning");
        return;
      }
      const nome = String(this.formAlterar.nome ?? "").trim();
      const salario = String(this.formAlterar.salario ?? "").trim();
      if (!nome) {
        showNotification("Nome é obrigatório para alterar.", "warning");
        return;
      }
      if (salario === "") {
        showNotification("Salário é obrigatório para alterar.", "warning");
        return;
      }
      const salNum = parseFloat(salario);
      if (Number.isNaN(salNum) || salNum < 0) {
        showNotification("Salário deve ser um número válido e não negativo.", "warning");
        return;
      }
      try {
        await updateEmployee(idNum, {
          name: nome,
          salary: salario,
          age: this.formAlterar.idade ?? "",
        });
        this.formAlterar = { id: "", nome: "", salario: "", idade: "" };
        await this.refreshEmployees();
        showNotification("Funcionário atualizado com sucesso.", "success");
      } catch (err) {
        showNotification(err?.message || "Erro ao atualizar.", "danger");
      }
    },
    async removerPorCodigo() {
      const id = this.codigoExcluir?.toString()?.trim();
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
        this.codigoExcluir = "";
        await this.refreshEmployees();
        showNotification("Funcionário excluído com sucesso.", "success");
      } catch (err) {
        showNotification(err?.message || "Erro ao excluir.", "danger");
      }
    },
    async excluirPorId(id) {
      if (id === undefined || id === null || id === "") {
        showNotification("ID inválido para exclusão.", "warning");
        return;
      }
      try {
        await deleteEmployee(id);
        await this.refreshEmployees();
        showNotification("Funcionário excluído com sucesso.", "success");
      } catch (err) {
        showNotification(err?.message || "Erro ao excluir.", "danger");
      }
    },
  },
  mounted() {
    this.refreshEmployees();
  },
});

app.mount("#app");
