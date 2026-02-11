package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.Videogioco;
import it.volta.smoothcriminal.model.Labirinto;
import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.LevelLoader;

import java.util.Scanner;

public class GiocoConsole extends Videogioco {

    ConsoleUI ui;
    GameLoop loop;
    LevelLoader loader = new LevelLoader();

    public GiocoConsole(Criminal criminal, Labirinto labirinto) {
        super(criminal, labirinto);
        this.ui = new ConsoleUI();
        this.loop = new GameLoop(ui);
    }

    public void avvia() {
        int ris = ui.scegliModalita();
        if (ris == 1) {
            avviaStoria();
        } else if (ris == 2) {
            avviaAllenamento();
        } else {
            avviaTorneo();
        }
    }

    public void avviaStoria() {
        int level = 1;
        do {
            Labirinto labirinto = loader.loadLevel(level);
            loop.run(labirinto, criminal, this::controllaVittoria);
            menu()
        } while(level);

    }

    public void avviaAllenamento() {
        long inizio = System.currentTimeMillis();
        loop.run(labirinto, criminal, this::controllaVittoria);
        long fine = System.currentTimeMillis();
        long secondi = (fine - inizio) / 1000;
    }

    public void avviaTorneo() {
        long inizio = System.currentTimeMillis();

        String nome = ui.scegliNome();
        loop.run(labirinto, criminal, this::controllaVittoria);

        long fine = System.currentTimeMillis();
        long secondi = (fine - inizio) / 1000;
        //salvaRecord(nome, secondi);
        System.out.println("Nome: " + nome + "\n Secondi: " + secondi);

    }

    public void salvaRecord(String nome, long secondi, int mappa) {
        //un file.txt per ogni mappa del torneo
        //mappe della storia in un file, SI Entità(non subito), SI trappole(non subito), SI Gadget(non subito)
        //mappe dell'allenamento generate a caso, a scelta dell'utente gadget, trappole ed entità
        //mappe del torneo in un file, difficolta crescente, NO Gadget, SI Entità, SI trappole
    }




}
