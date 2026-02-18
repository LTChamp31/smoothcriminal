package it.volta.smoothcriminal.model;

public class Trappole extends Oggetti{

    /* idee trappole:
    1. muro che si muove
    2. zona precisa che sposta l'uscita
    3. zona precisa che ti teletrasporta in un punto random (non muro) lontano dall'uscita
    4.
     */

    public Trappole(Labirinto labirinto, Criminal criminal, int x, int y, String nome) {
        super(labirinto, criminal, x, y, nome);
    }


    public void usa() {
        System.out.println("Usa trappole");
        if (nome.equals("Sposta Uscita")) {
            System.out.println("Sposta Uscita");
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
            } while (!labirinto.isMuro(tx, ty));
        }
    }


}
