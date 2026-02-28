from pydantic import BaseModel, Field

class EmployeeCreateRequest(BaseModel):
    name: str = Field(..., min_length=1, description="Nome do funcionário")
    salary: str = Field(default="0", description="Salário (string)")
    age: str = Field(default="0", description="Idade (string)")

class EmployeeDataResponse(BaseModel):
    id: int | str
    employee_name: str = ""
    employee_salary: str = "0"
    employee_age: str = "0"
    profile_image: str = ""

class EmployeeCreateDataResponse(BaseModel):
    id: int
    name: str
    salary: str
    age: str

class EmployeesListResponse(BaseModel):
    status: str = "success"
    data: list[EmployeeDataResponse]
    message: str = "Successfully! All records has been fetched."

class EmployeeSingleResponse(BaseModel):
    status: str = "success"
    data: EmployeeDataResponse

class EmployeeCreateResponse(BaseModel):
    status: str = "success"
    data: EmployeeCreateDataResponse

class EmployeeDeleteResponse(BaseModel):
    status: str = "success"
    message: str = "successfully! deleted Records"
