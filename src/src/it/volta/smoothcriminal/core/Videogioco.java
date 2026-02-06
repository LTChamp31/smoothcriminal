package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;

public abstract class Videogioco {
    protected Robot robot;
    protected Labirinto labirinto;
    protected boolean inCorso;

    public Videogioco(Robot robot, Labirinto labirinto) {
        this.robot = robot;
        this.labirinto = labirinto;
        inCorso = true;
    }

    public boolean controllaVittoria() {
        int x = robot.getX();
        int y = robot.getY();
        if (labirinto.isUscita(x,y)) {
            inCorso = false;
            return true;
        }
        return false;
    }

    public abstract void avvia();
    public abstract char leggiInput();
    public abstract void mostraStato(char move);

}
