package it.volta.smoothcriminal.model;
import it.volta.smoothcriminal.console.ConsoleUI;

public class Gadget extends Oggetti{
    int tasto;
    private ConsoleUI input = new ConsoleUI();
    public Gadget(String nome, int x, int y, Labirinto labirinto, Criminal criminal, int tasto) {
        super(labirinto, criminal, x, y, nome);
        this.tasto = tasto;
    }

    public void usa(){
        char ris;
        int cx = criminal.getX();
        int cy = criminal.getY();
        switch(nome){
            case "distruggi mura":
                System.out.println("Seleziona una direzione in cui distruggere un muro: ");
                ris = input.leggiInput();
                switch(ris){
                    case 'w':
                        labirinto.cancellaCarattere(cx,cy-1);
                        break;
                    case 'a':
                        labirinto.cancellaCarattere(cx-1,cy);
                        break;
                    case 's':
                        labirinto.cancellaCarattere(cx,cy+1);
                        break;
                    case 'd':
                        labirinto.cancellaCarattere(cx+1,cy);
                        break;
                }
                break;
            case "salta mura":
                System.out.println("Seleziona una direzione in cui saltare un muro: ");
                ris = input.leggiInput();
                switch(ris){
                    case 'w':
                        if(labirinto.isMuro(cx,cy-2) || (cy-2)>labirinto.getRighe()){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(cx, cy-2);
                        }
                        break;
                    case 'a':
                        if(labirinto.isMuro(cx-2,cy) || (cx-2)>labirinto.getColonne()){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(cx-2, cy);
                        }
                        break;
                    case 's':
                        if(labirinto.isMuro(cx,cy+2) || (cy+2)>labirinto.getRighe()){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(cx, cy+2);
                        }
                        break;
                    case 'd':
                        if(labirinto.isMuro(cx+2,cy) || (cx+2)>labirinto.getColonne()){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(cx+2, cy);
                        }
                        break;
                }
                break;
            case "muove diagonale":
                System.out.println("Seleziona una direzione in cui muoverti(Q, E, Z, C): ");
                ris = input.leggiInputDiagonale();
                switch(ris){
                    case 'q':
                        if(labirinto.isMuro(cx-1,cy-1)){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(cx-1, cy-1);
                        }
                        break;
                    case 'e':
                        if(labirinto.isMuro(cx+1,cy-1)){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(cx+1, cy-1);
                        }
                        break;
                    case 'z':
                        if(labirinto.isMuro(cx-1,cy+1)){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(cx-1, cy+1);
                        }
                        break;
                    case 'c':
                        if(labirinto.isMuro(cx+1,cy+1)){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(cx+1, cy+1);
                        }
                        break;
                }
                break;
            case "bomba":
                labirinto.cancellaCarattere(cx+1,cy);
                labirinto.cancellaCarattere(cx-1,cy);
                labirinto.cancellaCarattere(cx,cy+1);
                labirinto.cancellaCarattere(cx,cy-1);
                break;
            case "avvicina uscita":
                labirinto.avvicinaUscita(criminal.getX(), criminal.getY(), labirinto.getUscitaX(), labirinto.getUscitaY());
                break;
        }
    }

    public int getTasto(){
        return tasto;
    }


    /*idee per gadget:
    1. distruggi mura (G) 1
    2. salta mura  (J) 2
    3. si muove in diagonale (D) 3
    4. bomba,fa esplodere un muro per ogni direzione (B) 4
    5. Ti fa vedere tutta la mappa per tot secondi(per quando è oscurata) DOPO MAPPA OSCURATA ORA NO 13/02/2025
    6. ti avvicina l'uscita (Y) 5
    7.


     */
}
