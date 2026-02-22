package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.CreaOggetti;
import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.Gadget;
import it.volta.smoothcriminal.model.Labirinto;
import it.volta.smoothcriminal.model.Trappole;

import java.util.List;

public class ControllaOggetti {
    private CreaOggetti creaOggetti;
    private List<Trappole> trappole;
    private boolean gadgetCriminal = false;

    public ControllaOggetti(List<Trappole> trappole)
    {
        this.trappole = trappole;
    }

    public void controllaTrappole(int x, int y) {
        for (int i=0; i<trappole.size(); i++){
            if (trappole.get(i).getX() == x && trappole.get(i).getY() == y){
                trappole.get(i).usa();
            }
        }
    }

    public void controllaGadget(int x, int y, Criminal criminal, Gadget[] tuttiGadget, Labirinto labirinto){
        for(Gadget g : tuttiGadget){
            if(g!=null && g.getRaccolto()==false && g.getX()==y && g.getY()==x){
                gadgetCriminal = true;
                criminal.aggiungiGadget(g);
                labirinto.cancellaCarattere(x,y);
                g.setRaccolto(true);
            }
        }
    }


}
