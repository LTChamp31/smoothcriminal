package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;

import java.util.List;

public class ControllaOggetti {
    private List<Trappola> trappola;

    public ControllaOggetti(List<Trappola> trappola) {
        this.trappola = trappola;
    }

    public void controllaTrappole(int x, int y) {
        for (int i = 0; i< trappola.size(); i++){
            if (trappola.get(i).getX() == x && trappola.get(i).getY() == y){
                trappola.get(i).usa(' ');
            }
        }
    }

    public void controllaGadget(int x, int y, Gadget[] tuttiGadget, Labirinto labirinto, Criminal criminal) {
        for (Gadget g : tuttiGadget) {
            if (g != null && !g.getRaccolto() && g.getX() == y && g.getY() == x) {
                criminal.aggiungiGadget(g);
                g.setRaccolto(true);

                if (labirinto.getCarattere(x, y) != 'Ⓝ') {
                    labirinto.cancellaCarattere(x, y);
                }
            }
        }
    }




}
