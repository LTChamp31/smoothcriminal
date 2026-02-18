package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;

import java.util.List;

public class CreaOggetti {
    Criminal criminal;
    Labirinto labirinto;
    Trappole trappola;
    Gadget distruggiMura;
    private List<Integer>[] xyTrappole;
    private Trappole[] trappole = new Trappole[5];
    private int[][] xyGadgets;
    private Gadget[] gadgets = new Gadget[3];
    private int contaTrappole=0, contaGadget=0;

    public CreaOggetti(Criminal criminal, Labirinto labirinto) {
        this.criminal = criminal;
        this.labirinto = labirinto;
    }

    public void creaTrappole() {
        xyTrappole = labirinto.getTrappole();
        for (int i=0; i<3; i++) {
            for (int j=0; j<xyTrappole[i].size(); j+=2) {
                if (i==0) {
                    trappola = new Trappole(labirinto, criminal, xyTrappole[0].get(j), xyTrappole[0].get(j++), "Sposta Uscita");
                    trappole[contaTrappole] = trappola;
                    System.out.println(trappole[contaTrappole].getNome());
                    contaTrappole++;
                } else if (i==1) {
                    trappola = new Trappole(labirinto, criminal, xyTrappole[1].get(j), xyTrappole[1].get(j++), "Teleport");
                    trappole[contaTrappole] = trappola;
                    System.out.println(trappole[contaTrappole].getNome());
                    contaTrappole++;
                } else {
                    break;
                }
            }
        }

    }

    public void creaGadget(){
        xyGadgets = labirinto.getGadgets();

        if(xyGadgets[0][0] != 0 && xyGadgets[0][1] != 0){
            Gadget distruggiMura = new Gadget("distruggi mura", xyGadgets[0][0], xyGadgets[0][1], labirinto, criminal);
            gadgets[contaGadget] = distruggiMura;
            contaGadget++;
        }
        if(xyGadgets[1][0] != 0 && xyGadgets[1][1] != 0){
            Gadget saltaMura = new Gadget("salta mura", xyGadgets[1][0], xyGadgets[1][1], labirinto, criminal);
            gadgets[contaGadget] = saltaMura;
            contaGadget++;
        }
        if(xyGadgets[2][0] != 0 && xyGadgets[2][1] != 0){
            Gadget muoveDiagonale = new Gadget("muove diagonale", xyGadgets[2][0], xyGadgets[2][1], labirinto, criminal);
            gadgets[contaGadget] = muoveDiagonale;
            contaGadget++;
        }
        if(xyGadgets[3][0] != 0 && xyGadgets[3][1] != 0){
            Gadget bomba = new Gadget("bomba", xyGadgets[3][0], xyGadgets[3][1], labirinto, criminal);
            gadgets[contaGadget] = bomba;
            contaGadget++;
        }
        if(xyGadgets[4][0] != 0 && xyGadgets[4][1] != 0){
            Gadget avvicinaUscita = new Gadget("avvicina uscita", xyGadgets[4][0], xyGadgets[4][1], labirinto, criminal);
            gadgets[contaGadget] = avvicinaUscita;
            contaGadget++;
        }
    }

    public Trappole[] getTrappole() {
        return trappole;
    }
    //String nome, int x, int y, Labirinto labirinto, Criminal criminal



}
