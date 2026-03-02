import {
  listEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
  getEmployeeById,
  clearDeletedCache,
} from "./controller.js";
import { CONFIG } from "../../configs/settings.js";

const { createApp } = window.Vue;

const pageSize = CONFIG.PAGE_SIZE;
const MOBILE_BREAKPOINT = 768;

const app = createApp({
  data() {
    return {
      tabAtivo: 1,
      funcionarios: [],
      paginaAtual: 1,
      formInserir: { nome: "", salario: "", idade: "" },
      formAlterar: { id: "", nome: "", salario: "", idade: "" },
      codigoExcluir: "",
      notifications: [],
      notificationId: 0,
      isMobile: false,
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
    formatarSalario(v) {
      const n = parseFloat(v);
      return Number.isNaN(n) ? "R$ 0,00" : n.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
    },
    notify(message, type = "info") {
      const id = ++this.notificationId;
      this.notifications.push({ id, message, type });
      setTimeout(() => {
        const idx = this.notifications.findIndex((n) => n.id === id);
        if (idx >= 0) this.notifications.splice(idx, 1);
      }, 5000);
    },
    checkResponsive() {
      this.isMobile = window.innerWidth < MOBILE_BREAKPOINT;
    },
    async refreshEmployees() {
      try {
        const res = await listEmployees();
        this.funcionarios = res.data || [];
      } catch (err) {
        this.notify(err?.message || "Erro ao carregar funcionários.", "danger");
      }
    },
    async limparCache() {
      clearDeletedCache();
      await this.refreshEmployees();
      this.notify("Cache de exclusões limpo.", "success");
    },
    async inserir() {
      const nome = String(this.formInserir.nome ?? "").trim();
      const salario = String(this.formInserir.salario ?? "").trim();
      if (!nome) {
        this.notify("Nome é obrigatório.", "warning");
        return;
      }
      if (salario === "") {
        this.notify("Salário é obrigatório.", "warning");
        return;
      }
      const salNum = parseFloat(salario);
      if (Number.isNaN(salNum) || salNum < 0) {
        this.notify("Salário deve ser um número válido e não negativo.", "warning");
        return;
      }
      const idadeVal = String(this.formInserir.idade ?? "").trim();
      if (idadeVal !== "") {
        const idadeNum = parseInt(idadeVal, 10);
        if (Number.isNaN(idadeNum) || idadeNum <= 16 || idadeNum >= 120) {
          this.notify("Idade deve ser maior que 16 e menor que 120.", "warning");
          return;
        }
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
        this.notify("Funcionário inserido com sucesso.", "success");
      } catch (err) {
        this.notify(err?.message || "Erro ao inserir.", "danger");
      }
    },
    async buscarPorId() {
      const id = (this.$refs.idAlterarRef?.value ?? this.formAlterar.id ?? "").toString().trim();
      if (!id) {
        this.notify("Digite o ID para buscar.", "warning");
        return;
      }
      const idNum = parseInt(id, 10);
      if (Number.isNaN(idNum) || idNum < 0) {
        this.notify("ID deve ser um número válido.", "warning");
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
          this.notify("Funcionário encontrado.", "success");
        }
      } catch (err) {
        this.notify(err?.message || "ID não encontrado ou inválido.", "danger");
        this.formAlterar.nome = "";
        this.formAlterar.salario = "";
        this.formAlterar.idade = "";
      }
    },
    async alterar() {
      const idRaw = this.$refs.idAlterarRef?.value ?? this.formAlterar.id;
      const idStr = (idRaw === null || idRaw === undefined ? "" : String(idRaw)).trim();
      if (!idStr) {
        this.notify("Digite o ID do funcionário para alterar.", "warning");
        return;
      }
      const idNum = parseInt(idStr, 10);
      if (Number.isNaN(idNum) || idNum < 0) {
        this.notify("ID deve ser um número válido.", "warning");
        return;
      }
      const nome = String(this.formAlterar.nome ?? "").trim();
      const salario = String(this.formAlterar.salario ?? "").trim();
      if (!nome) {
        this.notify("Nome é obrigatório para alterar.", "warning");
        return;
      }
      if (salario === "") {
        this.notify("Salário é obrigatório para alterar.", "warning");
        return;
      }
      const salNum = parseFloat(salario);
      if (Number.isNaN(salNum) || salNum < 0) {
        this.notify("Salário deve ser um número válido e não negativo.", "warning");
        return;
      }
      const idadeVal = String(this.formAlterar.idade ?? "").trim();
      if (idadeVal !== "") {
        const idadeNum = parseInt(idadeVal, 10);
        if (Number.isNaN(idadeNum) || idadeNum <= 16 || idadeNum >= 120) {
          this.notify("Idade deve ser maior que 16 e menor que 120.", "warning");
          return;
        }
      }
      try {
        await updateEmployee(idNum, {
          name: nome,
          salary: salario,
          age: this.formAlterar.idade ?? "",
        });
        this.formAlterar = { id: "", nome: "", salario: "", idade: "" };
        await this.refreshEmployees();
        this.notify("Funcionário atualizado com sucesso.", "success");
      } catch (err) {
        this.notify(err?.message || "Erro ao atualizar.", "danger");
      }
    },
    async removerPorCodigo() {
      const id = this.codigoExcluir?.toString()?.trim();
      if (!id) {
        this.notify("Digite o ID para excluir.", "warning");
        return;
      }
      const idNum = parseInt(id, 10);
      if (Number.isNaN(idNum) || idNum < 0) {
        this.notify("ID deve ser um número válido.", "warning");
        return;
      }
      try {
        await deleteEmployee(id);
        this.codigoExcluir = "";
        await this.refreshEmployees();
        this.notify("Funcionário excluído com sucesso.", "success");
      } catch (err) {
        this.notify(err?.message || "Erro ao excluir.", "danger");
      }
    },
    async excluirPorId(id) {
      if (id === undefined || id === null || id === "") {
        this.notify("ID inválido para exclusão.", "warning");
        return;
      }
      try {
        await deleteEmployee(id);
        await this.refreshEmployees();
        this.notify("Funcionário excluído com sucesso.", "success");
      } catch (err) {
        this.notify(err?.message || "Erro ao excluir.", "danger");
      }
    },
  },
  mounted() {
    this.refreshEmployees();
    this.checkResponsive();
    window.addEventListener("resize", this.checkResponsive);
  },
  beforeUnmount() {
    window.removeEventListener("resize", this.checkResponsive);
  },
});

app.mount("#app");
