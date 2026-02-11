package it.volta.smoothcriminal.model;
import it.*;

public class Gadget extends Oggetti{

    private int durata;
    public Gadget(String nome, int x, int y, Labirinto labirinto, Criminal criminal, int durata) {
        super(nome,x,y, labirinto, criminal);
        this.durata = durata;
    }

    public int getDurata(){
        return durata;
    }

    public void usa(){

    }




    /*idee per gadget:
    1. distruggi mura
    2.salta mura
    3. si muove in diagonale
    4. bomba,fa esplodere un muro per ogni direzione
    5. Ti fa vedere tutta la mappa per tot secondi(per quando è oscurata)
    6. ti indica la via per tot secondi (da pensarci bene)


     */
}
