package it.volta.smoothcriminal.model;

/**
 * La classe {@code Objects} rappresenta tutti gli elementi
 * situati all'interno del maze.
 * Essendo una classe astratta, fornisce una struttura comune per sottoclassi come
 * {@link Gadget} e {@link Trap}, gestendo le coordinate, il nome
 * identificativo e i riferimenti ai componenti del gioco.
 * * @author Marco Caria & Lotan Teny
 */
public abstract class Objects {

    protected int x;
    protected int y;
    protected String nome;
    protected Maze maze;
    protected Criminal criminal;

    /**
     * Costruttore della classe {@code Objects}.
     * * @param maze L'istanza del {@link Maze} corrente.
     * @param criminal  L'istanza del {@link Criminal} controllato dal giocatore.
     * @param x         La posizione iniziale sulla coordinata X.
     * @param y         La posizione iniziale sulla coordinata Y.
     * @param nome      Il nome descrittivo dell'oggetto.
     */
    public Objects(Maze maze, Criminal criminal, int x, int y, String nome) {
        this.maze = maze;
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