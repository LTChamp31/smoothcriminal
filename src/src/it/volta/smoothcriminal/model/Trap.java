package it.volta.smoothcriminal.model;

/**
 * La classe {@code Trap} rappresenta un ostacolo
 * che si attiva al contatto con il giocatore.
 * Estende la classe {@link Objects} e tiene conto
 * dello spostamento dell'uscita o del teletrasporto
 * del giocatore in una posizione ignota.
 *
 * * @author Marco Caria & Lotan Teny
 */
public class Trap extends Objects {

    /**
     * Costruisce una nuova Trap con i parametri specificati.
     *
     * @param maze Il riferimento al {@link Maze} corrente.
     * @param criminal  Il riferimento al {@link Criminal} che attiva la trappola.
     * @param x         La coordinata X della trappola sulla mappa.
     * @param y         La coordinata Y della trappola sulla mappa.
     * @param nome      Il nome della trappola (es. "Sposta Uscita", "Teleport").
     */
    public Trap(Maze maze, Criminal criminal, int x, int y, String nome) {
        super(maze, criminal, x, y, nome);
    }

    /**
     * Attiva l'effetto della trappola in base al suo nome identificativo.
     * Le tipologie di trappola gestite sono:
     * Sposta Uscita: Individua una cella vuota casuale nel maze e
     * vi sposta l'uscita principale ('U').
     * Teleport: Individua una cella vuota casuale e vi teletrasporta
     * istantaneamente il giocatore.
     *
     * @param direzione Il parametro direzione viene accettato per compatibilità con
     * la firma del metodo {@code usa} in {@link Objects}, ma viene
     * ignorato dalle trappole poiché l'attivazione è automatica.
     */
    @Override
    public void usa(char direzione) {
        if (nome.equals("Sposta Uscita")) {
            int ux, uy;
            do {
                ux = (int) (Math.random() * maze.getColonne());
                uy = (int) (Math.random() * maze.getRighe());

                if (!maze.isMuro(ux, uy)) {
                    maze.spostaUscita(ux, uy);
                }
            } while (maze.isMuro(ux, uy));

        } else if (nome.equals("Teleport")) {
            int tx, ty;
            do {
                tx = (int) (Math.random() * maze.getColonne());
                ty = (int) (Math.random() * maze.getRighe());

                if (!maze.isMuro(tx, ty)) {
                    criminal.setXY(tx, ty);
                }
            } while (maze.isMuro(tx, ty));
        }
    }
}