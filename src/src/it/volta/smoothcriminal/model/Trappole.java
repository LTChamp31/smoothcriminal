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
        if (nome == "suscita") {

        }
    }


}
