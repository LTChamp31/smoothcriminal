package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.Videogioco;
import it.volta.smoothcriminal.model.Labirinto;
import it.volta.smoothcriminal.model.Robot;
import java.util.Scanner;
public class GiocoConsole extends Videogioco {

    private char move, m;
    private boolean ok;
    Scanner input = new Scanner(System.in);

    public GiocoConsole(Robot robot, Labirinto labirinto) {
        super(robot, labirinto);
    }

    public void avvia() {
        System.out.println(labirinto.mappa(labirinto.getInizioX(), labirinto.getInizioY()));
        do {
            m = leggiInput();
            mostraStato(m);
        } while(!controllaVittoria());
    }

    public char leggiInput() {
        System.out.print("Muoviti: W A S D ");
        do {
            ok = false;
            move = Character.toLowerCase(input.next().charAt(0));
            if (move == 'w' || move == 'a' || move == 's' || move == 'd') {
                ok = true;
            }
            else System.out.print("Mossa non valida, riprova:");
        } while (!ok);
        return move;
    }


    public void mostraStato(char move) {
        System.out.println();
        robot.muovi(move, labirinto);
        System.out.print("\033[H\033[2J");
        System.out.println(labirinto.mappa(robot.getX(), robot.getY()));
    }

}
