package it.volta.smoothcriminal.model;
import java.util.ArrayList;
import java.util.List;

public class Labirinto {
    private final int inizioX;
    private final int inizioY, colonne;
    private int uscitaY, uscitaX;
    private List<List<Character>> mat;
    private final List<Integer>[] xyTrappole;
    private final int[][] xyGadgets;
    private Trappole[] trappole;

    public Labirinto(List<List<Character>> mat, int inizioX, int inizioY, int colonne, int uscitaX, int UscitaY, List<Integer>[] xyTrappole, int[][] xyGadgets) {
        this.mat = mat;
        this.inizioX = inizioX;
        this.inizioY = inizioY;
        this.colonne = colonne;
        this.uscitaX = uscitaX;
        this.uscitaY = uscitaY;
        this.xyTrappole = xyTrappole;
        this.xyGadgets = xyGadgets;
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

    public void spostaUscita(int ux, int uy) {
        mat.get(uscitaY).set(uscitaX, ' ');
        uscitaX = ux;
        uscitaY = uy;
        mat.get(uscitaY).set(uscitaX, 'U');
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

    public List<Integer>[] getTrappole() {
        return xyTrappole;
    }

    public int[][] getGadgets() {
        return xyGadgets;
    }

    //Dopo
    public int getColonne() {return colonne;}

    public int getRighe() {return mat.size();}



}
