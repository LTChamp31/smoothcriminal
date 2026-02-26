package it.volta.smoothcriminal.model;

public class Nemico extends Entita {
    private char carattereSotto = ' '; // Memoria di cosa c'è sotto il nemico

    public Nemico(int x, int y) {
        super(x, y);
    }

    public void muovi(Labirinto mappa, Criminal criminal) {
        mappa.setCarattere(x, y, carattereSotto);

        int criminalX = criminal.getX();
        int criminalY = criminal.getY();

        int stepX = Integer.compare(criminalX, x);
        int stepY = Integer.compare(criminalY, y);

        int nuovaX = x;
        int nuovaY = y;

        if (Math.abs(criminalX - x) > Math.abs(criminalY - y)) {
            if (stepX != 0 && !mappa.isMuro(x + stepX, y)) nuovaX += stepX;
            else if (stepY != 0 && !mappa.isMuro(x, y + stepY)) nuovaY += stepY;
        } else {
            if (stepY != 0 && !mappa.isMuro(x, y + stepY)) nuovaY += stepY;
            else if (stepX != 0 && !mappa.isMuro(x + stepX, y)) nuovaX += stepX;
        }

        setXY(nuovaX, nuovaY);

        char nuovoCarattereSotto = mappa.getCarattere(x, y);
        if (nuovoCarattereSotto != 'Ⓒ') {
            carattereSotto = nuovoCarattereSotto;
        } else {
            carattereSotto = ' ';
        }

        mappa.setCarattere(x, y, 'Ⓝ');
    }
}