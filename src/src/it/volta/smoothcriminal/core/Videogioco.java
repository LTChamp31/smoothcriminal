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
        List<Nemici> nemici = labirinto.getNemici();
        for (int i=0; i<nemici.size(); i++) {
            nx = nemici.get(i).getX();
            ny = nemici.get(i).getY();
            //System.out.println(nx + " " + ny + "\n" + cx + " " + cy);
            if(nx == cx && ny == cy) {
                return true;
            } else {
                nemici.get(i).muovi(labirinto, criminal);
            }
        }

        return false;
    }

    public abstract boolean avvia();

}
