package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.CreaOggetti;
import it.volta.smoothcriminal.model.Trappole;

public class ControllaOggetti {
    CreaOggetti creaOggetti;
    private Trappole[] trappole;
    public ControllaOggetti(CreaOggetti creaOggetti){
        this.creaOggetti = creaOggetti;
        trappole = creaOggetti.getTrappole();
        System.out.println(trappole[0].getNome());

    }

    public void controllaTrappole(int x, int y) {
        System.out.println(trappole.length);
        for (int i=0; i<2; i++){
            System.out.println(x + " " + y + "\n" + trappole[i].getX() + " " + trappole[i].getY());
            if  (trappole[i].getX() == x && trappole[i].getY() == y){
                trappole[i].usa();
            }
        }
    }
}
