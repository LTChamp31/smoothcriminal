package it.volta.smoothcriminal.model;

public class Labirinto {
    char mat[][];
    String[] rows;
    int righe, colonne, inizioX, inizioY;
    public String labirinto =   "   ████████████████████████████\n" +
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
        rows = labirinto.split("\n");
        righe = rows.length;
        colonne = 0;
        for (int i = 0; i < righe; i++) {
            colonne = Math.max(colonne, rows[i].length());
        }
        mat = new char[righe][colonne];

        for(int i = 0; i < righe; i++) {
            for(int j = 0; j < colonne; j++) {
                if (j < rows[i].length()) {
                    mat[i][j] = rows[i].charAt(j);
                } else {
                    mat[i][j] = '█';
                }
            }
        }

        inizioX = 0;
        inizioY = 0;
    }


    public boolean isMuro(int x, int y) {
        if (mat[y][x] == '█') return true;
        return false;
    }

    public boolean isUscita(int x, int y) {
        if (mat[y][x] == 'U') return true;
        return false;
    }

    public String mappa(int x, int y) {
        String map = "";
        for (int i=0; i< righe; i++) {
            for (int j=0; j< rows[i].length(); j++) {
                if (i==y && j==x) map += "R";
                else map += mat[i][j];
            }
            map += "\n";
        }
        return map;
    }

    public int getInizioX() {
        return inizioX;
    }

    public int getInizioY() {
        return inizioY;
    }


}
