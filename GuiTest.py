from __future__ import annotations

import ctypes
import subprocess
import time
from pathlib import Path

import pyautogui
import pytest


APP_TITLE = "Sistema de Concesionaria"
JAVA_MAIN_CLASS = "ConcesionariaGUI"
PROJECT_ROOT = Path(__file__).resolve().parent
JAVA_CLASSPATH = PROJECT_ROOT / "out" / "production" / "Matematicas-discretas"


def _iter_window_titles() -> list[tuple[int, str]]:
    titles: list[tuple[int, str]] = []

    @ctypes.WINFUNCTYPE(ctypes.c_bool, ctypes.c_void_p, ctypes.c_void_p)
    def enum_windows(hwnd, _):
        if ctypes.windll.user32.IsWindowVisible(hwnd):
            length = ctypes.windll.user32.GetWindowTextLengthW(hwnd)
            if length > 0:
                buffer = ctypes.create_unicode_buffer(length + 1)
                ctypes.windll.user32.GetWindowTextW(hwnd, buffer, length + 1)
                titles.append((int(hwnd), buffer.value))
        return True

    ctypes.windll.user32.EnumWindows(enum_windows, 0)
    return titles


def _find_app_window() -> int | None:
    for hwnd, title in _iter_window_titles():
        if APP_TITLE.lower() in title.lower():
            return hwnd
    return None


def _focus_window(hwnd: int) -> None:
    user32 = ctypes.windll.user32
    user32.ShowWindow(hwnd, 9)
    user32.SetForegroundWindow(hwnd)


def _start_app() -> subprocess.Popen[str]:
    command = ["java", "-cp", str(JAVA_CLASSPATH), JAVA_MAIN_CLASS]
    creationflags = 0
    if hasattr(subprocess, "CREATE_NO_WINDOW"):
        creationflags |= subprocess.CREATE_NO_WINDOW
    if hasattr(subprocess, "CREATE_NEW_PROCESS_GROUP"):
        creationflags |= subprocess.CREATE_NEW_PROCESS_GROUP
    return subprocess.Popen(command, cwd=PROJECT_ROOT, creationflags=creationflags)


def _ensure_app_running(timeout_seconds: int = 30) -> subprocess.Popen[str] | None:
    hwnd = _find_app_window()
    if hwnd is not None:
        _focus_window(hwnd)
        return None

    process = _start_app()
    deadline = time.monotonic() + timeout_seconds
    while time.monotonic() < deadline:
        hwnd = _find_app_window()
        if hwnd is not None:
            _focus_window(hwnd)
            return process
        if process.poll() is not None:
            raise RuntimeError("ConcesionariaGUI exited before the window became ready.")
        time.sleep(0.5)

    raise TimeoutError("Timed out waiting for ConcesionariaGUI to become ready.")


@pytest.fixture(scope="session", autouse=True)
def concesionaria_gui_app() -> None:
    process = _ensure_app_running()
    try:
        yield
    finally:
        if process is not None and process.poll() is None:
            process.terminate()
            try:
                process.wait(timeout=10)
            except subprocess.TimeoutExpired:
                process.kill()


def test_gui_flow() -> None:
    pyautogui.PAUSE = 0.3

    print("Iniciando prueba UI-001.")
    pyautogui.click(x=950, y=700)
    pyautogui.click(x=1250, y=360)
    pyautogui.write("V2")
    pyautogui.press("tab")
    pyautogui.write("Carlos")
    pyautogui.press("tab")
    pyautogui.write("555-0000")
    pyautogui.click(x=1250, y=700)
    pyautogui.press("enter")
    pyautogui.press("enter")

    print("Ejecutando UI-002: Registrar Auto...")
    #ir al menu principal
    pyautogui.click(x=970, y=830)
    #ir a gestion de inventario
    pyautogui.click(x=960, y=430)
    #ir a registrar auto
    pyautogui.click(x=750, y=250)
    #Click en VIN
    pyautogui.click(x=1250, y=320)
    pyautogui.write("VIN999")
    pyautogui.press("tab")
    pyautogui.write("Honda")
    pyautogui.press("tab")
    pyautogui.write("Civic")
    pyautogui.press("tab")
    pyautogui.write("25000")
    pyautogui.click(x=1250, y=680)
    pyautogui.press("enter")

    print("Ejecutando UI-003: Visualizar Inventario General...")
    pyautogui.click(x=930, y=270)
    pyautogui.click(x=950, y=310)

    print("Ejecutando UI-004: Registro incompleto...")
    pyautogui.click(x=790, y=270)
    pyautogui.click(x=1250, y=360)
    for _ in range(4):
        pyautogui.press("tab")
    pyautogui.click(x=1250, y=670)
    pyautogui.press("enter")

    pyautogui.click(x=1250, y=820)