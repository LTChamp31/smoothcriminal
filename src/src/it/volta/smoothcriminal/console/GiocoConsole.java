package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.Videogioco;
import it.volta.smoothcriminal.model.Labirinto;
import it.volta.smoothcriminal.model.Robot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GiocoConsole extends Videogioco {

    private char move, m;
    private int ris;
    private boolean ok;
    Scanner input = new Scanner(System.in);

    public GiocoConsole(Robot robot, Labirinto labirinto) {
        super(robot, labirinto);
    }

    public void avvia() {
        System.out.println("1. Storia - 2. Allenamento - 3. Torneo ");
        do {
            ris = input.nextInt();
        } while (ris != 1 && ris != 2 && ris != 3);
        if (ris == 1) {
            avviaStoria();
        } else if (ris == 2) {
            avviaAllenamento();
        } else {
            avviaTorneo();
        }
    }

    public void avviaStoria() {
        System.out.println(labirinto.mappa(labirinto.getInizioX(), labirinto.getInizioY()));
        inCorso();
    }

    public void avviaAllenamento() {
        System.out.println(labirinto.mappa(labirinto.getInizioX(), labirinto.getInizioY()));
        inCorso();
    }

    public void avviaTorneo() {
        long inizio = System.currentTimeMillis();
        String nome;
        System.out.print("Inserisci il tuo nome(Verrà salvato): ");
        nome = input.nextLine();
        input.nextLine();
        System.out.println(labirinto.mappa(labirinto.getInizioX(), labirinto.getInizioY()));
        inCorso();
        long fine = System.currentTimeMillis();
        long secondi = (fine - inizio) / 1000;
        //salvaRecord(nome, secondi);
        System.out.println("Nome: " + nome + "\n Secondi: " + secondi);

    }

    public void salvaRecord(String nome, long secondi) {

    }

    public void inCorso(){
        do {
            m = leggiInput();
            mostraStato(m);
        } while (!controllaVittoria());
    }

    public char leggiInput() {
        System.out.print("Muoviti: W A S D ");
        do {
            ok = false;
            move = Character.toLowerCase(input.next().charAt(0));
            if (move == 'w' || move == 'a' || move == 's' || move == 'd') {
                ok = true;
            } else System.out.print("Mossa non valida, riprova:");
        } while (!ok);
        return move;
    }


    public void mostraStato(char move) {
        System.out.println();
        robot.muovi(move, labirinto);
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(labirinto.mappa(robot.getX(), robot.getY()));
    }

}
