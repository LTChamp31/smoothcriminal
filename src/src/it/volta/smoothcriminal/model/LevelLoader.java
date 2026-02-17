package it.volta.smoothcriminal.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {
    private int righe, colonne, inizioX=0, inizioY=0, uscitaX, uscitaY, nTrappole=0;
    private List<List<Character>> mat = new ArrayList<>();
    private List<Integer>[] xyTrappole = new ArrayList[3];
    private int[][] xyGadgets = new int[5][2];
    GernerateLabirinto gernerateLabirinto = new GernerateLabirinto(10,10);


    public LevelLoader() {
        for (int i=0; i<3; i++) {
            xyTrappole[i] = new ArrayList<>();
        }
    }

    public Labirinto loadlevel() {
        char matr[][] = gernerateLabirinto.generateLabirinto();
        colonne = 10;
        righe = 10;
        uscitaX = 9;
        uscitaY = 8;
        return new Labirinto(matr, inizioX, inizioY, colonne, uscitaX, uscitaY);
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

                switch (ch) {
                    case 'I':
                        ch = ' ';
                        inizioY = righe;
                        inizioX = currentcol;
                        break;
                    case 'U':
                        uscitaY = righe;
                        uscitaX = currentcol;
                        break;
                    //Inizio Trappole
                    case 'S':
                        ch = ' ';
                        xyTrappole[0].add(righe);
                        xyTrappole[0].add(currentcol);
                        break;
                    case 'T':
                        ch = ' ';
                        xyTrappole[1].add(righe);
                        xyTrappole[1].add(currentcol);
                        break;
                    //Inizio Gadget
                    case 'G':
                        xyGadgets[0][0] = righe;
                        xyGadgets[0][1] = currentcol;
                        break;
                    case 'J':
                        xyGadgets[1][0] = righe;
                        xyGadgets[1][1] = currentcol;
                        break;
                    case 'D':
                        xyGadgets[2][0] = righe;
                        xyGadgets[2][1] = currentcol;
                        break;
                    case 'B':
                        xyGadgets[3][0] = righe;
                        xyGadgets[3][1] = currentcol;
                        break;
                    case 'Y':
                        xyGadgets[4][0] = righe;
                        xyGadgets[4][1] = currentcol;
                        break;
                }
                if (ch == '\n') {
                    righe++;
                    mat.add(new ArrayList<>());
                    currentcol = 0;
                }
                else if (ch != '\r') { // Ignore Windows carriage returns
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

        return new Labirinto(mat, inizioX, inizioY, colonne, uscitaX, uscitaY, xyTrappole, xyGadgets);
    }
}



