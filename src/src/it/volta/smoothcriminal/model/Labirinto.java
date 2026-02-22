package it.volta.smoothcriminal.model;
import java.util.List;

public class Labirinto {
    private final int inizioX;
    private final int inizioY, colonne;
    private int uscitaY, uscitaX;
    private List<List<Character>> mat;
    private List<Coordinate>[] xyTrappole;
    private int[][] xyGadget;
    private List<Nemici> nemici;


    public Labirinto(List<List<Character>> mat, int inizioX, int inizioY, int colonne, int uscitaX, int uscitaY, List<Coordinate>[] xyTrappole, int[][] xyGadget, List<Nemici> nemici) {
        this.mat = mat;
        this.inizioX = inizioX;
        this.inizioY = inizioY;
        this.colonne = colonne;
        this.uscitaX = uscitaX;
        this.uscitaY = uscitaY;
        this.xyTrappole = xyTrappole;
        this.xyGadget = xyGadget;
        this.nemici = nemici;
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
        StringBuilder map = new StringBuilder();
        for (int i=0; i< mat.size(); i++) {
            List<Character> row = mat.get(i); // Get the current row
            for (int j=0; j< colonne; j++) {
                if (i==y && j==x) map.append("Ⓒ");
                else if (j < row.size()) {
                    map.append(row.get(j));
                }
                else {
                    map.append(" ");
                }
            }
            map.append("\n");
        }
        return map.toString();
    }

    public void cancellaCarattere(int x, int y){
        mat.get(y).set(x, ' ');
    }

    public void spostaUscita(int ux, int uy) {
        mat.get(this.uscitaY).set(this.uscitaX, ' ');
        this.uscitaX = ux;
        this.uscitaY = uy;
        mat.get(this.uscitaY).set(this.uscitaX, 'U');
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

    public int[][] getGadget(){return xyGadget;}

    public List<Coordinate>[] getTrappole() {
        return xyTrappole;
    }

    public int getColonne() {return colonne;}

    public int getRighe() {return mat.size();}



}
