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
    private List<Coordinate>[] xyTrappole = new ArrayList[3];
    private int[][] xyGadget = new int[5][2];
    private List<Nemici> nemici = new ArrayList<>();
    private int altezza = 17 + 2 * (int)(Math.random() * 6);
    private int larghezza = 17 + 2 * (int)(Math.random() * 6);
    private GeneraLabirinto generaLabirinto = new GeneraLabirinto(altezza, larghezza);

    public LevelLoader() {
        for (int i=0; i<3; i++) {
            xyTrappole[i] = new ArrayList<>();
        }
    }

    public Labirinto loadLevel() {
        mat.clear();
        mat = generaLabirinto.creaLabirinto();
        colonne = generaLabirinto.getLarghezza();
        righe = generaLabirinto.getAltezza();
        uscitaX = colonne-1;
        uscitaY = righe-2;
        inizioX = 1;
        inizioY = 1;
        return new Labirinto(mat, inizioX, inizioY, colonne, uscitaX, uscitaY, xyTrappole, xyGadget, nemici);
    }



    public Labirinto loadLevel(int[] indiceScelto, char tipo) {
        List<String> tutteLeMappe = new ArrayList<>();
        int indiceMappa;
        if (tipo == 'S') {
            tutteLeMappe = leggiFile("src/resources/levels/livelliStoria.txt");
            if (indiceScelto[0] >= 0 && indiceScelto[0] < tutteLeMappe.size()) {
                indiceMappa = indiceScelto[0];
            } else {
                indiceMappa = 0;
            }
            indiceScelto[0] = indiceMappa;
        } else {
            tutteLeMappe = leggiFile("src/resources/levels/livelliTorneo.txt");
            Random rand = new Random();
            indiceMappa = rand.nextInt(tutteLeMappe.size());
            indiceScelto[0] = indiceMappa;
        }

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
                //Inizio Gadget
                case 'Ⓖ':
                    xyGadget[0][0] = righe;
                    xyGadget[0][1] = currentCol;
                    break;
                case 'Ⓙ':
                    xyGadget[1][0] = righe;
                    xyGadget[1][1] = currentCol;
                    break;
                case 'Ⓓ':
                    xyGadget[2][0] = righe;
                    xyGadget[2][1] = currentCol;
                    break;
                case 'Ⓑ':
                    xyGadget[3][0] = righe;
                    xyGadget[3][1] = currentCol;
                    break;
                case 'Ⓨ':
                    xyGadget[4][0] = righe;
                    xyGadget[4][1] = currentCol;
                    break;
                case 'Ⓝ':
                    Nemici nemico = new Nemici(currentCol, righe);
                    nemici.add(nemico);
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
                xyGadget,
                nemici
        );
    }

    public List<String> leggiFile(String indirizzo) {
        List<String> tutteLeMappe = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(indirizzo))) {

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
            System.out.println("Errore caricamento mappe torneo");
        }
        return tutteLeMappe;
    }

        /*ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ*/
}



