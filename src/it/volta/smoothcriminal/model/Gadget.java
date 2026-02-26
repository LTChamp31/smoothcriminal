package it.volta.smoothcriminal.model;

/**
 * La classe {@code Gadget} rappresenta un oggetto collezionabile che conferisce
 * abilità o azioni al {@link Criminal}.
 * Ogni gadget è caratterizzato da un nome che ne determina l'effetto e da un tasto
 * numerico per l'attivazione. La classe gestisce internamente diverse tipologie di
 * azioni, come la distruzione di muri, il salto di ostacoli o l'uso di esplosivi.
 *
 * * @author Marco Caria & Lotan Teny
 */
public class Gadget extends Objects {

    private int tasto;

    private boolean raccolto = false;

    /**
     * Costruisce un nuovo Gadget con i parametri specificati.
     *
     * @param nome      Il nome identificativo del gadget
     * @param x         La coordinata X iniziale sulla mappa.
     * @param y         La coordinata Y iniziale sulla mappa.
     * @param maze Il riferimento al {@link Maze} per le interazioni ambientali.
     * @param criminal  Il riferimento al {@link Criminal} che utilizzerà il gadget.
     * @param tasto     Il tasto numerico assegnato per l'attivazione.
     */
    public Gadget(String nome, int x, int y, Maze maze, Criminal criminal, int tasto) {
        super(maze, criminal, x, y, nome);
        this.tasto = tasto;
    }

    /**
     * Esegue l'azione del gadget in base al suo nome.
     * Questo metodo implementa il comportamento dei gadget, smistando
     * l'esecuzione verso metodi o interagendo direttamente con
     * il maze e il giocatore.
     * @param direzione Il carattere della direzione scelta dall'utente (es. 'w', 'a', 's', 'd' o 'q', 'e', 'z', 'c').
     */
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
                maze.avvicinaUscita(cx, cy, maze.getUscitaX(), maze.getUscitaY());
                break;
        }
    }

    /**
     * Rimuove un muro adiacente al giocatore nella direzione specificata.
     *
     * @param dir La direzione del muro da distruggere ('w', 'a', 's', 'd').
     * @param cx  Coordinata X attuale del criminale.
     * @param cy  Coordinata Y attuale del criminale.
     */
    private void azioneDistruggi(char dir, int cx, int cy) {
        if (dir == 'w') maze.cancellaCarattere(cx, cy - 1);
        else if (dir == 's') maze.cancellaCarattere(cx, cy + 1);
        else if (dir == 'a') maze.cancellaCarattere(cx - 1, cy);
        else if (dir == 'd') maze.cancellaCarattere(cx + 1, cy);
    }

    /**
     * Permette al giocatore di saltare un ostacolo (muro) spostandosi di due unità.
     * Il salto avviene solo se la destinazione è libera e dentro ai confini della mappa.
     *
     * @param dir La direzione del salto.
     * @param cx  Coordinata X attuale del criminale.
     * @param cy  Coordinata Y attuale del criminale.
     */
    private void azioneSalta(char dir, int cx, int cy) {
        int nx = cx, ny = cy;
        if (dir == 'w') ny -= 2;
        else if (dir == 's') ny += 2;
        else if (dir == 'a') nx -= 2;
        else if (dir == 'd') nx += 2;

        if (!maze.isMuro(nx, ny) && ny >= 0 && ny < maze.getRighe() && nx >= 0 && nx < maze.getColonne()) {
            criminal.setXY(nx, ny);
        }
    }

    /**
     * Esegue uno spostamento diagonale di una singola unità.
     *
     * @param dir Il tasto della diagonale ('q', 'e', 'z', 'c').
     * @param cx  Coordinata X attuale del criminale.
     * @param cy  Coordinata Y attuale del criminale.
     */
    private void azioneDiagonale(char dir, int cx, int cy) {
        int nx = cx, ny = cy;
        if (dir == 'q') { nx--; ny--; }
        else if (dir == 'e') { nx++; ny--; }
        else if (dir == 'z') { nx--; ny++; }
        else if (dir == 'c') { nx++; ny++; }

        if (!maze.isMuro(nx, ny)) criminal.setXY(nx, ny);
    }

    /**
     * Distrugge tutti i muri immediatamente adiacenti (sopra, sotto, destra, sinistra) al giocatore.
     *
     * @param cx Coordinata X attuale del criminale.
     * @param cy Coordinata Y attuale del criminale.
     */
    private void azioneBomba(int cx, int cy) {
        maze.cancellaCarattere(cx + 1, cy);
        maze.cancellaCarattere(cx - 1, cy);
        maze.cancellaCarattere(cx, cy + 1);
        maze.cancellaCarattere(cx, cy - 1);
    }

    /** @return Il tasto associato al gadget. */
    public int getTasto() { return tasto; }

    /** @return {@code true} se il gadget è stato raccolto. */
    public boolean getRaccolto() { return raccolto; }

    /** @param raccolto Imposta lo stato di raccolta del gadget. */
    public void setRaccolto(boolean raccolto) { this.raccolto = raccolto; }
}