package it.volta.smoothcriminal;

import it.volta.smoothcriminal.model.Labirinto;

public class Main {
    public static void main(String[] args) {
        Labirinto labirinto = new Labirinto();

        System.out.println(labirinto.isMuro(10,10));
    }
}
