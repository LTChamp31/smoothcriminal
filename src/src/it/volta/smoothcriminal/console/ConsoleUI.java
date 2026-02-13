package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.Labirinto;

import java.util.Scanner;

public class ConsoleUI {
    Scanner input = new Scanner(System.in);

    public ConsoleUI() {
    }
    public String scegliNome() {
        System.out.println("Inserisci il tuo nome:");
        input.nextLine();
        return input.nextLine();
    }

    public int scegliModalita() {
        int ris;
        System.out.println("1. Storia - 2. Allenamento - 3. Torneo ");
        do {
            ris = input.nextInt();
        } while (ris != 1 && ris != 2 && ris != 3);
        return ris;
    }


    public void render(Labirinto labirinto, Criminal criminal) {
        System.out.println();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(labirinto.mappa(criminal.getX(), criminal.getY()));
    }

    public char leggiInput() {
        boolean ok;
        char move;
        do {
            ok = false;
            move = Character.toLowerCase(input.next().charAt(0));
            if (move == 'w' || move == 'a' || move == 's' || move == 'd') {
                ok = true;
            } else System.out.print("Mossa non valida, riprova:");
        } while (!ok);
        return move;
    }

    public char leggiInput(char diagonale) {
        boolean ok;
        char move;
        do {
            ok = false;
            move = Character.toLowerCase(input.next().charAt(0));
            if (move == 'q' || move == 'e' || move == 'z' || move == 'c') {
                ok = true;
            } else System.out.print("Mossa non valida, riprova:");
        } while (!ok);
        return move;
    }


}
