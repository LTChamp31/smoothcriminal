package it.volta.smoothcriminal.model;

/**
 * La classe {@code Oggetto} rappresenta tutti gli elementi
 * situati all'interno del labirinto.
 * Essendo una classe astratta, fornisce una struttura comune per sottoclassi come
 * {@link Gadget} e {@link Trappola}, gestendo le coordinate, il nome
 * identificativo e i riferimenti ai componenti del gioco.
 * * @author Marco Caria & Lotan Teny
 */
public abstract class Oggetto {

    protected int x;
    protected int y;
    protected String nome;
    protected Labirinto labirinto;
    protected Criminal criminal;

    /**
     * Costruttore della classe {@code Oggetto}.
     * * @param labirinto L'istanza del {@link Labirinto} corrente.
     * @param criminal  L'istanza del {@link Criminal} controllato dal giocatore.
     * @param x         La posizione iniziale sulla coordinata X.
     * @param y         La posizione iniziale sulla coordinata Y.
     * @param nome      Il nome descrittivo dell'oggetto.
     */
    public Oggetto(Labirinto labirinto, Criminal criminal, int x, int y, String nome) {
        this.labirinto = labirinto;
        this.criminal = criminal;
        this.x = x;
        this.y = y;
        this.nome = nome;
    }

    /**
     * * @return La coordinata X come intero.
     */
    public int getX() { return x; }

    /**
     * * @return La coordinata Y come intero.
     */
    public int getY() { return y; }

    /**
     * * @return Il nome come {@code String}.
     */
    public String getNome() { return nome; }

    /**
     * Metodo astratto che definisce l'interazione con l'oggetto.
     * * @param direzione Il carattere che indica la direzione dell'azione (es. 'w', 'a', 's', 'd').
     */
    public abstract void usa(char direzione);

}