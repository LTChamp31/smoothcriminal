package it.volta.smoothcriminal.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * La classe {@code GenerateMaze} è responsabile della generazione procedurale
 * di un maze utilizzando l'algoritmo di backtracking ricorsivo.
 * Il processo inizia riempiendo completamente la griglia di muri e successivamente
 * "scavando" percorsi casuali garantendo che l'intero maze sia navigabile.
 * * @author Marco Caria & Lotan Teny
 */
public class GenerateMaze {

    private int altezza, larghezza;

    private char[][] matrice;

    /** * Template delle direzioni possibili per lo scavo (Nord, Sud, Est, Ovest).
     * I valori indicano lo spostamento di due unità per saltare il muro intermedio.
     */
    private Integer[][] direzioniTemplate = {{0,2},{0,-2},{2,0},{-2,0}};

    /**
     * Costruttore della classe. Inizializza la matrice riempiendola interamente
     * con il carattere muro ('█').
     * * @param altezza Il numero di righe della matrice.
     * @param larghezza Il numero di colonne della matrice.
     */
    public GenerateMaze(int altezza, int larghezza){
        this.altezza = altezza;
        this.larghezza = larghezza;
        matrice = new char[altezza][larghezza];

        for (int i=0; i<altezza; i++) {
            for (int j=0; j<larghezza; j++) {
                matrice[i][j] = '█';
            }
        }
    }

    /**
     * Metodo principale per la generazione del maze.
     * Coordina lo scavo dei percorsi, imposta il punto di uscita ('U') e
     * converte la matrice interna nel formato List di List richiesto dal modello.
     * * @return Una rappresentazione del maze come {@code List<List<Character>>}.
     */
    public List<List<Character>> creaLabirinto(){
        crearePassagio(1,1);

        matrice[altezza-2][larghezza-1] = 'U';

        if (matrice[altezza-2][larghezza-2] == '█') {
            matrice[altezza-2][larghezza-2] = ' ';
        }

        List<List<Character>> mat = new ArrayList<>();
        for (char[] row : matrice) {
            List<Character> listRow = new ArrayList<>();
            for (char c : row) {
                listRow.add(c);
            }
            mat.add(listRow);
        }
        return mat;
    }

    /**
     * Algoritmo ricorsivo di scavo dei passaggi.
     * Utilizza una tecnica di randomizzazione delle direzioni per creare
     * percorsi sempre diversi (DFS - Depth First Search randomizzata).
     * Per ogni cella, controlla i vicini a distanza 2; se non sono stati visitati,
     * rimuove il muro intermedio e procede ricorsivamente.
     * * @param i Coordinata della riga corrente.
     * @param j Coordinata della colonna corrente.
     */
    public void crearePassagio(int i, int j){
        matrice[i][j] = ' ';

        List<Integer[]> localDirezioni = new ArrayList<>(Arrays.asList(direzioniTemplate));
        Collections.shuffle(localDirezioni);

        for (Integer[] dir : localDirezioni) {
            int prossimoI = i + dir[0];
            int prossimoJ = j + dir[1];

            if (prossimoI >= 0 && prossimoI < altezza-1 && prossimoJ >= 0 && prossimoJ < larghezza-1 && matrice[prossimoI][prossimoJ] == '█') {
                matrice[i + dir[0]/2][j + dir[1]/2] = ' ';

                crearePassagio(prossimoI, prossimoJ);
            }
        }
    }

    /** @return L'altezza impostata per il maze. */
    public int getAltezza() { return altezza; }

    /** @return La larghezza impostata per il maze. */
    public int getLarghezza() { return larghezza; }
}