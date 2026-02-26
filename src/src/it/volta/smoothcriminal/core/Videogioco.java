package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;

import java.util.List;

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

    public boolean controllaPerdita() {
        int cx = criminal.getX();
        int cy = criminal.getY();
        int nx, ny;
        List<Nemico> nemico = labirinto.getNemici();
        for (int i = 0; i< nemico.size(); i++) {
            nx = nemico.get(i).getX();
            ny = nemico.get(i).getY();
            if(nx == cx && ny == cy) {
                return true;
            } else {
                nemico.get(i).muovi(labirinto, criminal);
            }
        }

        return false;
    }

    public abstract boolean avvia();

}
