package it.volta.smoothcriminal.model;

/**
 * La classe {@code Trappola} rappresenta un ostacolo
 * che si attiva al contatto con il giocatore.
 * Estende la classe {@link Oggetto} e tiene conto
 * dello spostamento dell'uscita o del teletrasporto
 * del giocatore in una posizione ignota.
 *
 * * @author Marco Caria & Lotan Teny
 */
public class Trappola extends Oggetto {

    /**
     * Costruisce una nuova Trappola con i parametri specificati.
     *
     * @param labirinto Il riferimento al {@link Labirinto} corrente.
     * @param criminal  Il riferimento al {@link Criminal} che attiva la trappola.
     * @param x         La coordinata X della trappola sulla mappa.
     * @param y         La coordinata Y della trappola sulla mappa.
     * @param nome      Il nome della trappola (es. "Sposta Uscita", "Teleport").
     */
    public Trappola(Labirinto labirinto, Criminal criminal, int x, int y, String nome) {
        super(labirinto, criminal, x, y, nome);
    }

    /**
     * Attiva l'effetto della trappola in base al suo nome identificativo.
     * Le tipologie di trappola gestite sono:
     * Sposta Uscita: Individua una cella vuota casuale nel labirinto e
     * vi sposta l'uscita principale ('U').
     * Teleport: Individua una cella vuota casuale e vi teletrasporta
     * istantaneamente il giocatore.
     *
     * @param direzione Il parametro direzione viene accettato per compatibilità con
     * la firma del metodo {@code usa} in {@link Oggetto}, ma viene
     * ignorato dalle trappole poiché l'attivazione è automatica.
     */
    @Override
    public void usa(char direzione) {
        if (nome.equals("Sposta Uscita")) {
            int ux, uy;
            do {
                ux = (int) (Math.random() * labirinto.getColonne());
                uy = (int) (Math.random() * labirinto.getRighe());

                if (!labirinto.isMuro(ux, uy)) {
                    labirinto.spostaUscita(ux, uy);
                }
            } while (labirinto.isMuro(ux, uy));

        } else if (nome.equals("Teleport")) {
            int tx, ty;
            do {
                tx = (int) (Math.random() * labirinto.getColonne());
                ty = (int) (Math.random() * labirinto.getRighe());

                if (!labirinto.isMuro(tx, ty)) {
                    criminal.setXY(tx, ty);
                }
            } while (labirinto.isMuro(tx, ty));
        }
    }
}