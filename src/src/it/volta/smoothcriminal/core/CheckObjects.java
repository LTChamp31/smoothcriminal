package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;
import java.util.List;

/**
 * La classe {@code CheckObjects} gestisce la logica di collisione tra il giocatore
 * e gli elementi nel maze.
 * Si occupa di monitorare le coordinate del {@link Criminal} per attivare
 * le trappole o permettere la raccolta dei gadget.
 * * @author Marco Caria & Lotan Teny
 */
public class CheckObjects {

    private List<Trap> trap;

    /**
     * Costruttore della classe {@code CheckObjects}.
     * * @param trap La lista di oggetti {@link Trap} inizializzati per il livello.
     */
    public CheckObjects(List<Trap> trap) {
        this.trap = trap;
    }

    /**
     * Verifica se le coordinate fornite coincidono con la posizione di una trap.
     * In caso, viene invocato il metodo {@code usa} della trap.
     * Viene passato un carattere spazio (' ') come parametro di direzione
     * perchè le trappole non richiedono input dall'utente per attivarsi.
     * * @param x La coordinata X (colonna) attuale del giocatore.
     * @param y La coordinata Y (riga) attuale del giocatore.
     */
    public void controllaTrappole(int x, int y) {
        for (int i = 0; i < trap.size(); i++) {
            if (trap.get(i).getX() == x && trap.get(i).getY() == y) {
                trap.get(i).usa(' ');
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
     * @param maze   L'istanza del {@link Maze} per la modifica dei caratteri a video.
     * @param criminal    L'istanza del {@link Criminal} che raccoglie l'oggetto.
     */
    public void controllaGadget(int x, int y, Gadget[] tuttiGadget, Maze maze, Criminal criminal) {
        for (Gadget g : tuttiGadget) {
            if (g != null && !g.getRaccolto() && g.getX() == x && g.getY() == y) {
                criminal.aggiungiGadget(g);
                g.setRaccolto(true);

                if (maze.getCarattere(x, y) != 'Ⓝ') {
                    maze.cancellaCarattere(x, y);
                }
            }
        }
    }
}