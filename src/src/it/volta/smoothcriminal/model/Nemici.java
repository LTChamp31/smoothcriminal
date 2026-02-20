package it.volta.smoothcriminal.model;

import it.volta.smoothcriminal.core.Entita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Nemici extends Entita {
    int crx, cry;
    public Nemici(int x, int y) {
        super(x,y);
    }

    public void muovi(Labirinto map, Criminal criminal) {
        crx = criminal.getX();
        cry = criminal.getY();

        int stepX = Integer.compare(crx, x);
        int stepY = Integer.compare(cry, y);

        if (map.isMuro(x+=stepX, y)) {
            if (map.isMuro(x, y+=stepY)) {
                if (map.isMuro(x-=stepX, y)) {
                  setXY(x, y-=stepY);
                }
                else setXY(x-=stepX, y);
            } else setXY(x, y+=stepY);
        } else setXY(x+=stepX, y);



    }
}
