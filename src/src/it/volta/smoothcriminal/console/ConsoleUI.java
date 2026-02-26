package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.model.*;
import java.io.*;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ConsoleUI {
    private Scanner input = new Scanner(System.in);

    public ConsoleUI() {
    }

    public String scegliNome() {
        System.out.println("Inserisci il tuo nome:");
        input.nextLine();
        return input.nextLine();
    }

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


    public void render(Labirinto labirinto, Criminal criminal) {
        System.out.println();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(labirinto.mappa(criminal.getX(), criminal.getY()));
    }

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

    public char chiediDirezioneGadget(String nomeGadget) {
        System.out.println("Uso di " + nomeGadget + ". Scegli direzione (W,A,S,D):");
        return leggiInput();
    }

    public char leggiInputDiagonale(String nomeGadget) {
        boolean ok;
        char move;
        do {
            ok = false;
            System.out.println("Uso di " + nomeGadget + ". Scegli direzione (W,A,S,D):");
            move = Character.toLowerCase(input.next().charAt(0));
            if (move == 'q' || move == 'e' || move == 'z' || move == 'c') {
                ok = true;
            } else System.out.print("Mossa non valida, riprova:");
        } while (!ok);
        return move;
    }

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
