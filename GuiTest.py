import pyautogui
import time

# Espera unos segundos para que pongas la ventana de Java en foco
print("Iniciando prueba en 3 segundos...")
time.sleep(3)

# 1. Clic en "Gestión de Usuario" (Ajusta las coordenadas X, Y)
pyautogui.click(x=950, y=700) 
time.sleep(1)

# 2. Estamos en la pestaña "Registrar Vendedor". Hacer clic en el campo ID
pyautogui.click(x=1250, y=360)
pyautogui.write("V100")

# 3. Presionar Tab para pasar al nombre
pyautogui.press('tab')
pyautogui.write("Maria Tester")

# 4. Presionar Tab para pasar al teléfono
pyautogui.press('tab')
pyautogui.write("555-9876")

# 5. Presionar Tab para llegar al botón y dar Enter (o clic en el botón "Registrar")
pyautogui.press('tab')
pyautogui.press('enter')

print("Prueba completada. Verifica el mensaje en pantalla.")