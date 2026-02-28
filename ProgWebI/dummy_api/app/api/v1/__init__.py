from fastapi import APIRouter

from app.api.v1.employees import router as employees_router

router = APIRouter(prefix="/v1")
router.include_router(employees_router)
