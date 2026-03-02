from pathlib import Path

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
from fastapi.responses import RedirectResponse

from app.api import router as api_router

app = FastAPI(
    title="Dummy Employee API",
    description="API de funcionários compatível com dummy.restapiexample.com",
    version="1.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(api_router)

INTERFACE_DIR = Path(__file__).resolve().parent / "interface"
if INTERFACE_DIR.exists():
    app.mount("/static", StaticFiles(directory=str(INTERFACE_DIR)), name="static")


@app.get("/")
def root():
    return RedirectResponse(url="/static/templates/index.html")


@app.get("/vue")
def vue_root():
    return RedirectResponse(url="/static/templates/vue.html")
