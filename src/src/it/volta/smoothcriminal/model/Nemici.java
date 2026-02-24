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

        mappa.setCarattere(x, y, ' ');

        int stepX = Integer.compare(criminalX, x);
        int stepY = Integer.compare(criminalY, y);

        int distanceX = Math.abs(criminalX - x);
        int distanceY = Math.abs(criminalY - y);

        if (distanceX > distanceY) {
            if (stepX != 0 && !mappa.isMuro(x + stepX, y)) {
                setXY(x + stepX, y);
            } else if (stepY != 0 && !mappa.isMuro(x, y + stepY)) {
                setXY(x, y + stepY);
            }
        } else {
            if (stepY != 0 && !mappa.isMuro(x, y + stepY)) {
                setXY(x, y + stepY);
            } else if (stepX != 0 && !mappa.isMuro(x + stepX, y)) {
                setXY(x + stepX, y);
            }
        }

        mappa.setCarattere(x, y, 'Ⓝ');
    }
}
