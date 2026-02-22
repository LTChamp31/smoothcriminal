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
    private ConsoleUI ui;
    private GameLoop loop;
    private LevelLoader loader = new LevelLoader();
    private List<Trappole> trappole;
    private Scanner input = new Scanner(System.in);

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
        String nome = ui.scegliNome();
        int livelloSalvato = caricaProgresso(nome);
        int livelloCorrente = 1;
        if (livelloSalvato > 1) {
            int scelta = ui.leggiInputLivelloSalvato(livelloSalvato);
            if (scelta == 1) {
                livelloCorrente = livelloSalvato;
            } else {
                salvaProgresso(nome, 1);
            }
        }

        int totaleLivelli = contaLivelliStoria();

        while (livelloCorrente <= totaleLivelli) {
            int[] indice = new int[1];
            indice[0] = livelloCorrente - 1;

            this.labirinto = loader.loadLevel(indice, 'S');
            this.criminal = new Criminal(labirinto.getInizioX(), labirinto.getInizioY());
            this.trappole = CreaOggetti.creaTrappole(labirinto, criminal);
            loop.run(labirinto, criminal, this::controllaVittoria, trappole);

            if (controllaVittoria()) {
                livelloCorrente++;
                salvaProgresso(nome, livelloCorrente);
            }
        }

        System.out.println("Complimenti! Hai completato la modalità Storia!");
    }

    private int contaLivelliStoria() {

        int contatore = 0;

        try (BufferedReader br = new BufferedReader(
                new FileReader("src/resources/levels/livelliStoria.txt"))) {

            String riga;

            while ((riga = br.readLine()) != null) {
                if (riga.equals("---")) {
                    contatore++;
                }
            }

        } catch (Exception e) {
            System.out.println("Errore nella lettura del file");
        }

        return contatore;
    }



    private int caricaProgresso(String nome) {

        File file = new File("src/resources/Records/salvataggiStoria.txt");


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] parti = linea.split(":");

                if (parti.length == 2) {
                    if (parti[0].equalsIgnoreCase(nome)) {
                        return Integer.parseInt(parti[1]);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Errore lettura salvataggi storia");
        }

        return 1;
    }


    public void salvaProgresso(String nome, int livelloRaggiunto) {
        File fileSalvataggi = new File("src/resources/Records/salvataggiStoria.txt");
        List<String> righe = new ArrayList<>();
        boolean trovato = false;
        boolean aggiornato = false;

        if (fileSalvataggi.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileSalvataggi))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] parti = linea.split(":");
                    if (parti.length == 2) {
                        String nomeEsistente = parti[0];
                        int livelloEsistente = Integer.parseInt(parti[1]);

                        if (nomeEsistente.equalsIgnoreCase(nome)) {
                            trovato = true;
                            if (livelloRaggiunto > livelloEsistente) {
                                righe.add(nome + ":" + livelloRaggiunto);
                                aggiornato = true;
                            } else {
                                righe.add(linea);
                            }
                        } else {
                            righe.add(linea);
                        }
                    } else {
                        righe.add(linea);
                    }
                }
            } catch (IOException e) {
                System.out.println("Errore in lettura salvataggi storia");
            }
        }

        if (!trovato) {
            righe.add(nome + ":" + livelloRaggiunto);
            aggiornato = true;
        }

        if (aggiornato) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileSalvataggi))) {
                for (String riga : righe) {
                    writer.write(riga);
                    writer.newLine();
                }
                System.out.println("Progresso salvato: livello " + livelloRaggiunto);
            } catch (IOException e) {
                System.out.println("Errore in scrittura salvataggi storia");
            }
        }
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

        int[] indice = new int[1];

        this.labirinto = loader.loadLevel(indice, 'T');
        this.criminal = new Criminal(labirinto.getInizioX(), labirinto.getInizioY());
        this.trappole = CreaOggetti.creaTrappole(labirinto, criminal);

        String nome = ui.scegliNome();

        long inizio = System.currentTimeMillis();

        loop.run(labirinto, criminal, this::controllaVittoria, trappole);

        long fine = System.currentTimeMillis();
        long secondi = (fine - inizio) / 1000;

        salvaRecord(nome, secondi, indice[0] + 1);

        System.out.println("Tempo completamento: " + secondi + " secondi");
    }

    private int contaLivelli() {

        int contatore = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/Records/salvataggiStoria.txt"))) {

            String riga;
            while ((riga = br.readLine()) != null) {
                if (riga.equals("---")) {
                    contatore++;
                }
            }

        } catch (Exception e) {
            System.out.println("Errore nella lettura del file");
        }

        return contatore;
    }

    private List<String> caricaLivello(int numeroLivello) {

        List<String> mappa = new ArrayList<>();
        int contatore = 0;
        boolean leggendo = false;

        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/levels/livelliStoria.txt"))) {

            String riga;

            while ((riga = br.readLine()) != null) {

                if (riga.equals("---")) {
                    contatore++;

                    if (contatore == numeroLivello) {
                        leggendo = true;
                        continue;
                    } else if (contatore > numeroLivello) {
                        break;
                    }
                }

                if (leggendo) {
                    mappa.add(riga);
                }
            }

        } catch (Exception e) {
            System.out.println("Errore nel caricamento del livello");
        }

        return mappa;
    }



    public void salvaRecord(String nome, long secondi, int mappa) {
        String pathCartella = "src/resources/Records";
        File fileRecord = new File("src/resources/Records", "recordMappa_"+mappa+".txt");
        String linea;
        List<String> righeClassifica = new ArrayList<>();
        boolean trovato = false;
        boolean aggiornato = false;

        if (fileRecord.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileRecord))) {
                while ((linea = reader.readLine()) != null) {
                    String[] parti = linea.split(":");
                    if (parti.length == 2) {
                        String nomeEsistente = parti[0];
                        long tempoEsistente = Long.parseLong(parti[1]);

                        if (nomeEsistente.equalsIgnoreCase(nome)) {
                            trovato = true;
                            if (secondi < tempoEsistente) {
                                righeClassifica.add(nome + ":" + secondi);
                                aggiornato = true;
                            } else {
                                righeClassifica.add(linea);
                            }
                        } else {
                            righeClassifica.add(linea);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Errore in lettura: ");
            }
        }

        if (!trovato) {
            righeClassifica.add(nome + ":" + secondi);
            aggiornato = true;
        }

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
