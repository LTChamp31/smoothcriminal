package it.volta.smoothcriminal.model;

public class Gadget extends Oggetto {
    private int tasto;
    private boolean raccolto = false;

    public Gadget(String nome, int x, int y, Labirinto labirinto, Criminal criminal, int tasto) {
        super(labirinto, criminal, x, y, nome);
        this.tasto = tasto;
    }

    @Override
    public void usa(char direzione) {
        int cx = criminal.getX();
        int cy = criminal.getY();

        switch (getNome().toLowerCase()) {
            case "distruggi mura":
                azioneDistruggi(direzione, cx, cy);
                break;
            case "salta mura":
                azioneSalta(direzione, cx, cy);
                break;
            case "muove diagonale":
                azioneDiagonale(direzione, cx, cy);
                break;
            case "bomba":
                azioneBomba(cx, cy);
                break;
            case "avvicina uscita":
                labirinto.avvicinaUscita(cx, cy, labirinto.getUscitaX(), labirinto.getUscitaY());
                break;
        }
    }

    private void azioneDistruggi(char dir, int cx, int cy) {
        if (dir == 'w') labirinto.cancellaCarattere(cx, cy - 1);
        else if (dir == 's') labirinto.cancellaCarattere(cx, cy + 1);
        else if (dir == 'a') labirinto.cancellaCarattere(cx - 1, cy);
        else if (dir == 'd') labirinto.cancellaCarattere(cx + 1, cy);
    }

    private void azioneSalta(char dir, int cx, int cy) {
        int nx = cx, ny = cy;
        if (dir == 'w') ny -= 2;
        else if (dir == 's') ny += 2;
        else if (dir == 'a') nx -= 2;
        else if (dir == 'd') nx += 2;

        if (!labirinto.isMuro(nx, ny) && ny >= 0 && ny < labirinto.getRighe() && nx >= 0 && nx < labirinto.getColonne()) {
            criminal.setXY(nx, ny);
        }
    }

    private void azioneDiagonale(char dir, int cx, int cy) {
        int nx = cx, ny = cy;
        if (dir == 'q') { nx--; ny--; }
        else if (dir == 'e') { nx++; ny--; }
        else if (dir == 'z') { nx--; ny++; }
        else if (dir == 'c') { nx++; ny++; }

        if (!labirinto.isMuro(nx, ny)) criminal.setXY(nx, ny);
    }

    private void azioneBomba(int cx, int cy) {
        labirinto.cancellaCarattere(cx + 1, cy);
        labirinto.cancellaCarattere(cx - 1, cy);
        labirinto.cancellaCarattere(cx, cy + 1);
        labirinto.cancellaCarattere(cx, cy - 1);
    }

    public int getTasto() { return tasto; }
    public boolean getRaccolto() { return raccolto; }
    public void setRaccolto(boolean raccolto) { this.raccolto = raccolto; }
}