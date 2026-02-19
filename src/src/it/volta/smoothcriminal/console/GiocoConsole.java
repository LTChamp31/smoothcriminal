package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.CreaOggetti;
import it.volta.smoothcriminal.core.Videogioco;
import it.volta.smoothcriminal.model.*;
import java.util.*;
import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GiocoConsole extends Videogioco {
    ConsoleUI ui;
    GameLoop loop;
    LevelLoader loader = new LevelLoader();
    Gadget[] gadget = new Gadget[6];
    List<Trappole> trappole;

    public GiocoConsole(Criminal criminal, Labirinto labirinto) {
        super(criminal, labirinto);
        this.ui = new ConsoleUI();
        this.loop = new GameLoop(ui);
    }

    public void avvia() {
        int ris = ui.scegliModalita();
        if (ris == 1) {
            avviaStoria();
        } else if (ris == 2) {
            avviaAllenamento();
        } else {
            avviaTorneo();
        }
    }

    public void avviaStoria() {

        int level = 1;
        do {
            this.labirinto = loader.loadLevel(level);
            this.criminal = new Criminal(labirinto.getInizioX(), labirinto.getInizioY());
            this.trappole = CreaOggetti.creaTrappole(labirinto, criminal);

            loop.run(labirinto, criminal, this::controllaVittoria, trappole);
        } while(level < 3);

    }

    public void avviaAllenamento() {
        this.labirinto = loader.loadLevel();
        this.criminal = new Criminal(labirinto.getInizioX(), labirinto.getInizioY());
        this.trappole = CreaOggetti.creaTrappole(labirinto, criminal);


        long inizio = System.currentTimeMillis();
        loop.run(labirinto, criminal, this::controllaVittoria, trappole);
        long fine = System.currentTimeMillis();
        long secondi = (fine - inizio) / 1000;
    }

    public void avviaTorneo() {
        this.labirinto = loader.loadLevel(1);
        this.criminal = new Criminal(labirinto.getInizioX(), labirinto.getInizioY());
        this.trappole = CreaOggetti.creaTrappole(labirinto, criminal);




        // Qui dovrai passare 'mappaAsciiScelta' al tuo oggetto labirinto
        // labirinto.caricaDaStringa(mappaAsciiScelta);

        // --- Logica esistente ---
        long inizio = System.currentTimeMillis();
        String nome = ui.scegliNome();

        loop.run(labirinto, criminal, this::controllaVittoria, trappole);

        long fine = System.currentTimeMillis();
        long secondi = (fine - inizio) / 1000;

        // 3. Salviamo il record usando l'indice + 1 come identificativo mappa
        salvaRecord(nome, secondi, indiceMappa + 1);

        System.out.println("Nome: " + nome + "\nSecondi: " + secondi);
    }

    public void salvaRecord(String nome, long secondi, int mappa) {
        String pathCartella = "src/resources/Records";
        File fileRecord = new File("src/resources/Records", "recordMappa_"+mappa+".txt");
        String linea;
        List<String> righeClassifica = new ArrayList<>();
        boolean trovato = false;
        boolean aggiornato = false;

        // 1. Lettura: Carichiamo tutto il file in una lista
        if (fileRecord.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileRecord))) {
                while ((linea = reader.readLine()) != null) {
                    String[] parti = linea.split(":");
                    if (parti.length == 2) {
                        String nomeEsistente = parti[0];
                        long tempoEsistente = Long.parseLong(parti[1]);

                        // Controlliamo se è il giocatore attuale
                        if (nomeEsistente.equalsIgnoreCase(nome)) {
                            trovato = true;
                            // Aggiorniamo la riga solo se il nuovo tempo è migliore
                            if (secondi < tempoEsistente) {
                                righeClassifica.add(nome + ":" + secondi);
                                aggiornato = true;
                            } else {
                                righeClassifica.add(linea); // Teniamo il vecchio record
                            }
                        } else {
                            righeClassifica.add(linea); // Riga di un altro giocatore
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Errore in lettura: ");
            }
        }

        // 2. Se il giocatore non era nel file, lo aggiungiamo come nuovo record
        if (!trovato) {
            righeClassifica.add(nome + ":" + secondi);
            aggiornato = true;
        }

        // 3. Scrittura: Se c'è stato un aggiornamento o un nuovo inserimento, riscriviamo il file
        if (aggiornato) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileRecord))) {
                for (String riga : righeClassifica) {
                    writer.write(riga);
                    writer.newLine();
                }
                System.out.println("Record salvato correttamente.");
            } catch (IOException e) {
                System.out.println("Errore in scrittura: ");
            }
        } else {
            System.out.println("Nessun miglioramento: record non aggiornato.");
        }
    }




}
