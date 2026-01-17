Introduction-
This project implements a Pac-Man algorithm that navigates a 2D maze to collect all pink dots while avoiding ghosts and walls.
When Pac-Man eats a green dot (Power Pellet), ghosts become eatable for a short time, and Pac-Man will attempt to chase them.
The main goal of the algorithm is to decide at each step which direction to move (Up, Down, Left, Right) to maximize survival and progress toward eating all pink dots.

Overview of the Algorithm-
The main class is `Ex3Algo` which implements `PacManAlgo`.  
Main Components:
1. Move Counter and State Tracking
- `_count` – counts how many steps Pac-Man has moved.
- `_lastDir` – stores the last direction chosen.
- `_sameDirStreak` – counts how many times the same direction has been repeated consecutively.

Visit Tracking:
- `_visit[x][y]` – counts how many times Pac-Man has visited each cell.
- `_vw` and `_vh` – store the width and height of the board, used to reinitialize `_visit` when the level changes.

Functions and Their Behavior
`move(PacmanGame game)` --

- Main function called at each game step.
- Responsibilities:
  1. Get the current game board via `game.getGame(code)`.
  2. Parse Pac-Man's current position.
  3. Update visit count for the current cell.
  4. Get ghosts and their states (eatable or dangerous).
  5. Decide the best direction to move using `chooseDir`.
  6. Update direction streaks and return the chosen direction.

  `chooseDir(...)` --
  - Calculates the best direction for Pac-Man to move at the current step.
  - Steps:
  1. Consider all four directions: UP, DOWN, LEFT, RIGHT.
  2. For each direction:
     - Compute the next cell coordinates using `step`.
     - Skip moves into walls or outside the board.
     - Compute BFS distances from the next cell to all food and ghosts using `bfsDistances`.
     - Calculate the distance to the nearest pink dot (`nearestFoodDist`) and nearest dangerous ghost (`nearestGhostDist`).
     - Compute a **score** for the move based on:
       - Immediate food: high score.
       - Nearby food: score decreases with distance.
       - Ghost proximity: large penalty for nearby dangerous ghosts.
       - Visit penalty: discourage revisiting cells.
       - Direction streak penalty: discourage repeating the same direction too long.
  3. Choose the direction with the highest score.
  4. If no valid direction exists, pick a random direction.

  `step(int x, int y, int dir, int w, int h, boolean cyclic)` --
  - Computes the new cell coordinates after moving in a given direction.
  - Handles cyclic maps by wrapping coordinates.
  - Returns `null` if the move is outside the map boundaries.
 
    `bfsDistances(...)` --
    - Performs Breadth-First Search from a starting cell.
    - Computes the minimum steps to reach all cells, ignoring walls.
    - Supports cyclic maps.
    - Used to determine distances to food and ghosts for scoring.

    
     `nearestFoodDist(...)` --
    - Finds the nearest pink dot from a given cell using BFS distances.
    - Returns -1 if no food is reachable.
   
     `nearestGhostDist(...)` --
    - Finds the nearest dangerous (non-eatable) ghost using BFS distances.
    - Returns -1 if no dangerous ghost is reachable.
   
     `randomDir()` --
    - Returns a random direction (Up, Down, Left, Right) when no safe moves are available.
   
     `parsePosToXY(String pos, int w, int h)` --
     - Converts a position string from the game into x and y coordinates.
     - Returns `null` if the position is invalid.

     














