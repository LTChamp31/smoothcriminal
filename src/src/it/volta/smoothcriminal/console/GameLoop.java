package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.Labirinto;

import java.util.function.BooleanSupplier;

public class GameLoop {
    private char move, m;
    ConsoleUI ui;

    public GameLoop(ConsoleUI ui) {
        this.ui = ui;
    }

    public void run(Labirinto labirinto, Criminal criminal, BooleanSupplier vittoria){
        ui.render(labirinto, criminal);

        while (!vittoria.getAsBoolean()) {
            System.out.print("Muoviti: W A S D ");
            move = ui.leggiInput();
            criminal.muovi(move, labirinto);
            ui.render(labirinto, criminal);
        }
    }
}
