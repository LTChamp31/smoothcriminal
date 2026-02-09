package it.volta.smoothcriminal.model;
import java.util.List;

public class Labirinto {
    private final int inizioX;
    private final int inizioY, colonne;
    private final List<List<Character>> mat;

    public Labirinto(List<List<Character>> mat, int inizioX, int inizioY, int colonne) {
        this.mat = mat;
        this.inizioX = inizioX;
        this.inizioY = inizioY;
        this.colonne = colonne;
    }


    public boolean isMuro(int x, int y) {
        if (y < 0 || y >= mat.size() || x < 0 || x >= mat.get(y).size()) {
            return true;
        }
        return mat.get(y).get(x) == '█';
    }

    public boolean isUscita(int x, int y) {
        if (mat.get(y).get(x) == 'U') return true;
        return false;
    }

    public String mappa(int x, int y) {
        String map = "";
        for (int i=0; i< mat.size(); i++) {
            List<Character> row = mat.get(i); // Get the current row
            for (int j=0; j< colonne; j++) {
                if (i==y && j==x) map += "ℜ";
                else if (j < row.size()) {
                    map += row.get(j);
                }
                else {
                    // If the row is shorter than the maximum width, add a space
                    map += " ";
                }
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
