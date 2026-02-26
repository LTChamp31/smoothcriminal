package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.model.*;
import java.io.*;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * La classe ConsoleUI gestisce l'intera interfaccia CLI del gioco.
 * Si occupa dell'interazione diretta con l'utente e della lettura dell'input,
 * della validazione dei dati e della renderizzazione della mappa su console.
 * * @author Marco Caria & Lotan Teny
 */
public class ConsoleUI {

    private Scanner input = new Scanner(System.in);
    /**
     * Costruttore della classe ConsoleUI.
     */
    public ConsoleUI() {
    }

    /**
     * Chiede all'utente di inserire il proprio nome.
     * Utilizza una pulizia del buffer per evitare problemi con letture precedenti.
     * * @return Il nome inserito dall'utente come {@code String}.
     */
    public String scegliNome() {
        System.out.println("Inserisci il tuo nome:");
        input.nextLine();
        return input.nextLine();
    }

    /**
     * Mostra il menu principale e permette all'utente di scegliere la modalità di gioco.
     * Gestisce le eccezioni di tipo {@link InputMismatchException} per evitare crash
     * in caso di inserimento di caratteri non numerici.
     * * @return Un intero rappresentante la modalità scelta (1-4).
     */
    public int scegliModalita() {
        int ris = 0;
        boolean inputValido = false;

        while (!inputValido) {
            try {
                stampaMenu();
                System.out.print("Inserisci la tua scelta: ");
                ris = input.nextInt();

                if (ris >= 1 && ris <= 4) {
                    inputValido = true;
                } else {
                    System.out.println("Scelta non valida. Inserisci un numero tra 1 e 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Errore: devi inserire un numero!");
                input.next();
            }
        }
        return ris;
    }

    /**
     * Gestisce la scelta iniziale dell'interfaccia di gioco (Console o GUI).
     * Implementa un controllo degli errori per garantire l'inserimento di un valore valido.
     * * @return L'opzione scelta dall'utente (1 per Console, 2 per GUI).
     */
    public int scegliInterfaccia() {
        int ris = 0;
        boolean inputValido = false;

        System.out.println("Seleziona la modalità di visualizzazione:");
        System.out.println("1) Giocare su Console");
        System.out.println("2) Giocare su GUI (Interfaccia Grafica)");

        while (!inputValido) {
            try {
                System.out.print("Scelta: ");
                ris = input.nextInt();

                if (ris == 1 || ris == 2) {
                    inputValido = true;
                } else {
                    System.out.println("Scelta non valida. Inserisci 1 o 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Errore: Inserisci un numero intero!");
                input.next();
            }
        }
        return ris;
    }

    /**
     * Esegue il rendering grafico del maze su console.
     * Pulisce lo schermo e posiziona il cursore in alto a sinistra.
     * * @param maze L'oggetto {@link Maze} da visualizzare.
     * @param criminal L'oggetto {@link Criminal} per ottenere le coordinate attuali del giocatore.
     */
    public void render(Maze maze, Criminal criminal) {
        System.out.println();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(maze.mappa(criminal.getX(), criminal.getY()));
    }

    /**
     * Legge e valida l'input per il movimento o l'uso di gadget durante il gioco.
     * Accetta (W, A, S, D), tasti numerici per i gadget (1-5) e 'X' per uscire.
     * * @return Il carattere validato inserito dall'utente.
     */
    public char leggiInput() {
        boolean ok;
        char move;
        do {
            ok = false;
            move = Character.toLowerCase(input.next().charAt(0));
            if (move == 'w' || move == 'a' || move == 's' || move == 'd') {
                ok = true;
            }
            else if (move == '1' || move == '2' || move == '3' || move == '4' || move == '5') {
                ok = true;
            }
            else if (move == 'x'){
                ok = true;
            }
            else System.out.print("Mossa non valida, riprova:");
        } while (!ok);
        return move;
    }

    /**
     * Chiede all'utente la direzione in cui utilizzare un gadget specifico.
     * * @param nomeGadget Il nome del gadget in uso.
     * @return Il carattere della direzione scelta (W, A, S, D).
     */
    public char chiediDirezioneGadget(String nomeGadget) {
        System.out.println("Uso di " + nomeGadget + ". Scegli direzione (W,A,S,D):");
        return leggiInput();
    }

    /**
     * Legge e valida l'input per i movimenti diagonali richiesti da alcuni gadget.
     * Accetta i tasti Q (alto-sx), E (alto-dx), Z (basso-sx), C (basso-dx).
     * * @param nomeGadget Il nome del gadget che richiede il movimento diagonale.
     * @return Il carattere della direzione diagonale scelta.
     */
    public char leggiInputDiagonale(String nomeGadget) {
        boolean ok;
        char move;
        do {
            ok = false;
            System.out.println("Uso di " + nomeGadget + ". Scegli direzione diagonale (Q,E,Z,C):");
            move = Character.toLowerCase(input.next().charAt(0));
            if (move == 'q' || move == 'e' || move == 'z' || move == 'c') {
                ok = true;
            } else System.out.print("Mossa non valida, riprova:");
        } while (!ok);
        return move;
    }

    /**
     * Chiede all'utente se desidera riprendere dal livello salvato o ricominciare da capo.
     * * @param livelloSalvato Il numero del livello raggiunto precedentemente.
     * @return 1 per riprendere, 2 per ricominciare.
     */
    public int leggiInputLivelloSalvato(int livelloSalvato) {
        boolean ok;
        int risposta;
        do {
            ok = false;
            System.out.println("Hai raggiunto il livello " + livelloSalvato);
            System.out.println("1) Riprendi");
            System.out.println("2) Ricomincia");
            risposta = input.nextInt();
            if (risposta == 1 || risposta == 2) {
                ok = true;
            } else System.out.print("risposta non valida, riprova:");
        } while (!ok);
        return risposta;
    }

    /**
     * Legge il file del menu principale e lo stampa a video.
     */
    public void stampaMenu() {
        File file = new File("src/resources/schermate/home.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del menu.");
        }
    }
}