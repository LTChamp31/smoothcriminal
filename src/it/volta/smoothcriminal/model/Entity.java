package it.volta.smoothcriminal.model;

/**
 * La classe {@code Entity} rappresenta la base per qualsiasi oggetto
 * dinamico all'interno del mondo di gioco.
 * Fornisce i parametri di posizionamento (coordinate X e Y) e i
 * relativi metodi di accesso e modifica per il movimento e le
 * interazioni spaziali.
 * * @author Marco Caria & Lotan Teny
 */
public class Entity {

    protected int x;
    protected int y;

    /**
     * Costruttore della classe {@code Entity}.
     * Inizializza l'entità in una posizione specifica della griglia.
     *
     * @param x La posizione iniziale sulla coordinata X.
     * @param y La posizione iniziale sulla coordinata Y.
     */
    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Restituisce la posizione attuale sull'asse X.
     *
     * @return Il valore intero della coordinata X.
     */
    public int getX() {
        return x;
    }

    /**
     * Restituisce la posizione attuale sull'asse Y.
     *
     * @return Il valore intero della coordinata Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Aggiorna simultaneamente entrambe le coordinate dell'entità.
     * Questo metodo viene usato per il teletrasporto
     * o spostamenti istantanei.
     *
     * @param x La nuova posizione sulla coordinata X.
     * @param y La nuova posizione sulla coordinata Y.
     */
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
    Possibili implementazioni per Nemici:
    1. Nemici che ignorano le collisioni e attraversano i muri.
    2. Nemici con pattern di movimento diagonale.
    */
}