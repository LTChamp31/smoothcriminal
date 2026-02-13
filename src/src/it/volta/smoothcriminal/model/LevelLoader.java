package it.volta.smoothcriminal.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private int righe, colonne, inizioX, inizioY, uscitaX, uscitaY, nTrappole=0;
    private List<List<Character>> mat = new ArrayList<>();
    private List<Integer> xyTrappole = new ArrayList<>();

    public LevelLoader() {

    }

    public Labirinto loadLevel(int l) {
        String livello = "src/resources/levels/livello" + l + ".txt";

        mat.clear();
        int currentcol = 0;
        mat.add(new ArrayList<>());
        try (BufferedReader reader = new BufferedReader(new FileReader(livello))) {
            int charValue;
            righe = 0;
            while ((charValue = reader.read()) != -1) {
                char ch = (char) charValue;
                if (ch == 'U') {
                    uscitaY = righe;
                    uscitaX = currentcol;
                } else if (ch == 'S') {
                    ch = ' ';
                    xyTrappole.add(righe);
                    xyTrappole.add(currentcol);
                } else if (ch == 'T') {
                    ch = ' ';
                    xyTrappole.add(righe+100);
                    xyTrappole.add(currentcol+100);
                }
                if (ch == '\n') {
                    righe++;
                    mat.add(new ArrayList<>());
                    currentcol = 0;
                } else if (ch != '\r') { // Ignore Windows carriage returns
                    mat.get(righe).add(ch);
                    currentcol++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
        this.righe = mat.size();
        this.colonne = 0;
        for(List<Character> row : mat) {
            this.colonne = Math.max(this.colonne, row.size());
        }
        inizioX = 0;
        inizioY = 0;

        return new Labirinto(mat, inizioX, inizioY, colonne, uscitaX, uscitaY, xyTrappole);
    }
}
