from pynput import mouse

def on_click(x, y, button, pressed):
    # Only act when left button is pressed (not released)
    if button == mouse.Button.left and pressed:
        print(f"Left click at: ({x}, {y})")

# Start listening to mouse events
with mouse.Listener(on_click=on_click) as listener:
    listener.join()