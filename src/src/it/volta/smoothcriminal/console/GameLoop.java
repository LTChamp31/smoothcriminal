package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.CreaOggetti;
import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.Labirinto;

import java.util.function.BooleanSupplier;

public class GameLoop {
    private char move, m;
    ConsoleUI ui;
    ControllaOggetti controllaOggetti;
    public GameLoop(ConsoleUI ui) {
        this.ui = ui;
    }

    public void run(Labirinto labirinto, Criminal criminal, BooleanSupplier vittoria, CreaOggetti creaOggetti){
        ui.render(labirinto, criminal);
        controllaOggetti = new ControllaOggetti(creaOggetti);
        while (!vittoria.getAsBoolean()) {
            System.out.print("Muoviti: W A S D ");
            move = ui.leggiInput();
            //labirinto.controllaTrappole();
            criminal.muovi(move, labirinto);
            controllaOggetti.controllaTrappole(criminal.getX(), criminal.getY());
            ui.render(labirinto, criminal);
        }
    }
}
