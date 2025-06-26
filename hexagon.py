import numpy as np
from PIL import Image, ImageDraw

def create_hexagon_mask(filename="hex_mask.png", size=800, margin=50):
    img = Image.new('L', (size, size),255)
    draw = ImageDraw.Draw(img)

    r = (size - margin * 2) // 2
    cx, cy = size // 2, size // 2
    points = [
        (cx + r * np.cos(a), cy + r * np.sin(a))
        for a in np.linspace(0, 2 * np.pi, 7)[:-1]
    ]
    draw.polygon(points, fill=0)
    img.save(filename)

create_hexagon_mask()
