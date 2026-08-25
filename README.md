# Pac-Man (Java Swing)

A simple Pac-Man clone built with Java and Swing (`JPanel`, `JFrame`, `Timer`). The game renders a tile-based maze, animates Pac-Man and four ghosts, and tracks score and lives.

## Features

- Tile-based maze loaded from a string array (`tileMap`)
- Pac-Man movement with directional sprites (up/down/left/right)
- Wall collision detection (AABB / bounding box overlap)
- Ghosts with basic random-direction that avoids walls
- Tunnel wrap-around on the left/right edges of the map
- Food (dots) that Pac-Man eats for points
- Score and lives tracking, with a "Game Over" state

## Requirements

- Java JDK 8 or later
- The following image assets in the same package/folder as `PacMan.java`:
  - `wall.png`
  - `blueGhost.png`, `orangeGhost.png`, `pinkGhost.png`, `redGhost.png`
  - `pacmanUp.png`, `pacmanDown.png`, `pacmanleft.png`, `pacmanRight.png`

> Image loading uses `getClass().getResource("./filename.png")`, so these files must be on the classpath relative to the compiled `PacMan.class`.

## Project Structure

```
.
├── App.java       # Entry point — creates the JFrame and starts the game
├── PacMan.java     # Game panel — map, rendering, movement, collisions, game loop
└── assets/         # wall.png, ghost sprites, pacman sprites (place alongside classes)
```

## Running the Game

Compile and run from the directory containing the source files and image assets:

```bash
javac App.java PacMan.java
java App
```

A window titled **"Pac Man"** will open, sized to fit the maze grid.

## Controls

| Key         | Action       |
|-------------|--------------|
| Arrow Up    | Move up      |
| Arrow Down  | Move down    |
| Arrow Left  | Move left    |
| Arrow Right | Move right   |

Direction changes are applied on **key release** and only take effect if the new direction isn't immediately blocked by a wall.

## How It Works

- **Map**: `tileMap` is a `String[]` where each character represents a tile — `X` for walls, `O` for open tunnel space, `P` for Pac-Man's start, a space (` `) for food, and `b`/`o`/`p`/`r` for the blue/orange/pink/red ghosts' starting positions.
- **Blocks**: every entity (walls, food, ghosts, Pac-Man) is represented by an inner `Block` class holding position, size, image, direction, and velocity.
- **Game loop**: a `javax.swing.Timer` fires every 50ms (~20 FPS), calling `move()` and `repaint()` each tick.
- **Collision**: `collision(Block a, Block b)` uses standard axis-aligned bounding box (AABB) overlap detection to check if two blocks intersect.
- **Ghost AI**: ghosts move in a straight line until they hit a wall or a map edge, then pick a new random direction (retrying if the random choice is also blocked).

## Known Limitations / Possible Improvements

- Ghost AI is fully random — no pathfinding or chase behavior toward Pac-Man.
- No pause/restart controls once the game ends.
- Movement direction updates on key release rather than key press, which can feel slightly less responsive than classic Pac-Man controls.

## License

This project is for educational/personal use.
