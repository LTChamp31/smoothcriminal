package it.volta.smoothcriminal;

import it.volta.smoothcriminal.console.*;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();

        int sceltaInterfaccia;
        do {
            sceltaInterfaccia = ui.scegliInterfaccia();
            if (sceltaInterfaccia == 1) {
                GameConsole gioco = new GameConsole(null, null);
                gioco.avvia();
            } else {
                System.out.println("L'interfaccia grafica è ancora in fase di sviluppo!!");
            }
        } while (sceltaInterfaccia == 2);
    }
}