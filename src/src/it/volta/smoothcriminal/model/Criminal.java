package it.volta.smoothcriminal.model;

import it.volta.smoothcriminal.core.Entita;

public class Criminal extends Entita{
    private boolean gadgetCriminal = false;
    public Criminal(int x, int y){
        super(x,y);
    }
    private Gadget[] gadgetUtilizzabili = new Gadget[3];
    private int contatore = 0;

    public boolean muovi(char dir, Labirinto mappa){
        //1 = sopra, 2 = sotto, 3 = destra, 4 = sinistra

        switch(Character.toLowerCase(dir)){
            case 'w':
                if(mappa.isMuro(x, y-1)){
                    return false;
                }
                setXY(x, y-1);
                break;
            case 's':
                if(mappa.isMuro(x, y+1)){
                    return false;
                }
                setXY(x, y+1);
                break;
            case 'd':
                if(mappa.isMuro(x+1, y)){
                    return false;
                }
                setXY(x+1, y);
                break;
            case 'a':
                if(mappa.isMuro(x-1, y)){
                    return false;
                }
                setXY(x-1, y);
                break;
        }
        return true;
    }

    public int getContatore(){return contatore;}

    public void aggiungiGadget(Gadget gadget){
        gadgetUtilizzabili[contatore] = gadget;
        System.out.println("Gadget Utilizzabili:");
        for(Gadget g : gadgetUtilizzabili){
            if(g!=null) System.out.println(g.getNome());
        }
        contatore++;
        gadgetCriminal = true;
    }

    public void rimuoviGadget(Gadget gadget){
        for(int i=0;i<3;i++){
            if(gadgetUtilizzabili[i]!=null && gadgetUtilizzabili[i].getNome()==gadget.getNome()){
                gadgetUtilizzabili[i] = null;
                contatore--;
            }
        }
        if(contatore==0){
            gadgetCriminal = false;
        }
    }

    public void tastiGadget(){
        for(Gadget g : gadgetUtilizzabili){
            if(g!=null){
                System.out.print(g.getTasto() + " ");
            }
        }
        System.out.println();
    }

    public Gadget[] getGadgetUtilizzabili(){
        return gadgetUtilizzabili;
    }

    public boolean getGadgetCriminal(){
        return gadgetCriminal;
    }
}
