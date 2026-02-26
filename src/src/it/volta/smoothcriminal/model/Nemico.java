package it.volta.smoothcriminal.model;

import it.volta.smoothcriminal.model.Entita;

/**
 * La classe {@code Nemico} rappresenta un'entità all'interno del gioco che
 * insegue il {@link Criminal}.
 * Implementa una logica di movimento verso la posizione del giocatore e
 * gestisce un sistema di "carattere sotto" per evitare di cancellare graficamente
 * gadget o trappole quando vi transita sopra.
 *
 * * @author Marco Caria & Lotan Teny
 */
public class Nemico extends Entita {

    /** * Memorizza il carattere grafico presente nella cella prima del passaggio del nemico.
     * Serve a ripristinare correttamente la mappa dopo lo spostamento.
     */
    private char carattereSotto = ' ';

    /**
     * Costruttore della classe Nemico.
     * Inizializza la posizione dell'entità nemica.
     *
     * @param x Coordinata X iniziale (colonna).
     * @param y Coordinata Y iniziale (riga).
     */
    public Nemico(int x, int y) {
        super(x, y);
    }

    /**
     * Gestisce il movimento del nemico verso il giocatore.
     * Il processo segue queste fasi:
     * Ripristina il carattere originale nella posizione attuale prima di spostarsi
     * Calcola la direzione (asse X o Y) per avvicinarsi al giocatore.
     * Verifica la presenza di muri tramite {@link Labirinto#isMuro(int, int)} prima di muoversi.
     * Salva il carattere presente nella nuova cella (a meno che non sia il giocatore)
     * Posiziona l'icona del nemico ({@code Ⓝ}) nella nuova posizione sulla mappa.
     *
     * @param mappa    L'istanza del {@link Labirinto} corrente per controlli e aggiornamenti grafici.
     * @param criminal L'istanza del {@link Criminal} da inseguire per ottenerne le coordinate.
     */
    public void muovi(Labirinto mappa, Criminal criminal) {
        mappa.setCarattere(x, y, carattereSotto);

        int criminalX = criminal.getX();
        int criminalY = criminal.getY();

        int stepX = Integer.compare(criminalX, x);
        int stepY = Integer.compare(criminalY, y);

        int nuovaX = x;
        int nuovaY = y;

        if (Math.abs(criminalX - x) > Math.abs(criminalY - y)) {
            if (stepX != 0 && !mappa.isMuro(x + stepX, y)) nuovaX += stepX;
            else if (stepY != 0 && !mappa.isMuro(x, y + stepY)) nuovaY += stepY;
        } else {
            if (stepY != 0 && !mappa.isMuro(x, y + stepY)) nuovaY += stepY;
            else if (stepX != 0 && !mappa.isMuro(x + stepX, y)) nuovaX += stepX;
        }

        setXY(nuovaX, nuovaY);

        char nuovoCarattereSotto = mappa.getCarattere(x, y);
        if (nuovoCarattereSotto != 'Ⓒ') {
            carattereSotto = nuovoCarattereSotto;
        } else {
            carattereSotto = ' ';
        }

        mappa.setCarattere(x, y, 'Ⓝ');
    }
}