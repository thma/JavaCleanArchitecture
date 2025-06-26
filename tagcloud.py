from wordcloud import WordCloud
import matplotlib.pyplot as plt
import numpy as np
from PIL import Image

# Load mask
mask = np.array(Image.open("hex_mask.png"))

# Define grouped tags
tags = {
    # Architecture (blue shades)
    "Clean Architecture": 100,

    "Onion": 80,
    "Hexagonal": 80,
    "Ports and Adapters": 75,
    
    # Design Principles (green shades)
    "Dependency Rule": 90,
    "Inversion of Control": 80,
    "Dependency Inversion": 50,
    "Separation of Concerns": 50,
    "SOLID Principles": 50,
    "Domain-Driven Design": 75,
    "Persistence Ignorance": 20,

    # Implementation Elements (orange shades)
    "Entities": 55,
    "Repositories": 55,
    "Adapters": 45,
    "Controllers": 30,
    "Use Cases": 30,
    "Ports": 30,
    
    # Testing & Quality (purple shades)
    "Testability": 45,
    "Mocking": 25,
    "Decoupling": 25,
    "Flexibility": 25,
    "Refactorability": 20,
    "Evolvability": 20,
    "Maintainability": 20,

    # Tooling & Process (gray shades)
    "Spring Boot": 20,
    "ArchUnit": 20,
    "Framework Agnostic": 20,
}

# Define color categories
color_map = {
    "blue": {
        "Clean Architecture", "Onion",
        "Hexagonal", "Ports and Adapters"
    },
    "green": {
        "Use Cases", "Inversion of Control", "Dependency Inversion", "Dependency Rule",
        "Separation of Concerns", "SOLID Principles", "Domain-Driven Design", "Persistence Ignorance"
    },
    "orange": {
        "Entities", "Repositories", "Adapters", "Controllers", "Use Cases", "Ports"
    },
    "purple": {
        "Testability", "Mocking", "Decoupling", "Flexibility",
        "Refactorability", "Evolvability", "Maintainability"
    },
    "gray": {
        "Spring Boot", "ArchUnit",  "Framework Agnostic"
    }
}

# Color function for word groups
def grouped_color_func(word, **kwargs):
    if word in color_map["blue"]:
        return "rgb(31, 119, 180)"
    elif word in color_map["green"]:
        return "rgb(44, 160, 44)"
    elif word in color_map["orange"]:
        return "rgb(255, 127, 14)"
    elif word in color_map["purple"]:
        return "rgb(148, 103, 189)"
    elif word in color_map["gray"]:
        return "rgb(120, 120, 120)"
    else:
        return "black"

# Generate word cloud
wc = WordCloud(
    width=1920,
    height=1280,
    background_color="white",
    mask=mask,
    prefer_horizontal=0.1,
    contour_color="gray",
    contour_width=2
).generate_from_frequencies(tags)

# Apply custom colors
wc.recolor(color_func=grouped_color_func)

# Display
plt.figure(figsize=(12, 12))
plt.imshow(wc, interpolation='bilinear')
plt.axis("off")
plt.tight_layout()
plt.show()
