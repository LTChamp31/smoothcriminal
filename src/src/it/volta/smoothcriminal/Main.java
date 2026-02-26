package it.volta.smoothcriminal;

import it.volta.smoothcriminal.console.*;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();

        int sceltaInterfaccia;
        do {
            sceltaInterfaccia = ui.scegliInterfaccia();
            if (sceltaInterfaccia == 1) {
                GiocoConsole gioco = new GiocoConsole(null, null);
                boolean continua = true;
                while (continua) {
                    continua = gioco.avvia();
                }
            } else {
                System.out.println("L'interfaccia grafica è ancora in fase di sviluppo!!");
            }
        } while (sceltaInterfaccia == 2);
    }
}