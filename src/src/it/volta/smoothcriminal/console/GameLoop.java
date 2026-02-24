package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.CreaOggetti;
import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.Labirinto;
import it.volta.smoothcriminal.model.Trappole;
import it.volta.smoothcriminal.model.Gadget;

import java.util.List;
import java.util.function.BooleanSupplier;

public class GameLoop {
    private char move;
    private ConsoleUI ui;
    private ControllaOggetti controllaOggetti;

    public GameLoop(ConsoleUI ui) {
        this.ui = ui;
    }

    public void run(Labirinto labirinto, Criminal criminal, BooleanSupplier vittoria, BooleanSupplier perdita, List<Trappole> trappole){
        Gadget[] tuttiGadget = CreaOggetti.creaGadget(labirinto, criminal);
        ui.render(labirinto, criminal);
        controllaOggetti = new ControllaOggetti(/*criminal, */trappole/*, criminal.getX(), criminal.getY()*/);
        while (!vittoria.getAsBoolean() && !perdita.getAsBoolean()) {
            if(criminal.getGadgetCriminal()){
                System.out.println("Hai a disposizione dei gadget!!");
                System.out.print("Premi: ");
                criminal.mostraTastiGadget();
            }
            System.out.print("Muoviti: W A S D ");
            move = ui.leggiInput();
            if (move == 'w' || move == 'a' || move == 's' || move == 'd') {
                criminal.muovi(move, labirinto);
                controllaOggetti.controllaTrappole(criminal.getX(), criminal.getY());
                controllaOggetti.controllaGadget(criminal.getX(), criminal.getY(), tuttiGadget, labirinto, criminal);
            }
            else{
                int tasto = move - '0';
                Gadget[] gadgetUtilizabili = criminal.getGadgetUtilizzabili();
                for(int i=0;i<3;i++){
                    if(gadgetUtilizabili[i]!=null && tasto==gadgetUtilizabili[i].getTasto()){
                        gadgetUtilizabili[i].usa();
                        criminal.rimuoviGadget(gadgetUtilizabili[i]);

                    }
                }
            }
            ui.render(labirinto, criminal);
        }
    }
}
