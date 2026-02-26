# smoothcriminal
[![Ask DeepWiki](https://devin.ai/assets/askdeepwiki.png)](https://deepwiki.com/LTChamp31/smoothcriminal)

> YOU'VE BEEN HIT BY, YOU'VE BEEN HIT BY... A SMOOTH CRIMINAL

`smoothcriminal` is a classic, top-down maze-runner game built in Java and played entirely in the console. As the criminal, your goal is to navigate complex mazes, evade a relentless enemy, use a variety of gadgets to your advantage, and reach the exit. The game features multiple modes, procedurally generated levels for endless replayability, and persistent progress tracking.

## Features

*   **Three Game Modes:** Choose between a progressive Story mode, an endless Training mode, and a competitive Tournament mode.
*   **Dynamic Mazes:** Play on hand-crafted levels in Story and Tournament modes, or test your skills in procedurally generated mazes in Training mode.
*   **Interactive Gameplay:** Collect and use a variety of gadgets to overcome obstacles. Watch out for traps that can teleport you or move the exit!
*   **Challenging AI:** Evade a persistent enemy (`Ⓝ`) that actively hunts you down through the maze.
*   **Persistent Data:** Your progress in Story mode is automatically saved, and your best times in Tournament mode are recorded.

## How to Play

The objective is simple: guide your character (`Ⓒ`) from the starting point (`I`) to the exit (`U`).

### Controls

*   **Movement:** `W` (Up), `A` (Left), `S` (Down), `D` (Right)
*   **Use Gadget:** `1`, `2`, `3`, `4`, `5` (After collecting a gadget, its corresponding number will be displayed).
*   **Exit Game:** `X`

### Map Legend

| Symbol | Description                  |
| :----- | :--------------------------- |
| `Ⓒ`    | You (The Criminal)           |
| `█`    | Impassable Wall              |
| `I`    | Starting Position            |
| `U`    | Exit                         |
| `Ⓝ`    | Enemy (Avoid it!)            |
| `S`, `T` | Traps (Move Exit, Teleport)  |
| `Ⓖ`, `Ⓙ`, `Ⓓ`, `Ⓑ`, `Ⓨ` | Collectible Gadgets        |

## Game Modes

*   **Story:** Progress through a series of increasingly difficult, pre-designed levels. Your progress is saved as you complete each level.
*   **Training:** Hone your skills on a new, randomly generated maze every time you play.
*   **Tournament:** Compete against the clock on a randomly selected map from the tournament pool. Your best time for each map is saved.

## Setup & Running the Game

1.  **Prerequisites:** Make sure you have a Java Development Kit (JDK) installed on your system.

2.  **Clone the repository:**
    ```sh
    git clone https://github.com/ltchamp31/smoothcriminal.git
    cd smoothcriminal
    ```

3.  **Compile the source files:**
    ```sh
    # Create a directory for the compiled classes
    mkdir bin

    # Compile all Java files into the bin directory
    javac -d bin -cp src/src src/src/it/volta/smoothcriminal/**/*.java
    ```

4.  **Run the game:**
    ```sh
    java -cp bin it.volta.smoothcriminal.Main
    ```
    You will be greeted with the main menu in your console. Enjoy