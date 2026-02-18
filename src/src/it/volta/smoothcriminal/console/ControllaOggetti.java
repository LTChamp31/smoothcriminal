package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.CreaOggetti;
import it.volta.smoothcriminal.model.Trappole;

import java.util.List;

public class ControllaOggetti {
    CreaOggetti creaOggetti;
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
}
