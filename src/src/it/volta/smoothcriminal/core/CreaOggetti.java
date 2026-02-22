package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;

import java.util.ArrayList;
import java.util.List;

public class CreaOggetti {

    public static List<Trappole> creaTrappole(Labirinto labirinto, Criminal criminal) {
        List<Coordinate>[] xyTrappole = labirinto.getTrappole();
        List<Trappole> trappole = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (Coordinate c : xyTrappole[i]) {
                Trappole t = null;
                // Use p.getX() and p.getY()
                if (i == 0) t = new Trappole(labirinto, criminal, c.getX(), c.getY(), "Sposta Uscita");
                else if (i == 1) t = new Trappole(labirinto, criminal, c.getX(), c.getY(), "Teleport");

                if (t != null) trappole.add(t);
            }
        }
        return trappole;
    }

    public static Gadget[] creaGadget(Labirinto labirinto, Criminal criminal){
        int[][] xyGadgets = labirinto.getGadget();
        Gadget[] gadgets = new Gadget[5];
        int contaGadget=0;
        for (int i=0; i<=4; i++) {
            Gadget gadget = null;
            if (xyGadgets[i][0] != 0 && xyGadgets[i][1] != 0) {
                switch (i) {
                    case 0:
                        gadget = new Gadget("distruggi mura", xyGadgets[0][0], xyGadgets[0][1], labirinto, criminal, 1);
                        break;
                    case 1:
                        gadget = new Gadget("salta mura", xyGadgets[1][0], xyGadgets[1][1], labirinto, criminal, 2);
                        break;
                    case 2:
                        gadget = new Gadget("muove diagonale", xyGadgets[2][0], xyGadgets[2][1], labirinto, criminal, 3);
                        break;
                    case 3:
                        gadget = new Gadget("bomba", xyGadgets[3][0], xyGadgets[3][1], labirinto, criminal, 4);
                        break;
                    case 4:
                        gadget = new Gadget("avvicina uscita", xyGadgets[4][0], xyGadgets[4][1], labirinto, criminal, 5);
                        break;
                    default:
                        break;
                }
                gadgets[contaGadget] = gadget;
                contaGadget++;
            }
        }
        /*for(int i=0;i<5;i++){
            for(int j=0;j<2;j++){
                System.out.print(xyGadgets[i][j] + " ");
            }
            System.out.println();
        }*/
        return gadgets;
    }

    //String nome, int x, int y, Labirinto labirinto, Criminal criminal



}
