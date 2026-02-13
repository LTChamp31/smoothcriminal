package it.volta.smoothcriminal.model;
import it.volta.smoothcriminal.console.ConsoleUI;

public class Gadget extends Oggetti{

    private ConsoleUI input = new ConsoleUI();
    private int durata;
    public Gadget(String nome, int x, int y, Labirinto labirinto, Criminal criminal, int durata) {
        super(labirinto, criminal, x, y, nome);
        this.durata = durata;
    }

    public int getDurata(){
        return durata;
    }

    public void usa(String nome){
        char ris;
        switch(nome){
            case "distruggi mura":
                System.out.println("Seleziona una direzione in cui distrggere un muro: ");
                ris = input.leggiInput();
                switch(ris){
                    case 'w':
                        labirinto.distruggiMura(x,y-1);
                        break;
                    case 'a':
                        labirinto.distruggiMura(x-1,y);
                        break;
                    case 's':
                        labirinto.distruggiMura(x,y+1);
                        break;
                    case 'd':
                        labirinto.distruggiMura(x+1,y);
                        break;
                }
                break;
            case "salta mura":
                System.out.println("Seleziona una direzione in cui saltare un muro: ");
                ris = input.leggiInput();
                switch(ris){
                    case 'w':
                        if(labirinto.isMuro(x,y-2) || (y-2)>labirinto.getRighe()){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(x, y-2);
                        }
                        break;
                    case 'a':
                        if(labirinto.isMuro(x-2,y) || (x-2)>labirinto.getColonne()){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(x-2, y);
                        }
                        break;
                    case 's':
                        if(labirinto.isMuro(x,y+2) || (y+2)>labirinto.getRighe()){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(x, y+2);
                        }
                        break;
                    case 'd':
                        if(labirinto.isMuro(x+2,y) || (x+2)>labirinto.getColonne()){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(x+2, y);
                        }
                        break;
                }
                break;
            case "muove in diagonale":
                System.out.println("Seleziona una direzione in cui muoverti(Q, E, Z, C): ");
                ris = input.leggiInput('d');
                switch(ris){
                    case 'q':
                        if(labirinto.isMuro(x-1,y-1)){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(x-1, y-1);
                        }
                        break;
                    case 'e':
                        if(labirinto.isMuro(x+1,y-1)){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(x+1, y-1);
                        }
                        break;
                    case 'z':
                        if(labirinto.isMuro(x-1,y+1)){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(x-1, y+1);
                        }
                        break;
                    case 'c':
                        if(labirinto.isMuro(x+1,y+1)){
                            System.out.println("Impossibile saltare... gadget sprecato!!");
                        }
                        else{
                            criminal.setXY(x+1, y+1);
                        }
                        break;
                }
                break;
            case "bomba":
                labirinto.distruggiMura(x+1,y);
                labirinto.distruggiMura(x-1,y);
                labirinto.distruggiMura(x,y+1);
                labirinto.distruggiMura(x,y-1);
                break;
            case "avvicina uscita":
                labirinto.avvicinaUscita(criminal.getX(), criminal.getY(), labirinto.getUscitaX(), labirinto.getUscitaY());
                break;
        }
    }



    /*idee per gadget:
    1. distruggi mura
    2. salta mura
    3. si muove in diagonale
    4. bomba,fa esplodere un muro per ogni direzione
    5. Ti fa vedere tutta la mappa per tot secondi(per quando è oscurata) DOPO MAPPA OSCURATA ORA NO 13/02/2025
    6. ti avvicina l'uscita
    7.


     */
}
