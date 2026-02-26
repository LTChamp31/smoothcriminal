package it.volta.smoothcriminal.model;

public class Criminal extends Entita{
    private boolean gadgetCriminal = false;
    private Gadget[] gadgetUtilizzabili = new Gadget[3];
    private int numeroGadget = 0;

    public Criminal(int x, int y){
        super(x,y);
    }
    public boolean muovi(char dir, Labirinto mappa){
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

    public void mostraTastiGadget(){
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
