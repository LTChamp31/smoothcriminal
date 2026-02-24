package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.CreaOggetti;
import it.volta.smoothcriminal.model.*;

import java.util.List;

public class ControllaOggetti {
    private CreaOggetti creaOggetti;
    private List<Trappole> trappole;
    //private Criminal criminal;
    //private int criminalX, criminalY;

    public ControllaOggetti(/*Criminal criminal,*/ List<Trappole> trappole/*, int criminalX, int criminalY*/)
    {
        //this.criminal = criminal;
        this.trappole = trappole;
        //this.criminalX = criminalX;
        //this.criminalY = criminalY;
    }

    public void controllaTrappole(int x, int y) {
        for (int i=0; i<trappole.size(); i++){
            if (trappole.get(i).getX() == x && trappole.get(i).getY() == y){
                trappole.get(i).usa();
            }
        }
    }

    public boolean controllaNemici(Labirinto labirinto, Criminal criminal) {
        int nx, ny;
        List<Nemici> nemici = labirinto.getNemici();
        for (int i=0; i<nemici.size(); i++) {
            nx = nemici.get(i).getX();
            ny = nemici.get(i).getY();

            if(nx == criminal.getX() && ny == criminal.getY()) {
                return true;
            } else {
                nemici.get(i).muovi(labirinto, criminal);
            }
        }

        return false;
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
