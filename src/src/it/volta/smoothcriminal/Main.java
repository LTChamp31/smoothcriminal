package it.volta.smoothcriminal;

import it.volta.smoothcriminal.console.GiocoConsole;
import it.volta.smoothcriminal.model.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        LevelLoader loader = new LevelLoader();
        Labirinto labirinto = loader.loadLevel(1);
        Criminal criminal = new Criminal(labirinto.getInizioX(), labirinto.getInizioY());

        GiocoConsole gioco = new GiocoConsole(criminal, labirinto);
        Scanner input = new Scanner(System.in);
        int ris;


        System.out.println("Giocare su console -1 | Giocare su GUI -2");

        do {
            ris = input.nextInt();
        } while (ris != 1 && ris != 2);
        if (ris == 1) {
            gioco.avvia();
        }

    }
}
