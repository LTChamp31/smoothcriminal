package it.volta.smoothcriminal.model;

import java.util.List;

/**
 * La classe {@code Maze} rappresenta l'ambiente di gioco
 * Gestisce la matrice dei caratteri del maze, le posizioni iniziali,
 * i punti di uscita e le liste di oggetti interattivi come trappole, gadget e nemici.
 * * @author Marco Caria & Lotan Teny
 */
public class Maze {
    private final int inizioX;
    private final int inizioY;
    private final int colonne;
    private int uscitaY;
    private int uscitaX;
    private List<List<Character>> mat;
    private List<Coordinates>[] xyTrappole;
    private int[][] xyGadget;
    private List<Enemy> enemy;

    /**
     * Costruisce un nuovo oggetto Maze con tutti i parametri necessari per la sessione.
     *
     * @param mat          La matrice di caratteri che rappresenta i muri e i percorsi.
     * @param inizioX      Coordinata X del punto di partenza del giocatore.
     * @param inizioY      Coordinata Y del punto di partenza del giocatore.
     * @param colonne      Numero totale di colonne del maze.
     * @param uscitaX      Coordinata X iniziale dell'uscita.
     * @param uscitaY      Coordinata Y iniziale dell'uscita.
     * @param xyTrappole   Array di liste contenente le coordinate delle trappole.
     * @param xyGadget     Matrice contenente le posizioni dei gadget.
     * @param enemy       Lista dei nemici presenti nel livello.
     */
    public Maze(List<List<Character>> mat, int inizioX, int inizioY, int colonne, int uscitaX, int uscitaY, List<Coordinates>[] xyTrappole, int[][] xyGadget, List<Enemy> enemy) {
        this.mat = mat;
        this.inizioX = inizioX;
        this.inizioY = inizioY;
        this.colonne = colonne;
        this.uscitaX = uscitaX;
        this.uscitaY = uscitaY;
        this.xyTrappole = xyTrappole;
        this.xyGadget = xyGadget;
        this.enemy = enemy;
    }

    /**
     * Verifica se una specifica coordinata corrisponde a un muro ({@code █}).
     * Restituisce vero anche se le coordinate fornite sono esterne ai limiti della mappa.
     *
     * @param x Coordinata X da controllare.
     * @param y Coordinata Y da controllare.
     * @return {@code true} se la cella è un muro o fuori dai confini, {@code false} altrimenti.
     */
    public boolean isMuro(int x, int y) {
        if (y < 0 || y >= mat.size() || x < 0 || x >= mat.get(y).size()) {
            return true;
        }
        return mat.get(y).get(x) == '█';
    }

    /**
     * Verifica se la cella indicata contiene il simbolo dell'uscita ({@code U}).
     *
     * @param x Coordinata X.
     * @param y Coordinata Y.
     * @return {@code true} se la cella è l'uscita.
     */
    public boolean isUscita(int x, int y) {
        return mat.get(y).get(x) == 'U';
    }

    /**
     * Genera il maze.
     * Inserisce il simbolo del giocatore ({@code Ⓒ}) alle coordinate specificate.
     *
     * @param x Coordinata X attuale del giocatore.
     * @param y Coordinata Y attuale del giocatore.
     * @return Una stringa rappresentante lo stato attuale della mappa.
     */
    public String mappa(int x, int y) {
        StringBuilder map = new StringBuilder();
        for (int i = 0; i < mat.size(); i++) {
            List<Character> row = mat.get(i);
            for (int j = 0; j < colonne; j++) {
                if (i == y && j == x) map.append("Ⓒ");
                else if (j < row.size()) {
                    map.append(row.get(j));
                } else {
                    map.append(" ");
                }
            }
            map.append("\n");
        }
        return map.toString();
    }

    /**
     * Sovrascrive il carattere in una determinata posizione.
     *
     * @param x Coordinata X.
     * @param y Coordinata Y.
     * @param c Il nuovo carattere da inserire.
     */
    public void setCarattere(int x, int y, char c) { mat.get(y).set(x, c); }

    /**
     * Rende una cella calpestabile sostituendo il carattere attuale con uno spazio vuoto.
     *
     * @param x Coordinata X.
     * @param y Coordinata Y.
     */
    public void cancellaCarattere(int x, int y) {
        mat.get(y).set(x, ' ');
    }

    /**
     * Rimuove l'uscita dalla posizione attuale e la sposta in una nuova coordinata.
     *
     * @param ux Nuova coordinata X dell'uscita.
     * @param uy Nuova coordinata Y dell'uscita.
     */
    public void spostaUscita(int ux, int uy) {
        mat.get(this.uscitaY).set(this.uscitaX, ' ');
        this.uscitaX = ux;
        this.uscitaY = uy;
        mat.get(this.uscitaY).set(this.uscitaX, 'U');
    }

    /**
     * Avvicina l'uscita al giocatore di una posizione in base alla direzione relativa tra i due.
     *
     * @param xc Coordinata X del criminale.
     * @param yc Coordinata Y del criminale.
     * @param xu Coordinata X attuale dell'uscita.
     * @param yu Coordinata Y attuale dell'uscita.
     */
    public void avvicinaUscita(int xc, int yc, int xu, int yu) {
        int stepX = Integer.compare(xc, xu);
        int stepY = Integer.compare(yc, yu);

        mat.get(this.uscitaY).set(this.uscitaX, ' ');
        mat.get(this.uscitaY + stepX).set(this.uscitaX + stepY, 'U');
    }

    /** @return Coordinata X di partenza del giocatore. */
    public int getInizioX() { return inizioX; }

    /** @return Coordinata Y di partenza del giocatore. */
    public int getInizioY() { return inizioY; }

    /** @return Coordinata X attuale dell'uscita. */
    public int getUscitaX() { return uscitaX; }

    /** @return Coordinata Y attuale dell'uscita. */
    public int getUscitaY() { return uscitaY; }

    /** @return Matrice delle posizioni dei gadget. */
    public int[][] getGadget() { return xyGadget; }

    /** @return Array di liste delle coordinate delle trappole. */
    public List<Coordinates>[] getTrappole() { return xyTrappole; }

    /** @return Numero totale di colonne. */
    public int getColonne() { return colonne; }

    /** @return Numero totale di righe. */
    public int getRighe() { return mat.size(); }

    /** @return Lista dei nemici presenti. */
    public List<Enemy> getNemici() { return enemy; }

    /**
     * Restituisce il carattere presente a una specifica coordinata.
     * Restituisce uno spazio se le coordinate sono esterne alla mappa.
     *
     * @param x Coordinata X.
     * @param y Coordinata Y.
     * @return Il carattere contenuto nella cella.
     */
    public char getCarattere(int x, int y) {
        if (y >= 0 && y < mat.size() && x >= 0 && x < mat.get(y).size()) {
            return mat.get(y).get(x);
        }
        return ' ';
    }
}