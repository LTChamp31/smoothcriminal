package it.volta.smoothcriminal.model;

public class Labirinto {
    String rawMaze =
            "████████████████████████████\n" +
                    "   █  █  █                 █  █\n" +
                    "█  █  █  █  ████  █  ███████  █\n" +
                    "█           █  █  █           █\n" +
                    "███████  ████  █  █  █  ████  █\n" +
                    "█           █     █  █         \n" +
                    " █  █\n" +
                    "████  █  ████  ████  ██████████\n" +
                    "█     █  █     █  █     █     █\n" +
                    "████  ███████  █  ██████████  █\n" +
                    "█     █     █        █        █\n" +
                    "█  ███████  █  ███████  ████  █\n" +
                    "█  █        █           \n" +
                    "   █  █\n" +
                    "████  █  ███████  ████  ███████\n" +
                    "█  █  █  █        █           █\n" +
                    "█  ████  ████████████████  ████\n" +
                    "█  █     █                    █\n" +
                    "█  ████  ████  █  ███████  ████\n" +
                    "█  █     █     █     █  \n" +
                    "      █\n" +
                    "█  ████  █  ███████  ████  ████\n" +
                    "█              █     █        \n" +
                    "████████████████████████████";

    char[][] mazeMatrix = convertToCharMatrix(rawMaze);

    for (int i=0; i<mazeMatrix.length(); i++) {
        for (int j=0; j<mazeMatrix[0].length(); j++) {
            System.out.print(mazeMatrix[i][j]);
        }
        System.out.println();
    }

    public static char[][] convertToCharMatrix(String mazeStr) {
        String[] lines = mazeStr.split("\n");
        int rows = lines.length;
        int cols = 0;

        // Determine max width for the matrix [cite: 1, 2, 3, 4]
        for (String line : lines) {
            cols = Math.max(cols, line.length());
        }

        char[][] matrix = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            String currentLine = lines[i];
            for (int j = 0; j < cols; j++) {
                if (j < currentLine.length()) {
                    // Use the original char or swap '█' for '#' for easier typing
                    char c = currentLine.charAt(j);
                    matrix[i][j] = (c == '█') ? '#' : '.';
                } else {
                    // Fill empty space if lines are uneven
                    matrix[i][j] = '.';
                }
            }
        }
        return matrix;
    }
}
