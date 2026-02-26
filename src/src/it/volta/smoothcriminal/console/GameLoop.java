package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.ControllaOggetti;
import it.volta.smoothcriminal.model.*;
import java.util.List;
import java.util.function.BooleanSupplier;

public class GameLoop {
    private ConsoleUI ui;
    private GiocoConsole giocoConsole;

    public GameLoop(ConsoleUI ui, GiocoConsole giocoConsole) {
        this.ui = ui;
        this.giocoConsole = giocoConsole;
    }

    public void run(Labirinto labirinto, Criminal criminal, BooleanSupplier vittoria, BooleanSupplier perdita, List<Trappola> trappole) {
        ControllaOggetti controllore = new ControllaOggetti(trappole);
        Gadget[] tuttiGadget = it.volta.smoothcriminal.core.CreaOggetti.creaGadget(labirinto, criminal);

        while (!vittoria.getAsBoolean() && !perdita.getAsBoolean()) {
            ui.render(labirinto, criminal);

            if (criminal.getGadgetCriminal()) {
                System.out.print("Gadget disponibili (Premi i tasti): ");
                criminal.mostraTastiGadget();
            }

            System.out.print("Mossa (WASD) o Gadget: ");
            char move = ui.leggiInput();

            if (move == 'x') {
                giocoConsole.avvia();
                return;
            }

            if (move == 'w' || move == 'a' || move == 's' || move == 'd') {
                criminal.muovi(move, labirinto);
            } else if (Character.isDigit(move)) {
                gestisciUsoGadget(move, criminal);
            }

            controllore.controllaTrappole(criminal.getX(), criminal.getY());
            controllore.controllaGadget(criminal.getX(), criminal.getY(), tuttiGadget, labirinto, criminal);
        }
    }

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