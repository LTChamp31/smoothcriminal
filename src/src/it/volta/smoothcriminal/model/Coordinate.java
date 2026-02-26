package it.volta.smoothcriminal.model;

/**
 * La classe {@code Coordinate} rappresenta una posizione
 * Viene utilizzata principalmente per memorizzare e trasferire le coordinate (X, Y) di oggetti,
 * trappole e punti di interesse all'interno del {@link Labirinto}.
 * * @author Marco Caria & Lotan Teny
 */
public class Coordinate {

    private int x;
    private int y;

    /**
     * Costruisce un nuovo oggetto Coordinate con i valori specificati.
     * * @param x La posizione sull'asse delle ascisse.
     * @param y La posizione sull'asse delle ordinate.
     */
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Restituisce la coordinata X.
     * * @return Il valore intero della coordinata X.
     */
    public int getX(){return x;}

    /**
     * Restituisce la coordinata Y.
     * * @return Il valore intero della coordinata Y.
     */
    public int getY(){return y;}
}