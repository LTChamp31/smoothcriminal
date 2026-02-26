package it.volta.smoothcriminal.model;

public class Trappola extends Oggetto {

    public Trappola(Labirinto labirinto, Criminal criminal, int x, int y, String nome) {
        super(labirinto, criminal, x, y, nome);
    }

    public void usa(char direzione) {
        if (nome.equals("Sposta Uscita")) {
            int ux, uy;
            do {
                ux = (int) (Math.random() * labirinto.getColonne());
                uy = (int) (Math.random() * labirinto.getRighe());
                if (!labirinto.isMuro(ux, uy)) {
                   labirinto.spostaUscita(ux, uy);
                }
            } while (labirinto.isMuro(ux, uy));

        } else if (nome.equals("Teleport")) {
            int tx, ty;
            do {
                tx = (int) (Math.random() * labirinto.getColonne());
                ty = (int) (Math.random() * labirinto.getRighe());
                if (!labirinto.isMuro(tx, ty)) {
                    criminal.setXY(tx, ty);
                }
            } while (labirinto.isMuro(tx, ty));
        }
    }


}
