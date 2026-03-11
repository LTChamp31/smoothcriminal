package it.volta.smoothcriminal;

import it.volta.smoothcriminal.console.*;
import it.volta.smoothcriminal.gui.GUIApplication;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        int sceltaInterfaccia;
        do {
            sceltaInterfaccia = ui.scegliInterfaccia();
            if (sceltaInterfaccia == 1) {
                GameConsole gioco = new GameConsole(null, null);
                gioco.avvia();
            } else if (sceltaInterfaccia == 2) {
                Application.launch(GUIApplication.class, args);
            }
        } while (sceltaInterfaccia == 2 && sceltaInterfaccia != 2); // Prevent loop after GUI exit since GUI manages its own lifecycle
    }
}