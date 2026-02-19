package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.CreaOggetti;
import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.Labirinto;
import it.volta.smoothcriminal.model.Trappole;
import it.volta.smoothcriminal.model.Gadget;

import java.util.List;
import java.util.function.BooleanSupplier;

public class GameLoop {
    private char move, m;
    ConsoleUI ui;
    ControllaOggetti controllaOggetti;
    public GameLoop(ConsoleUI ui) {
        this.ui = ui;
    }

    public void run(Labirinto labirinto, Criminal criminal, BooleanSupplier vittoria, List<Trappole> trappole){
        ui.render(labirinto, criminal);
        controllaOggetti = new ControllaOggetti(trappole);
        while (!vittoria.getAsBoolean()) {
            if(controllaOggetti.getGadgetCriminal()){
                System.out.println("Hai a disposizione dei gadget!!");
            }
            System.out.println("Muoviti: W A S D ");
            move = ui.leggiInput();
            //labirinto.controllaTrappole();
            criminal.muovi(move, labirinto);
            controllaOggetti.controllaTrappole(criminal.getX(), criminal.getY());
            controllaOggetti.controllaGadget(criminal.getX(), criminal.getY(), criminal, CreaOggetti.creaGadget(labirinto, criminal));
            ui.render(labirinto, criminal);
        }
    }
}
