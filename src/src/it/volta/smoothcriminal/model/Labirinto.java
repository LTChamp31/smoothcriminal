package it.volta.smoothcriminal.model;

public class Labirinto {
    char mat[][];
    int righe, colonne;
    public String labirinto = "   ████████████████████████████\n" +
            "                              █\n" +
            "█  ██████████  ██████████  ████\n" +
            "█           █  █     █        █\n" +
            "████  █████████████  ████  ████\n" +
            "█  █              █  █        █\n" +
            "█  █  █  █  █  ████  ████  ████\n" +
            "█     █  █  █  █  █  █     █  █\n" +
            "███████  ███████  █  ████  █  █\n" +
            "█              █        █  █  █\n" +
            "████  █  ███████  █  ████  █  █\n" +
            "█  █  █        █  █     █     █\n" +
            "█  ███████  █  ███████  █  █  █\n" +
            "█  █  █     █  █        █  █  █\n" +
            "█  █  ████  ████  █  █  █  ████\n" +
            "█  █  █        █  █  █     █  █\n" +
            "█  █  █  █  ███████  █  █  █  █\n" +
            "█        █  █        █  █     █\n" +
            "█  ████████████████  █  █  ████\n" +
            "█           █        █  █     \n" +
            "████████████████████████████  U";


    public Labirinto() {
        String[] rows = labirinto.split("\n");
        righe = rows.length;
        colonne = rows[0].length();
        mat = new char[righe][colonne];

        for(int i = 0; i < righe; i++) {
            for(int j = 0; j < rows[i].length(); j++) {
                mat[i][j] = rows[i].charAt(j);
            }
        }
    }

    public int getCellula(int x, int y) {
        return 0;
    }

    public boolean isMuro(int x, int y) {
        if (mat[x][y] == '█') return true;
        return false;
    }

    public boolean isUscita(int x, int y) {
        if (mat[x][y] == 'U') return true;
        return false;
    }


}
