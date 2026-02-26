package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.ControllaOggetti;
import it.volta.smoothcriminal.model.*;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * La classe GameLoop gestisce il ciclo principale di esecuzione durante una partita.
 * Coordina il rendering della mappa, la lettura dell'input dell'utente,
 * lo spostamento del giocatore e l'interazione con oggetti e trappole.
 * * @author Marco Caria & Lotan Teny
 */
public class GameLoop {

    private ConsoleUI ui;
    private GiocoConsole giocoConsole;

    /**
     * Costruttore della classe GameLoop.
     * * @param ui L'istanza di {@link ConsoleUI} utilizzata per input e output.
     * @param giocoConsole L'istanza di {@link GiocoConsole} che gestisce lo stato globale.
     */
    public GameLoop(ConsoleUI ui, GiocoConsole giocoConsole) {
        this.ui = ui;
        this.giocoConsole = giocoConsole;
    }

    /**
     * Avvia e mantiene attivo il ciclo di gioco finché non si vince o si perde.
     * All'interno del ciclo vengono gestiti:
     *
     * Il rendering della mappa e del criminale.
     * La visualizzazione dei gadget disponibili.
     * L'elaborazione dei comandi di movimento (WASD) o di uscita (X).
     * L'attivazione dei gadget.
     * Il controllo delle trappole e gadget.
     * * @param labirinto L'istanza del {@link Labirinto} corrente.
     * @param criminal L'istanza del {@link Criminal} controllato dall'utente.
     * @param vittoria Un {@link BooleanSupplier} che restituisce true se il giocatore ha vinto.
     * @param perdita Un {@link BooleanSupplier} che restituisce true se il giocatore ha perso.
     * @param trappole La lista delle {@link Trappola} presenti nel livello corrente.
     */
    public boolean run(Labirinto labirinto, Criminal criminal, BooleanSupplier vittoria, BooleanSupplier perdita, List<Trappola> trappole) {
        ControllaOggetti controllore = new ControllaOggetti(trappole);
        Gadget[] tuttiGadget = it.volta.smoothcriminal.core.CreaOggetti.creaGadget(labirinto, criminal);

        while (!vittoria.getAsBoolean() && !perdita.getAsBoolean()) {
            ui.render(labirinto, criminal);

            if (criminal.getGadgetCriminal()) {
                System.out.print("Gadget disponibili: ");
                criminal.mostraTastiGadget();
            }

            System.out.print("Mossa (WASD) o Gadget: ");
            char move = ui.leggiInput();

            if (move == 'x') {
                return false; // L'utente ha chiesto di uscire
            }

            if (move == 'w' || move == 'a' || move == 's' || move == 'd') {
                criminal.muovi(move, labirinto);
            } else if (Character.isDigit(move)) {
                gestisciUsoGadget(move, criminal);
            }

            controllore.controllaTrappole(criminal.getX(), criminal.getY());
            controllore.controllaGadget(criminal.getX(), criminal.getY(), tuttiGadget, labirinto, criminal);
        }
        return true; // La partita è finita per vittoria o perdita
    }

    /**
     * Metodo per gestire l'attivazione e l'uso di un gadget.
     * Identifica il gadget dal tasto premuto e, se necessario, richiede alla UI
     * la direzione prima di procedere all'uso.
     * * @param tastoChar Il carattere numerico inserito dall'utente.
     * @param criminal L'istanza del giocatore che possiede i gadget.
     */
    private void gestisciUsoGadget(char tastoChar, Criminal criminal) {
        int tasto = tastoChar - '0';
        for (Gadget g : criminal.getGadgetUtilizzabili()) {
            if (g != null && g.getTasto() == tasto) {
                char direzioneInviata = ' ';
                if (g.getNome().equals("distruggi mura") || g.getNome().equals("salta mura")) {
                    direzioneInviata = ui.chiediDirezioneGadget(g.getNome());
                } else if (g.getNome().equals("muove diagonale")) {
                    direzioneInviata = ui.leggiInputDiagonale(g.getNome());
                }

                g.usa(direzioneInviata);
                criminal.rimuoviGadget(g);
                break;
            }
        }
    }
}