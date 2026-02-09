package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;

public abstract class Videogioco {
    protected Criminal criminal;
    protected Labirinto labirinto;
    protected boolean inCorso;

    public Videogioco(Criminal criminal, Labirinto labirinto) {
        this.criminal = criminal;
        this.labirinto = labirinto;
        inCorso = true;
    }

    public boolean controllaVittoria() {
        int x = criminal.getX();
        int y = criminal.getY();
        if (labirinto.isUscita(x,y)) {
            inCorso = false;
            return true;
        }
        return false;
    }

    public abstract void avvia();

}
