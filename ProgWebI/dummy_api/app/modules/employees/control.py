from typing import Optional, Any
from app.integrations.connection_factory import get_connection, init_db

init_db()


def _row_to_dict(row) -> dict[str, Any]:
    return dict(zip(row.keys(), row))


def list_all() -> list[dict[str, Any]]:
    with get_connection() as conn:
        rows = conn.execute(
            "SELECT id, name, salary, age, profile_image FROM employees ORDER BY id"
        ).fetchall()
    return [_row_to_dict(r) for r in rows]


def get_by_id(id: int | str) -> Optional[dict[str, Any]]:
    try:
        id = int(id)
    except (ValueError, TypeError):
        return None

    with get_connection() as conn:
        row = conn.execute("SELECT id, name, salary, age, profile_image FROM employees WHERE id = ?", (id,)).fetchone()

    return _row_to_dict(row) if row else None


def create(name: str, salary: str = "0", age: str = "0") -> dict[str, Any]:
    with get_connection() as conn:
        cur = conn.execute(
            "INSERT INTO employees (name, salary, age) VALUES (?, ?, ?)",
            (name, salary, age),
        )
        id = cur.lastrowid

        row = conn.execute("SELECT id, name, salary, age, profile_image FROM employees WHERE id = ?",(id,)).fetchone()

    return _row_to_dict(row)


def update(id: int | str, name: str, salary: str = "0", age: str = "0") -> Optional[dict[str, Any]]:
    try:
        id = int(id)
    except (ValueError, TypeError):
        return None

    with get_connection() as conn:
        cur = conn.execute("UPDATE employees SET name = ?, salary = ?, age = ? WHERE id = ?",(name, salary, age, id))

        if cur.rowcount == 0:
            return None

        row = conn.execute("SELECT id, name, salary, age, profile_image FROM employees WHERE id = ?",(id,)).fetchone()

    return _row_to_dict(row)


def delete(id: int | str) -> bool:
    try:
        id = int(id)
    except (ValueError, TypeError):
        return False

    with get_connection() as conn:
        cur = conn.execute("DELETE FROM employees WHERE id = ?", (id,))
        return cur.rowcount > 0