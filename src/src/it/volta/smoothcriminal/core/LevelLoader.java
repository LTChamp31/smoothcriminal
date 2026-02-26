package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La classe {@code LevelLoader} è responsabile del caricamento e della configurazione dei livelli di gioco.
 * Supporta due modalità di caricamento:
 * 1 - Utilizza {@link GenerateMaze} per creare una mappa casuale (usata in Allenamento).
 * 2 - Da File: Legge mappe in formato ASCII da file di testo, interpretando i simboli per posizionare
 * giocatore, uscita, gadget, trappole e nemici (usata in Storia e Torneo).
 *
 * * @author Marco Caria & Lotan Teny
 */
public class LevelLoader {
    private int righe, colonne, inizioX=0, inizioY=0, uscitaX, uscitaY;
    private List<List<Character>> mat = new ArrayList<>();
    private List<Coordinates>[] xyTrappole = new ArrayList[3];
    private int[][] xyGadget = new int[5][2];
    private List<Enemy> enemy = new ArrayList<>();

    /** Dimensioni casuali per la generazione procedurale. */
    private int altezza = 17 + 2 * (int)(Math.random() * 6);
    private int larghezza = 17 + 2 * (int)(Math.random() * 6);
    private GenerateMaze generateMaze = new GenerateMaze(altezza, larghezza);

    /**
     * Costruttore della classe. Inizializza le liste per le coordinate delle trappole.
     */
    public LevelLoader() {
        for (int i=0; i<3; i++) {
            xyTrappole[i] = new ArrayList<>();
        }
    }

    /**
     * Carica un livello generato proceduralmente.
     * Configura automaticamente le coordinate di inizio e fine in base alla generazione casuale.
     * * @return Un'istanza di {@link Maze} generata algoritmicamente.
     */
    public Maze loadLevel() {
        mat.clear();
        mat = generateMaze.creaLabirinto();
        colonne = generateMaze.getLarghezza();
        righe = generateMaze.getAltezza();
        uscitaX = colonne-1;
        uscitaY = righe-2;
        inizioX = 1;
        inizioY = 1;
        return new Maze(mat, inizioX, inizioY, colonne, uscitaX, uscitaY, xyTrappole, xyGadget, enemy);
    }

    /**
     * Carica un livello specifico partendo da un file di testo (Storia o Torneo).
     * Il metodo interpreta i seguenti simboli ASCII:
     * 'I': Punto di inizio (Criminal)
     * 'U': Punto di uscita
     * 'S', 'T': Tipologie di trappole
     * 'Ⓖ', 'Ⓙ', 'Ⓓ', 'Ⓑ', 'Ⓨ': Tipologie di gadget
     * 'Ⓝ': Posizione iniziale di un enemy
     * * @param indiceScelto Array contenente l'indice della mappa da caricare (modificato in caso di Torneo).
     * @param tipo Carattere indicante il tipo di gioco ('S' per Storia, altri per Torneo).
     * @return Il {@link Maze} configurato con tutti gli oggetti estratti dalla mappa ASCII.
     */
    public Maze loadLevel(int[] indiceScelto, char tipo) {
        List<String> tutteLeMappe = new ArrayList<>();
        int indiceMappa;

        if (tipo == 'S') {
            tutteLeMappe = leggiFile("src/resources/livelli/livelliStoria.txt");
            if (indiceScelto[0] >= 0 && indiceScelto[0] < tutteLeMappe.size()) {
                indiceMappa = indiceScelto[0];
            } else {
                indiceMappa = 0;
            }
            indiceScelto[0] = indiceMappa;
        } else {
            tutteLeMappe = leggiFile("src/resources/livelli/livelliTorneo.txt");
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
                    xyTrappole[0].add(new Coordinates(currentCol, righe));
                    break;
                case 'T':
                    ch = ' ';
                    xyTrappole[1].add(new Coordinates(currentCol, righe));
                    break;
                case 'Ⓖ': xyGadget[0][0] = righe; xyGadget[0][1] = currentCol; break;
                case 'Ⓙ': xyGadget[1][0] = righe; xyGadget[1][1] = currentCol; break;
                case 'Ⓓ': xyGadget[2][0] = righe; xyGadget[2][1] = currentCol; break;
                case 'Ⓑ': xyGadget[3][0] = righe; xyGadget[3][1] = currentCol; break;
                case 'Ⓨ': xyGadget[4][0] = righe; xyGadget[4][1] = currentCol; break;
                case 'Ⓝ':
                    Enemy n = new Enemy(currentCol, righe);
                    this.enemy.add(n);
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

        return new Maze(mat, inizioX, inizioY, colonne, uscitaX, uscitaY, xyTrappole, xyGadget, enemy);
    }

    /**
     * Legge un file di testo contenente mappe multiple separate dal delimitatore "---".
     * * @param indirizzo Percorso del file da leggere.
     * @return Una lista di stringhe, dove ogni stringa rappresenta una mappa ASCII completa.
     */
    public List<String> leggiFile(String indirizzo) {
        List<String> tutteLeMappe = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(indirizzo))) {
            String mappaCorrente = "";
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.equals("---")) {
                    tutteLeMappe.add(mappaCorrente);
                    mappaCorrente = "";
                } else {
                    mappaCorrente += linea + "\n";
                }
            }

            if (!mappaCorrente.isEmpty())
                tutteLeMappe.add(mappaCorrente);

        } catch (IOException e) {
            System.out.println("Errore caricamento mappe: " + indirizzo);
        }
        return tutteLeMappe;
    }
}