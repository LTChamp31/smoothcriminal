package it.volta.smoothcriminal.model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Labirinto {
    List<List<Character>> mat = new ArrayList<>();
    int righe, colonne, inizioX, inizioY;

    public Labirinto() {
        loadLevel(1);
    }

    public void loadLevel(int l) {
        String livello = "src/resources/levels/livello" + l + ".txt";

        mat.clear();

        mat.add(new ArrayList<>());
        try (BufferedReader reader = new BufferedReader(new FileReader(livello))) {
            int charValue;
            righe = 0;
            while ((charValue = reader.read()) != -1) {
                char ch = (char) charValue;

                if (ch == '\n') {
                    righe++;
                    mat.add(new ArrayList<>());
                } else if (ch != '\r') { // Ignore Windows carriage returns
                    mat.get(righe).add(ch);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        this.righe = mat.size();
        this.colonne = 0;
        for(List<Character> row : mat) {
            this.colonne = Math.max(this.colonne, row.size());
        }
        inizioX = 0;
        inizioY = 0;
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
