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
JAVA_CLASSPATH = PROJECT_ROOT

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
    java_executable = r"C:\Program Files\Java\jdk-17\bin\java.exe"
    command = [java_executable, "-cp", str(JAVA_CLASSPATH), JAVA_MAIN_CLASS]
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

def check_window_exists(expected_title: str) -> bool:
    for hwnd, title in _iter_window_titles():
        if expected_title.lower() in title.lower():
            return True
    return False

pyautogui.PAUSE = 0.3

def test_ui_001_registro_usuario() -> None:
    print("Iniciando prueba UI-001.")
    pyautogui.click(x=950, y=700)
    time.sleep(0.5)
    pyautogui.click(x=1250, y=360)
    time.sleep(0.5)
    pyautogui.write("V2")
    time.sleep(0.5)
    pyautogui.press("tab")
    pyautogui.write("Carlos")
    time.sleep(0.5)
    pyautogui.press("tab")
    pyautogui.write("555-0000")
    time.sleep(0.5)
    pyautogui.click(x=1250, y=700)
    time.sleep(0.5)
    mensaje_exito = pyautogui.locateOnScreen('registro_vendedor_exitoso.png', confidence=0.9)
    assert mensaje_exito is not None, "FALLO: No se detectó el mensaje de éxito en pantalla."
    pyautogui.press("enter")
    time.sleep(0.5)
    pyautogui.press("enter")
    time.sleep(0.5)

def test_ui_002_registrar_auto() -> None:
    print("Ejecutando UI-002: Registrar Auto...")
    pyautogui.click(x=1250, y=800)
    time.sleep(0.5)
    pyautogui.click(x=950, y=400)
    time.sleep(0.5)
    pyautogui.click(x=750, y=250)
    time.sleep(0.5)
    pyautogui.click(x=1250, y=320)
    time.sleep(0.5)
    pyautogui.write("VIN999")
    time.sleep(0.5)
    pyautogui.press("tab")
    pyautogui.write("Honda")
    time.sleep(0.5)
    pyautogui.press("tab")
    pyautogui.write("Civic")
    time.sleep(0.5)
    pyautogui.press("tab")
    pyautogui.write("25000")
    time.sleep(0.5)
    pyautogui.click(x=1250, y=680)
    time.sleep(0.5)
    mensaje_exito = pyautogui.locateOnScreen('registro_auto_exitoso.png', confidence=0.9)
    assert mensaje_exito is not None, "FALLO: No se detectó el mensaje de éxito en pantalla."
    pyautogui.press("enter")
    time.sleep(0.5)

def test_ui_003_visualizar_inventario() -> None:
    print("Ejecutando UI-003: Visualizar Inventario General...")
    pyautogui.click(x=900, y=250)
    time.sleep(0.5)
    pyautogui.click(x=900, y=280)
    time.sleep(0.5)

def test_ui_004_registro_incompleto() -> None:
    print("Ejecutando UI-004: Registro incompleto...")
    pyautogui.click(x=500, y=250)
    time.sleep(0.5)
    pyautogui.click(x=1250, y=360)
    time.sleep(0.5)
    for _ in range(4):
        pyautogui.press("tab")
    pyautogui.click(x=1250, y=750)
    mensaje_fallido = pyautogui.locateOnScreen('error_venta_incompleto.png', confidence=0.9)
    assert mensaje_fallido is not None, "FALLO: No se detectó el mensaje de falla en pantalla."
    time.sleep(0.5)
    pyautogui.press("enter")
    time.sleep(0.5)
    pyautogui.click(x=1250, y=800)
    time.sleep(0.5)