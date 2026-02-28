from pathlib import Path
import sqlite3
from contextlib import contextmanager
from typing import Iterator

DB_PATH = Path(__file__).parent / "employees.db"

@contextmanager
def get_connection() -> Iterator[sqlite3.Connection]:
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    try:
        yield conn
        conn.commit()
    finally:
        conn.close()


def init_db() -> None:
    with get_connection() as conn:
        conn.execute(
            """
            CREATE TABLE IF NOT EXISTS employees (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                salary TEXT NOT NULL DEFAULT '0',
                age TEXT NOT NULL DEFAULT '0',
                profile_image TEXT NOT NULL DEFAULT ''
            )
            """
        )