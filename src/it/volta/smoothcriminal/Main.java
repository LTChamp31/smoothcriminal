package it.volta.smoothcriminal;

import it.volta.smoothcriminal.console.*;
import it.volta.smoothcriminal.gui.GameGUI;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        int sceltaInterfaccia;
        
        sceltaInterfaccia = ui.scegliInterfaccia();
        if (sceltaInterfaccia == 1) {
            GameConsole gioco = new GameConsole(null, null);
            gioco.avvia();
        } else {
            GameGUI gui = new GameGUI(null, null);
            gui.avvia();
        }
    }
}