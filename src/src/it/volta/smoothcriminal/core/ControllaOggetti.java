package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;
import java.util.List;

/**
 * La classe {@code ControllaOggetti} gestisce la logica di collisione tra il giocatore
 * e gli elementi nel labirinto.
 * Si occupa di monitorare le coordinate del {@link Criminal} per attivare
 * le trappole o permettere la raccolta dei gadget.
 * * @author Marco Caria & Lotan Teny
 */
public class ControllaOggetti {

    private List<Trappola> trappola;

    /**
     * Costruttore della classe {@code ControllaOggetti}.
     * * @param trappola La lista di oggetti {@link Trappola} inizializzati per il livello.
     */
    public ControllaOggetti(List<Trappola> trappola) {
        this.trappola = trappola;
    }

    /**
     * Verifica se le coordinate fornite coincidono con la posizione di una trappola.
     * In caso, viene invocato il metodo {@code usa} della trappola.
     * Viene passato un carattere spazio (' ') come parametro di direzione
     * perchè le trappole non richiedono input dall'utente per attivarsi.
     * * @param x La coordinata X (colonna) attuale del giocatore.
     * @param y La coordinata Y (riga) attuale del giocatore.
     */
    public void controllaTrappole(int x, int y) {
        for (int i = 0; i < trappola.size(); i++) {
            if (trappola.get(i).getX() == x && trappola.get(i).getY() == y) {
                trappola.get(i).usa(' ');
            }
        }
    }

    /**
     * Verifica se il giocatore è passato sopra un gadget non ancora raccolto.
     * Se viene rilevata una collisione:
     * Il gadget viene aggiunto all'inventario del criminale.
     * Il gadget viene segnato come raccolto per evitarne la duplicazione.
     * Il carattere grafico del gadget viene rimosso dalla mappa, a meno che
     * un nemico non stia attualmente occupando quella cella.
     * * @param x La coordinata X (colonna) del giocatore.
     * @param y           La coordinata Y (riga) del giocatore.
     * @param tuttiGadget L'array contenente tutti i {@link Gadget} istanziati nel livello.
     * @param labirinto   L'istanza del {@link Labirinto} per la modifica dei caratteri a video.
     * @param criminal    L'istanza del {@link Criminal} che raccoglie l'oggetto.
     */
    public void controllaGadget(int x, int y, Gadget[] tuttiGadget, Labirinto labirinto, Criminal criminal) {
        for (Gadget g : tuttiGadget) {
            if (g != null && !g.getRaccolto() && g.getX() == y && g.getY() == x) {
                criminal.aggiungiGadget(g);
                g.setRaccolto(true);

                if (labirinto.getCarattere(x, y) != 'Ⓝ') {
                    labirinto.cancellaCarattere(x, y);
                }
            }
        }
    }
}