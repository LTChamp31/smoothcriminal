package it.volta.smoothcriminal.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    int righe, colonne, inizioX, inizioY;
    List<List<Character>> mat = new ArrayList<>();

    public LevelLoader() {

    }

    public Labirinto loadLevel(int l) {
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
            return null;
        }
        this.righe = mat.size();
        this.colonne = 0;
        for(List<Character> row : mat) {
            this.colonne = Math.max(this.colonne, row.size());
        }
        inizioX = 0;
        inizioY = 0;

        return new Labirinto(mat, inizioX, inizioY, colonne);
    }
}
