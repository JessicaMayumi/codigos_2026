const API_BASE = "http://dummy.restapiexample.com/api/v1";

// Melhorar
async function request(path, { method = "GET", body, retries = 3 } = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers: { "Content-Type": "application/json" },
    body: body ? JSON.stringify(body) : undefined
  });

  if (res.status === 429 && retries > 0) {
    const wait = 1000 * (4 - retries); // 1s, 2s, 3s...
    await new Promise(r => setTimeout(r, wait));
    return request(path, { method, body, retries: retries - 1 });
  }

  const text = await res.text();
//   let data;
//   try { data = text ? JSON.parse(text) : null; }
//   catch { throw new Error(`Resposta não-JSON (HTTP ${res.status}): ${text.slice(0, 60)}`); }

//   if (!res.ok) throw new Error(data?.message || `Erro HTTP ${res.status}`);
  return text; //data
}

// Listar os funcionarios
async function listEmployees(){
    return request("/employees");
}

// Buscar funcionario pelo id
// TODO: precisa de uma excessao para quando nao houver um funcionario cadastrado com aquele id
async function getEmployeeById(id){
    if(id){
        return request(`/employee/${encodeURIComponent(id)}`);
    }
    throw new Error("Informe um id");
}

// Criar um Registro de um novo funcionario
// TODO: verificar quais campos sao obrigatorios
async function createEmployee({name, salary, age}){
    if(name){
        return request("/create", {method: "POST", body: {name, salary, age}});
    }
    throw new Error("Informe o nome");
}

// Alterar os dados de um funcionário 
// Todo tenntar entender melhor o encodeduricomponent
async function updateEmployee(id, {name, salary, age}){
    if(id){
        return request(`/update/${encodeURIComponent(id)}`, {method: "PUT", body: {name, salary, age}});
    }
}

// Apagar um registro de um funcionário
async function deleteEmployee(id){
    if(id){
        return request(`/delete/${encodeURIComponent(id)}`, {method: "DELETE"});
    }
}

// Salvar em Storage Interno
// No create

// Teste
const sleep = (ms) => new Promise(r => setTimeout(r, ms));

(async () => {
  try {
    console.log("LIST...");
    console.log(await listEmployees());

    await sleep(18000);

    console.log("getEmployeeById ...");
    console.log(await getEmployeeById(61)); 


    // console.log("CREATE...");
    // console.log(await createEmployee({ name: "May", salary: "123", age: "25" }));

    // console.log("UPDATE...");
    // console.log(await updateEmployee(1, { name: "May Updated", salary: "999", age: "26" }));

    // console.log("DELETE...");
    // console.log(await deleteEmployee(61));
  } catch (e) {
    console.error("ERRO:", e.message);
  }
})();