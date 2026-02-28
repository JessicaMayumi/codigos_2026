from fastapi import APIRouter, HTTPException

from app.api.v1.employees.models import (
    EmployeeCreateRequest,
    EmployeeDataResponse,
    EmployeeCreateDataResponse,
    EmployeesListResponse,
    EmployeeSingleResponse,
    EmployeeCreateResponse,
    EmployeeDeleteResponse,
)
from app.modules.employees import control as employee_control

router = APIRouter(tags=["employees"])


def _to_employee_data(emp: dict) -> EmployeeDataResponse:
    """Converte registro interno para formato de resposta."""
    return EmployeeDataResponse(
        id=emp["id"],
        employee_name=emp.get("name", ""),
        employee_salary=str(emp.get("salary", "0")),
        employee_age=str(emp.get("age", "0")),
        profile_image=emp.get("profile_image", ""),
    )


def _to_create_response(emp: dict) -> EmployeeCreateDataResponse:
    """Formato de resposta do create/update."""
    return EmployeeCreateDataResponse(
        id=emp["id"],
        name=emp.get("name", ""),
        salary=str(emp.get("salary", "0")),
        age=str(emp.get("age", "0")),
    )


@router.get("/employees", response_model=EmployeesListResponse)
def list_employees():
    """GET /api/v1/employees - Lista todos os funcionários."""
    data = [_to_employee_data(e) for e in employee_control.list_all()]
    return EmployeesListResponse(data=data)


@router.get("/employee/{id}", response_model=EmployeeSingleResponse)
def get_employee(id: int | str):
    """GET /api/v1/employee/{id} - Busca um funcionário pelo ID."""
    emp = employee_control.get_by_id(id)
    if emp is None:
        raise HTTPException(status_code=404, detail="Employee not found")
    return EmployeeSingleResponse(data=_to_employee_data(emp))


@router.post("/create", response_model=EmployeeCreateResponse)
def create_employee(body: EmployeeCreateRequest):
    """Cria um novo funcionário."""
    emp = employee_control.create(name=body.name, salary=body.salary, age=body.age)
    return EmployeeCreateResponse(data=_to_create_response(emp))


@router.put("/update/{id}", response_model=EmployeeCreateResponse)
def update_employee(id: int | str, body: EmployeeCreateRequest):
    """PUT /api/v1/update/{id} - Atualiza um funcionário."""
    emp = employee_control.update(id, name=body.name, salary=body.salary, age=body.age)
    if emp is None:
        raise HTTPException(status_code=404, detail="Employee not found")
    return EmployeeCreateResponse(data=_to_create_response(emp))


@router.delete("/delete/{id}", response_model=EmployeeDeleteResponse)
def delete_employee(id: int | str):
    """DELETE /api/v1/delete/{id} - Remove um funcionário."""
    if not employee_control.delete(id):
        raise HTTPException(status_code=404, detail="Employee not found")
    return EmployeeDeleteResponse()
