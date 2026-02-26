package it.volta.smoothcriminal.model;

/**
 * La classe {@code Criminal} rappresenta il personaggio controllato dal giocatore.
 * Estende {@link Entity} per la gestione della posizione (X, Y) e implementa
 * un sistema di inventario limitato a tre slot per la gestione dei {@link Gadget}.
 * Gestisce inoltre le collisioni con i muri durante il movimento nel {@link Maze}.
 * * @author Marco Caria & Lotan Teny
 */
public class Criminal extends Entity {

    private boolean gadgetCriminal = false;
    private Gadget[] gadgetUtilizzabili = new Gadget[3];
    private int numeroGadget = 0;

    /**
     * Costruttore della classe Criminal.
     * Inizializza la posizione iniziale del giocatore nel maze.
     * * @param x Coordinata X iniziale (colonna).
     * @param y Coordinata Y iniziale (riga).
     */
    public Criminal(int x, int y){
        super(x,y);
    }

    /**
     * Gestisce lo spostamento del giocatore nel maze in base alla direzione fornita.
     * Il metodo verifica se la cella di destinazione è un muro tramite {@link Maze#isMuro(int, int)}:
     * Se la via è libera, aggiorna le coordinate del giocatore e restituisce {@code true}.
     * Se è presente un muro, il movimento viene annullato e restituisce {@code false}.
     * * @param dir Il carattere della direzione ('w' per su, 'a' per sinistra, 's' per giù, 'd' per destra).
     * @param mappa Il {@link Maze} corrente per il controllo delle collisioni.
     * @return {@code true} se il movimento è avvenuto con successo, {@code false} se bloccato da un muro.
     */
    public boolean muovi(char dir, Maze mappa){
        switch(Character.toLowerCase(dir)){
            case 'w':
                if(mappa.isMuro(x, y-1)) return false;
                setXY(x, y-1);
                break;
            case 's':
                if(mappa.isMuro(x, y+1)) return false;
                setXY(x, y+1);
                break;
            case 'd':
                if(mappa.isMuro(x+1, y)) return false;
                setXY(x+1, y);
                break;
            case 'a':
                if(mappa.isMuro(x-1, y)) return false;
                setXY(x-1, y);
                break;
        }
        return true;
    }

    /**
     * Aggiunge un gadget all'inventario se è presente almeno uno slot libero (massimo 3).
     * Dopo l'aggiunta, stampa a console l'elenco aggiornato dei nomi dei gadget posseduti.
     * Imposta {@code gadgetCriminal} a {@code true}.
     * * @param gadget L'oggetto {@link Gadget} da aggiungere all'inventario.
     */
    public void aggiungiGadget(Gadget gadget){
        if (numeroGadget < 3) {
            gadgetUtilizzabili[numeroGadget] = gadget;
            numeroGadget++;
        }
        System.out.println("Gadget Utilizzabili:");
        for(Gadget g : gadgetUtilizzabili){
            if(g!=null) System.out.println(g.getNome());
        }
        gadgetCriminal = true;
    }

    /**
     * Rimuove un gadget specifico dall'inventario cercando per corrispondenza di nome.
     * Se l'inventario si svuota completamente, imposta il flag {@code gadgetCriminal} a {@code false}.
     *
     * * @param gadget L'oggetto {@link Gadget} da rimuovere (viene confrontato il nome).
     */
    public void rimuoviGadget(Gadget gadget){
        for(int i=0;i<3;i++){
            if(gadgetUtilizzabili[i]!=null && gadgetUtilizzabili[i].getNome().equals(gadget.getNome())){
                gadgetUtilizzabili[i] = null;
                numeroGadget--;
            }
        }
        if(numeroGadget==0){
            gadgetCriminal = false;
        }
    }

    /**
     * Stampa a video i tasti di attivazione dei gadget attualmente presenti nell'inventario.
     */
    public void mostraTastiGadget(){
        for(Gadget g : gadgetUtilizzabili){
            if(g!=null){
                System.out.print(g.getTasto() + " ");
            }
        }
        System.out.println();
    }

    /**
     * Restituisce l'array dei gadget attualmente posseduti.
     * * @return Un array di oggetti {@link Gadget}.
     */
    public Gadget[] getGadgetUtilizzabili(){
        return gadgetUtilizzabili;
    }

    /**
     * Restituisce lo stato di possesso dei gadget
     * * @return {@code true} se il giocatore ha almeno un gadget, {@code false} altrimenti.
     */
    public boolean getGadgetCriminal(){
        return gadgetCriminal;
    }

}