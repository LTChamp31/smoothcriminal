package it.volta.smoothcriminal.gui;

import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.Enemy;
import it.volta.smoothcriminal.model.Maze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameRenderer {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final int CELL_SIZE = 30;

    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void render(Maze maze, Criminal criminal) {
        int righe = maze.getRighe();
        int colonne = maze.getColonne();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw maze
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {
                char c = maze.getCarattere(x, y);
                if (c == '█') {
                    gc.setFill(Color.DARKSLATEGRAY);
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (c == 'U') {
                    gc.setFill(Color.GOLD);
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (c == 'T') {
                    gc.setFill(Color.PURPLE);
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                    gc.setFill(Color.web("#e0e0e0")); // Walkable path
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
                
                // Optional: Draw cell borders
                gc.setStroke(Color.web("#d0d0d0"));
                gc.strokeRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // Draw Enemies
        gc.setFill(Color.RED);
        if (maze.getNemici() != null) {
            for (Enemy e : maze.getNemici()) {
                gc.fillOval(e.getX() * CELL_SIZE + 4, e.getY() * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);
            }
        }

        // Draw Criminal
        gc.setFill(Color.BLUE);
        gc.fillOval(criminal.getX() * CELL_SIZE + 2, criminal.getY() * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
    }
}
