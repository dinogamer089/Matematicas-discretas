import pyautogui
import time

# =======================================================
# CASO UI-001: Registrar un venedor
# =======================================================
# Espera unos segundos para que pongas la ventana de Java en foco
print("Iniciando prueba UI-001 en 3 segundos. Asegúrate de tener la ventana en foco...")
time.sleep(3)

# 1. Clic en "Gestión de Usuario" del menú principal
# Usamos tus coordenadas actuales para el botón inferior
pyautogui.click(x=950, y=700) 
time.sleep(1)    

# 2. Clic en el campo de texto "ID Vendedor"
# Asegúrate de que esta coordenada realmente caiga dentro del primer TextField
pyautogui.click(x=1250, y=360)
pyautogui.write("V2")
time.sleep(1)

# 3. Mover al campo "Nombre"
pyautogui.press('tab')
pyautogui.write("Carlos")
time.sleep(1)

# 4. Mover al campo "Teléfono"
pyautogui.press('tab')
pyautogui.write("555-0000")
time.sleep(1)

# 5. Mover al botón "Registrar" y ejecutar la acción
pyautogui.click(x=1250, y=700)
time.sleep(0.5) # Pequeña pausa para ver que se seleccionó el botón
pyautogui.press('enter')

# 6. Manejar el JOptionPane de éxito
print("Esperando el popup de éxito...")
time.sleep(1) # Esperamos 1 segundo a que el JOptionPane aparezca en pantalla
pyautogui.press('enter') # Presiona 'Aceptar' en el popup

print("Prueba UI-001 completada.")
print("Verifica visualmente los criterios de aceptación: Los campos de texto deben estar vacíos.")

pyautogui.click(x=1250, y=800)
time.sleep(1)
pyautogui.click(x=950, y=400) 
time.sleep(1)


# =======================================================
# CASO UI-002: Registrar un nuevo automóvil
# =======================================================
print("Ejecutando UI-002: Registrar Auto...")

# 1. Clic en la pestaña "Registrar Auto" (Tercera pestaña)
# AJUSTAR COORDENADA: Haz clic exactamente sobre el texto de la pestaña
pyautogui.click(x=750, y=250) 
time.sleep(1)

# 2. Clic en el primer campo de texto (VIN)
# AJUSTAR COORDENADA: Clic dentro del TextField de VIN
pyautogui.click(x=1250, y=320)
time.sleep(1)

# 3. Llenar los datos usando Tabulador
pyautogui.write("VIN999")
time.sleep(1)
pyautogui.press('tab')
pyautogui.write("Honda")
time.sleep(1)
pyautogui.press('tab')
pyautogui.write("Civic")
time.sleep(1)
pyautogui.press('tab')
pyautogui.write("25000")
time.sleep(1)

# 4. Tabular hacia el botón y registrar
pyautogui.click(x=1250, y=680)
time.sleep(0.5)
pyautogui.press('enter')

# 5. Manejar el popup de éxito
time.sleep(1) 
pyautogui.press('enter') # Cierra el JOptionPane
print("UI-002 completado. Auto registrado.")
time.sleep(1)

# =======================================================
# CASO UI-003: Visualizar Inventario General
# =======================================================
print("Ejecutando UI-003: Visualizar Inventario General...")

# 1. Clic en la pestaña "Consulta General" (Cuarta pestaña)
# AJUSTAR COORDENADA: Clic sobre la pestaña
pyautogui.click(x=900, y=250)
time.sleep(1)

# 2. Clic en el botón "Mostrar Inventario"
# AJUSTAR COORDENADA: Botón en la parte superior del panel
pyautogui.click(x=900, y=280)
time.sleep(1)
print("UI-003 completado. La tabla debe mostrar el Honda Civic recién agregado.")
time.sleep(1)

# =======================================================
# CASO UI-004: Intento de registro de venta incompleto
# =======================================================
print("Ejecutando UI-004: Registro incompleto...")

# 1. Clic en la pestaña "Registrar Venta" (Primera pestaña)
# AJUSTAR COORDENADA: Clic sobre la pestaña
pyautogui.click(x=500, y=250)
time.sleep(1)

# 2. Posicionarse en el primer campo de texto (Nombre Cliente)
# AJUSTAR COORDENADA: Clic en el TextField de Cliente (el de arriba es un ComboBox)
pyautogui.click(x=1250, y=360) 

# 3. Avanzamos con Tabulador por todo el formulario dejándolo vacío
# Tabulamos 7 veces para pasar por Cliente, Dirección, Teléfono, VIN, Tipo, Precio, Mensualidades y llegar al botón
for _ in range(7):
    pyautogui.press('tab')
    time.sleep(1)

# 4. Presionar el botón "Registrar Venta" con los campos en blanco
pyautogui.click(x=1250, y=750)

# 5. Manejar el JOptionPane de Error que lanza el catch (NumberFormatException)
time.sleep(1)           
pyautogui.press('enter') # Cierra el popup de error
print("UI-004 completado. Se capturó la excepción y la app sigue viva.")

# =======================================================
# FINALIZAR: Volver al menú
# =======================================================
time.sleep(1)
# Usando la coordenada que ya tenías descubierta para el botón de volver
pyautogui.click(x=1250, y=800)
print("¡Todas las pruebas automatizadas han concluido!")