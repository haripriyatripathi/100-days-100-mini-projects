import turtle, math, random, time

# -- Setup --
screen = turtle.Screen()
screen.bgcolor("black")
screen.title("✨ Solar System with Glitter & Rocket Puppy ✨")
screen.setup(width=1000, height=800)
screen.tracer(0)

# --Sun--
sun = turtle.Turtle()
sun.shape("circle")
sun.color("gold")
sun.shapesize(3)
sun.penup()

# --- Planet data: [name, color, distance, size, speed] ---
planets_data = [
    ("Mercury", "#9e9e9e", 40, 0.2, 0.05),
    ("Venus", "#c2b280", 70, 0.4, 0.035),
    ("Earth", "#4a90e2", 100, 0.5, 0.025),
    ("Mars", "#b7410e", 140, 0.4, 0.020),
    ("Jupiter", "#d2b48c", 200, 1.2, 0.010),
    ("Saturn", "#deb887", 250, 1.0, 0.008),
    ("Uranus", "#708090", 300, 0.9, 0.006),
    ("Neptune", "#483d8b", 350, 0.9, 0.004)
]

# --- Glitter stars (background twinkling) ---
glitter = []
for _ in range(150):
    star = turtle.Turtle()
    star.hideturtle()
    star.speed(0)
    star.shape("circle")
    star.color(random.choice(["#9370db", "#778899", "#5f9ea0"]))  # purple, grey, soft blue
    star.shapesize(random.uniform(0.05, 0.15))
    star.penup()
    star.goto(random.randint(-480, 480), random.randint(-380, 380))
    glitter.append(star)

for g in glitter:
    g.showturtle()

# --- Orbits ---
orbit_drawer = turtle.Turtle()
orbit_drawer.hideturtle()
orbit_drawer.speed(0)
orbit_drawer.color("gray25")
for _, _, dist, _, _ in planets_data:
    orbit_drawer.penup()
    orbit_drawer.goto(0, -dist)
    orbit_drawer.pendown()
    orbit_drawer.circle(dist)

# --- Planets ---
planets = []
labels = []
for name, color, dist, size, speed in planets_data:
    t = turtle.Turtle()
    t.shape("circle")
    t.color(color)
    t.shapesize(size)
    t.penup()
    planets.append([t, dist, speed, 0, name])  # [turtle, dist, speed, angle, name]

    label = turtle.Turtle()
    label.hideturtle()
    label.color("white")
    label.penup()
    labels.append(label)

# --- Asteroids ---
asteroids = []
for _ in range(6):
    x = random.randint(-500, 500)
    y = random.randint(250, 400)
    ast = turtle.Turtle()
    ast.shape("circle")
    ast.color(random.choice(["#9370db", "#778899", "#b0c4de"]))
    ast.shapesize(0.12)
    ast.penup()
    ast.goto(x, y)
    ast.speed(0)
    asteroids.append([ast, random.uniform(-2.5, -1), random.uniform(-2, -0.5)])

# --- Cute Rocket (body) ---
rocket_body = turtle.Turtle()
rocket_body.shape("triangle")
rocket_body.color("#ff6f91")  # pastel pink
rocket_body.shapesize(2, 5)
rocket_body.penup()
rocket_body.setheading(90)
rocket_body.goto(-400, -250)

# Rocket window
window = turtle.Turtle()
window.shape("circle")
window.color("white")
window.shapesize(1.2)
window.penup()
window.goto(rocket_body.xcor(), rocket_body.ycor() + 25)

# Puppy astronaut (emoji text)
puppy = turtle.Turtle()
puppy.hideturtle()
puppy.color("white")
puppy.penup()

# Flame
flame = turtle.Turtle()
flame.shape("triangle")
flame.color("orange")
flame.shapesize(0.7, 1.5)
flame.penup()
flame.setheading(-90)

# --- MAIN LOOP ---
while True:
    # --- Planets orbiting ---
    for i, p in enumerate(planets):
        t, dist, speed, angle, name = p
        angle += speed
        x = dist * math.cos(angle)
        y = dist * math.sin(angle)
        t.goto(x, y)

        labels[i].goto(x + 12, y + 12)
        labels[i].clear()
        labels[i].write(f"{name} (spd {speed:.3f})", font=("Arial", 8, "normal"))

        p[3] = angle  # update angle

    # --- Asteroids drifting ---
    for ast in asteroids:
        t, dx, dy = ast
        x, y = t.xcor(), t.ycor()
        t.goto(x + dx, y + dy)
        if x < -520 or y < -420:
            t.goto(random.randint(300, 480), random.randint(250, 400))
            ast[1] = random.uniform(-2.5, -1)
            ast[2] = random.uniform(-2, -0.5)

    # --- Twinkling glitter stars ---
    for g in glitter:
        if random.random() < 0.02:
            g.shapesize(random.uniform(0.05, 0.15))

    # --- Rocket movement ---
    rx, ry = rocket_body.xcor(), rocket_body.ycor()
    rocket_body.goto(rx + 1.2, ry + 0.4)
    window.goto(rx, ry + 25)
    flame.goto(rx, ry - 40)

    # Puppy astronaut peeking & waving
    puppy.goto(rx - 10, ry + 15)
    puppy.clear()
    puppy.write("🐶👋", font=("Arial", 18, "bold"))

    # Reset rocket when off screen
    if rx > 500 or ry > 400:
        rocket_body.goto(-480, -300)

    # --- Update frame ---
    screen.update()
    time.sleep(0.03)
