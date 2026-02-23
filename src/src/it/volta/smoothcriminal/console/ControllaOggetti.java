package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.CreaOggetti;
import it.volta.smoothcriminal.model.*;

import java.util.List;

public class ControllaOggetti {
    private CreaOggetti creaOggetti;
    private List<Trappole> trappole;
    private boolean gadgetCriminal = false;
    private Criminal criminal;
    private int cx, cy;
    public ControllaOggetti(Criminal criminal, List<Trappole> trappole, int cx, int cy)
    {
        this.criminal = criminal;
        this.trappole = trappole;
        this.cx = cx;
        this.cy = cy;
    }

    public void controllaTrappole() {
        for (int i=0; i<trappole.size(); i++){
            if (trappole.get(i).getX() == cx && trappole.get(i).getY() == cy){
                trappole.get(i).usa();
            }
        }
    }

    public boolean controllaNemici(Labirinto labirinto) {
        int nx, ny;
        List<Nemici> nemici = labirinto.getNemici();
        for (int i=0; i<nemici.size(); i++) {
            nx = nemici.get(i).getX();
            ny = nemici.get(i).getY();

            if(nx == cx && ny == cy) {
                return true;
            } else {
                nemici.get(i).muovi(labirinto, criminal);
            }
        }

        return false;
    }

    public void controllaGadget(Gadget[] tuttiGadget, Labirinto labirinto){
        for(Gadget g : tuttiGadget){
            if(g!=null && g.getRaccolto()==false && g.getX()==cy && g.getY()==cx){
                gadgetCriminal = true;
                criminal.aggiungiGadget(g);
                labirinto.cancellaCarattere(cx,cy);
                g.setRaccolto(true);
            }
        }
    }


}
