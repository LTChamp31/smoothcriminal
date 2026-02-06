package it.volta.smoothcriminal;

import it.volta.smoothcriminal.console.GiocoConsole;
import it.volta.smoothcriminal.core.Videogioco;
import it.volta.smoothcriminal.model.*;

public class Main {
    public static void main(String[] args) {
        Labirinto labirinto = new Labirinto();
        Robot robot = new Robot(labirinto.getInizioX(), labirinto.getInizioY());
        GiocoConsole gioco = new GiocoConsole(robot, labirinto);

        gioco.avvia();



    }
}
