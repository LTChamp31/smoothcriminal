package it.volta.smoothcriminal.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelLoader {
    private int righe, colonne, inizioX=0, inizioY=0, uscitaX, uscitaY, nTrappole=0;
    private List<List<Character>> mat = new ArrayList<>();
    List<Coordinate>[] xyTrappole = new ArrayList[3];
    private int[][] xyGadgets = new int[5][2];
    int altezza = 17 + 2 * (int)(Math.random() * 6);
    int larghezza = 17 + 2 * (int)(Math.random() * 6);
    GeneraLabirinto generaLabirinto = new GeneraLabirinto(altezza, larghezza);


    public LevelLoader() {
        for (int i=0; i<3; i++) {
            xyTrappole[i] = new ArrayList<>();
        }
    }

    public Labirinto loadLevel() {
        mat.clear();
        mat = generaLabirinto.generateLabirinto();
        colonne = generaLabirinto.getLarghezza();
        righe = generaLabirinto.getAltezza();
        uscitaX = colonne-1;
        uscitaY = righe-2;
        inizioX = 1;
        inizioY = 1;
        return new Labirinto(mat, inizioX, inizioY, colonne, uscitaX, uscitaY, xyTrappole, xyGadgets);
    }

    public Labirinto loadTorneo(int[] indiceScelto) {

        List<String> tutteLeMappe = new ArrayList<>();

        // 1. Leggiamo tutte le mappe separate da ---
        try (BufferedReader br = new BufferedReader(
                new FileReader("src/resources/levels/livelliTorneo.txt"))) {

            StringBuilder mappaCorrente = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.equals("---")) {
                    tutteLeMappe.add(mappaCorrente.toString());
                    mappaCorrente.setLength(0);
                } else {
                    mappaCorrente.append(linea).append("\n");
                }
            }

            if (mappaCorrente.length() > 0)
                tutteLeMappe.add(mappaCorrente.toString());

        } catch (IOException e) {
            System.out.println("Errore caricamento mappe torneo: " + e.getMessage());
        }

        // 2. Scelta random
        Random rand = new Random();
        int indiceMappa = rand.nextInt(tutteLeMappe.size());
        indiceScelto[0] = indiceMappa;

        String mappaAscii = tutteLeMappe.get(indiceMappa);

        // 3. Parsing mappa
        mat.clear();
        for (int i = 0; i < 3; i++)
            xyTrappole[i].clear();

        int currentCol = 0;
        righe = 0;

        mat.add(new ArrayList<>());

        for (int i = 0; i < mappaAscii.length(); i++) {

            char ch = mappaAscii.charAt(i);

            switch (ch) {
                case 'I':
                    ch = ' ';
                    inizioY = righe;
                    inizioX = currentCol;
                    break;

                case 'U':
                    uscitaY = righe;
                    uscitaX = currentCol;
                    break;

                case 'S':
                    ch = ' ';
                    xyTrappole[0].add(new Coordinate(currentCol, righe));
                    break;

                case 'T':
                    ch = ' ';
                    xyTrappole[1].add(new Coordinate(currentCol, righe));
                    break;
            }

            if (ch == '\n') {
                righe++;
                mat.add(new ArrayList<>());
                currentCol = 0;
            }
            else if (ch != '\r') {
                mat.get(righe).add(ch);
                currentCol++;
            }
        }

        colonne = 0;
        for (List<Character> row : mat)
            colonne = Math.max(colonne, row.size());

        return new Labirinto(
                mat,
                inizioX,
                inizioY,
                colonne,
                uscitaX,
                uscitaY,
                xyTrappole,
                xyGadgets
        );
    }


    public Labirinto loadLevel(int l) {
        String livello = "src/resources/levels/livello" + l + ".txt";

        //Cancellare vecchie trappole e matrice
        for (int i=0; i<3; i++) {
            xyTrappole[i].clear();
        }
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
                        xyTrappole[0].add(new Coordinate(currentcol, righe));

                        break;
                    case 'T':
                        ch = ' ';
                        xyTrappole[1].add(new Coordinate(currentcol, righe));
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

    public Labirinto loadStoria(int[] indiceScelto) {

        List<String> tutteLeMappe = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/levels/livelliStoria.txt"))) {

            StringBuilder mappaCorrente = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.equals("---")) {
                    tutteLeMappe.add(mappaCorrente.toString());
                    mappaCorrente.setLength(0);
                } else {
                    mappaCorrente.append(linea).append("\n");
                }
            }

            if (mappaCorrente.length() > 0)
                tutteLeMappe.add(mappaCorrente.toString());

        } catch (IOException e) {
            System.out.println("Errore caricamento mappe storia");
        }

        int indiceMappa;
        if (indiceScelto[0] >= 0 && indiceScelto[0] < tutteLeMappe.size()) {
            indiceMappa = indiceScelto[0];
        } else {
            indiceMappa = 0;
        }
        indiceScelto[0] = indiceMappa;

        String mappaAscii = tutteLeMappe.get(indiceMappa);

        mat.clear();
        for (int i = 0; i < 3; i++)
            xyTrappole[i].clear();

        int currentCol = 0;
        righe = 0;
        mat.add(new ArrayList<>());

        for (int i = 0; i < mappaAscii.length(); i++) {
            char ch = mappaAscii.charAt(i);

            switch (ch) {
                case 'I':
                    ch = ' ';
                    inizioY = righe;
                    inizioX = currentCol;
                    break;
                case 'U':
                    uscitaY = righe;
                    uscitaX = currentCol;
                    break;
                case 'S':
                    ch = ' ';
                    xyTrappole[0].add(new Coordinate(currentCol, righe));
                    break;
                case 'T':
                    ch = ' ';
                    xyTrappole[1].add(new Coordinate(currentCol, righe));
                    break;
            }

            if (ch == '\n') {
                righe++;
                mat.add(new ArrayList<>());
                currentCol = 0;
            } else if (ch != '\r') {
                mat.get(righe).add(ch);
                currentCol++;
            }
        }

        colonne = 0;
        for (List<Character> row : mat)
            colonne = Math.max(colonne, row.size());

        return new Labirinto(
                mat,
                inizioX,
                inizioY,
                colonne,
                uscitaX,
                uscitaY,
                xyTrappole,
                xyGadgets
        );
    }


}



