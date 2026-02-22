package it.volta.smoothcriminal.model;

import it.volta.smoothcriminal.core.Entita;

public class Nemici extends Entita {
    private int criminalX, criminalY;

    public Nemici(int x, int y) {
        super(x,y);
    }

    public void muovi(Labirinto mappa, Criminal criminal) {
        criminalX = criminal.getX();
        criminalY = criminal.getY();

        int stepX = Integer.compare(criminalX, x);
        int stepY = Integer.compare(criminalY, y);

        if (mappa.isMuro(x+=stepX, y)) {
            if (mappa.isMuro(x, y+=stepY)) {
                if (mappa.isMuro(x-=stepX, y)) {
                  setXY(x, y-=stepY);
                }
                else setXY(x-=stepX, y);
            } else setXY(x, y+=stepY);
        } else setXY(x+=stepX, y);



    }
}
