package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.model.*;

import java.util.List;

public class ControllaOggetti {
    private List<Trappole> trappole;

    public ControllaOggetti(List<Trappole> trappole) {
        this.trappole = trappole;
    }

    public void controllaTrappole(int x, int y) {
        for (int i=0; i<trappole.size(); i++){
            if (trappole.get(i).getX() == x && trappole.get(i).getY() == y){
                trappole.get(i).usa();
            }
        }
    }

    public void controllaGadget(int x, int y, Gadget[] tuttiGadget, Labirinto labirinto, Criminal criminal){
        for(Gadget g : tuttiGadget){
            if(g!=null && !g.getRaccolto() && g.getX()==y && g.getY()==x){
                criminal.aggiungiGadget(g);
                labirinto.cancellaCarattere(x,y);
                g.setRaccolto(true);
            }
        }
    }




}
