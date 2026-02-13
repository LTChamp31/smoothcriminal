package it.volta.smoothcriminal.model;
import java.util.ArrayList;
import java.util.List;

public class Labirinto {
    private final int inizioX;
    private final int inizioY, colonne;
    private int uscitaY, uscitaX;
    private List<List<Character>> mat;
    private final List<Integer> xyTrappole;
    private Trappole trappla = new Trappole();
    private Gadget gadget = new Gadget();
    public Labirinto(List<List<Character>> mat, int inizioX, int inizioY, int colonne, int uscitaX, int UscitaY, List<Integer> xyTrappole) {
        this.mat = mat;
        this.inizioX = inizioX;
        this.inizioY = inizioY;
        this.colonne = colonne;
        this.uscitaX = uscitaX;
        this.uscitaY = uscitaY;
        this.xyTrappole = xyTrappole;
    }


    public boolean isMuro(int x, int y) {
        if (y < 0 || y >= mat.size() || x < 0 || x >= mat.get(y).size()) {
            return true;
        }
        return mat.get(y).get(x) == '█';
    }

    public boolean isUscita(int x, int y) {
        if (mat.get(y).get(x) == 'U') return true;
        return false;
    }

    public String mappa(int x, int y) {
        String map = "";
        for (int i=0; i< mat.size(); i++) {
            List<Character> row = mat.get(i); // Get the current row
            for (int j=0; j< colonne; j++) {
                if (i==y && j==x) map += "ℜ";
                else if (j < row.size()) {
                    map += row.get(j);
                }
                else {
                    // If the row is shorter than the maximum width, add a space
                    map += " ";
                }
            }
            map += "\n";
        }
        return map;
    }

    public void distruggiMura(int x, int y){
        mat.get(y).set(x, ' ');
    }

    public void spostaUscita() {
        mat.get(uscitaY).set(uscitaX, ' ');
        int ux, uy;
        do {
            ux = (int) (Math.random() * colonne);
            uy = (int) (Math.random() * mat.size());
            if (!isMuro(ux, uy)) {
                uscitaX = ux;
                uscitaY = uy;
            }
        } while (!isMuro(ux, uy));
    }

    public void avvicinaUscita(int xc, int yc, int xu, int yu) {
        int stepX = Integer.compare(xc, xu);
        int stepY = Integer.compare(yc, yu);

        uscitaX += stepX;
        uscitaY += stepY;
    }

    public int getInizioX() {
        return inizioX;
    }

    public int getInizioY() {
        return inizioY;
    }

    public int getUscitaX() { return uscitaX; }

    public int getUscitaY() { return uscitaY; }

    public void controllaTrappole(int x, int y) {
        if (xyTrappole.contains(x)) {
            if (xyTrappole.contains(y)) {
                trappole
            }
        }
    }

    public void controllaGadget(int x, int y){
        if (mat.get(y).get(x) == 'D') gadget.usa("distruggi mura");
    }
    //Dopo
    public int getColonne() {return colonne;}

    public int getRighe() {return mat.size();}



}
