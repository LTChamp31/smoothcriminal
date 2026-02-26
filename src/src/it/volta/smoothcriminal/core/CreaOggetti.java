package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code CreaOggetti} è una classe che crea
 * gli oggetti del gioco.
 * Si occupa di leggere i dati forniti dal {@link Labirinto} per istanziare
 * correttamente le liste di trappole e l'array di gadget necessari per la partita.
 *
 * * @author Marco Caria & Lotan Teny
 */
public class CreaOggetti {

    /**
     * Genera una lista di trappole basandosi sulle coordinate estratte dal labirinto.
     * Il metodo itera attraverso le diverse categorie di trappole definite nella mappa:
     * Indice 0: Trappola "Sposta Uscita"
     * Indice 1: Trappola "Teleport"
     *
     * @param labirinto L'istanza del {@link Labirinto} da cui estrarre le coordinate delle trappole.
     * @param criminal  L'istanza del {@link Criminal} a cui associare le trappole.
     * @return Una {@link List} di oggetti {@link Trappola} pronti per essere inseriti nel gioco.
     */
    public static List<Trappola> creaTrappole(Labirinto labirinto, Criminal criminal) {
        List<Coordinate>[] xyTrappole = labirinto.getTrappole();
        List<Trappola> trappola = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (Coordinate c : xyTrappole[i]) {
                Trappola t = null;
                if (i == 0) t = new Trappola(labirinto, criminal, c.getX(), c.getY(), "Sposta Uscita");
                else if (i == 1) t = new Trappola(labirinto, criminal, c.getX(), c.getY(), "Teleport");

                if (t != null) trappola.add(t);
            }
        }
        return trappola;
    }

    /**
     * Inizializza l'array di gadget disponibili nel livello corrente.
     * Ogni gadget viene creato con un nome specifico e un tasto di scelta rapida associato
     * in base al suo indice nel file di caricamento:
     * Indice 0: distruggi mura (Tasto 1)
     * Indice 1: salta mura (Tasto 2)
     * Indice 2: muove diagonale (Tasto 3)
     * Indice 3: bomba (Tasto 4)
     * Indice 4: avvicina uscita (Tasto 5)
     *
     * @param labirinto L'istanza del {@link Labirinto} contenente le posizioni dei gadget.
     * @param criminal  L'istanza del {@link Criminal} che potrà raccogliere i gadget.
     * @return Un array di {@link Gadget} contenente gli oggetti posizionati nella mappa.
     */
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
        return gadgets;
    }
}