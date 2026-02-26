package it.volta.smoothcriminal.console;

import it.volta.smoothcriminal.core.*;
import it.volta.smoothcriminal.model.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code GameConsole} estende {@link VideoGame} e rappresenta l'implementazione
 * specifica del gioco per l'interfaccia a riga di comando.
 * Gestisce il flusso principale delle diverse modalità di gioco: Storia, Allenamento
 * e Torneo, si occupa del caricamento dei livelli, della gestione dei progressi
 * dell'utente e del salvataggio dei record temporali.
 * * @author Marco Caria & Lotan Teny
 **/
public class GameConsole extends VideoGame {

    private ConsoleUI ui;
    private GameLoop loop;
    private LevelLoader loader = new LevelLoader();
    private List<Trap> trap;
    private Clip musica;
    /**
     * Costruttore della classe {@code GameConsole}.
     * Inizializza l'interfaccia utente e il ciclo di gioco.
     * * @param criminal L'entità del giocatore (può essere null all'inizializzazione).
     * @param maze La struttura del maze (può essere null all'inizializzazione).
     */
    public GameConsole(Criminal criminal, Maze maze) {
        super(criminal, maze);
        this.ui = new ConsoleUI();
        this.loop = new GameLoop(ui, this);
    }

    /**
     * Avvia il menu principale del gioco e porta l'utente verso la modalità scelta.
     * * @return {@code true} se il gioco continua, {@code false} se l'utente sceglie di uscire.
     */
    public boolean avvia() {
        int ris = ui.scegliModalita();
        if (ris == 1) {
            avviaStoria();
            return true;
        } else if (ris == 2) {
            avviaAllenamento();
            return true;
        } else if (ris == 3) {
            avviaTorneo();
            return true;
        } else if (ris == 4) {
            String smooth = "resources/sound/smooth_criminal.wav";
            if (musica != null && musica.isRunning()) {
                musica.stop();
            } else {
                musica = PlayMusic(smooth);
            }
            avvia();
            return true;
        } else if (ris == 5) {
            avviaComandi();
            return true;
        } else if (ris == 6) {
            System.out.println("Ciao Ciao");
            System.exit(0);
            return false;
        } else {
            return false;
        }
    }

    /**
     * Gestisce la modalità Storia.
     * Carica il progresso dell'utente, gestisce la sequenza dei livelli e salva
     * automaticamente i progressi dopo ogni vittoria.
     */
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

            this.loader = new LevelLoader();
            this.maze = loader.loadLevel(indice, 'S');
            this.criminal = new Criminal(maze.getInizioX(), maze.getInizioY());
            this.trap = CreateObjects.creaTrappole(maze, criminal);

            boolean terminatoRegolarmente = loop.run(maze, criminal, this::controllaVittoria, this::controllaPerdita, trap);

            if (!terminatoRegolarmente) {
                return;
            }

            if (controllaVittoria()) {
                livelloCorrente++;
                salvaProgresso(nome, livelloCorrente);
                System.out.println("Livello Completato! Preparati per il prossimo...");
            }
            else if (controllaPerdita()) {
                mostraSconfitta();
                System.out.println("Premi INVIO per tornare al menu...");
                try { System.in.read(); } catch (IOException e) {}
                avvia();
            }
        }

        System.out.println("Complimenti " + nome + "! Hai completato la modalità Storia!");
        System.out.println("Premi INVIO per tornare al menu...");
        try { System.in.read(); } catch (IOException e) {}
        avvia();
    }

    /**
     * Conta quanti livelli sono presenti nel file di configurazione della storia.
     * * @return Il numero totale di livelli trovati.
     */
    private int contaLivelliStoria() {
        int contatore = 0;
        try (BufferedReader br = new BufferedReader(
                new FileReader("resources/livelli/livelliStoria.txt"))) {
            String riga;
            while ((riga = br.readLine()) != null) {
                if (riga.equals("---")) {
                    contatore++;
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nella lettura del file livelliStoria.txt");
        }
        return contatore;
    }

    /**
     * Carica il livello massimo raggiunto da un determinato giocatore da file.
     * * @param nome Il nome del giocatore.
     * @return Il numero del livello raggiunto (default 1 se non trovato).
     */
    private int caricaProgresso(String nome) {
        File file = new File("resources/record/salvataggiStoria.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] parti = linea.split(":");
                if (parti.length == 2 && parti[0].equalsIgnoreCase(nome)) {
                    return Integer.parseInt(parti[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore lettura salvataggi storia");
        }
        return 1;
    }

    /**
     * Salva o aggiorna il progresso della modalità storia per un utente.
     * * @param nome Il nome del giocatore.
     * @param livelloRaggiunto Il numero del nuovo livello raggiunto.
     */
    public void salvaProgresso(String nome, int livelloRaggiunto) {
        File fileSalvataggi = new File("resources/record/salvataggiStoria.txt");
        List<String> righe = new ArrayList<>();
        boolean trovato = false;
        boolean aggiornato = false;

        if (fileSalvataggi.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileSalvataggi))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] parti = linea.split(":");
                    if (parti.length == 2) {
                        if (parti[0].equalsIgnoreCase(nome)) {
                            trovato = true;
                            int livelloEsistente = Integer.parseInt(parti[1]);
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

    /**
     * Avvia Allenamento con un maze generato casualmente.
     * Poi mostra il tempo di completamento.
     */
    public void avviaAllenamento() {
        this.loader = new LevelLoader();
        this.maze = loader.loadLevel();
        this.criminal = new Criminal(maze.getInizioX(), maze.getInizioY());
        this.trap = CreateObjects.creaTrappole(maze, criminal);

        long inizio = System.currentTimeMillis();

        boolean terminatoRegolarmente = loop.run(maze, criminal, this::controllaVittoria, this::controllaPerdita, trap);

        if (!terminatoRegolarmente) {
            return;
        }

        long fine = System.currentTimeMillis();
        long secondi = (fine - inizio) / 1000;
        System.out.println("Tempo completamento: " + secondi + " secondi");
        System.out.println("Premi INVIO per tornare al menu...");
        try { System.in.read(); } catch (IOException e) {}
        avvia();
    }

    /**
     * Avvia la modalità Torneo su una mappa randomica caricata da file.
     * Calcola il tempo di completamento e aggiorna i record se necessario.
     */
    public void avviaTorneo() {
        int[] indice = new int[1];
        this.loader = new LevelLoader();

        this.maze = loader.loadLevel(indice, 'T');
        this.criminal = new Criminal(maze.getInizioX(), maze.getInizioY());
        this.trap = CreateObjects.creaTrappole(maze, criminal);

        String nome = ui.scegliNome();
        long inizio = System.currentTimeMillis();

        boolean terminatoRegolarmente = loop.run(maze, criminal, this::controllaVittoria, this::controllaPerdita, trap);

        if (!terminatoRegolarmente) {
            return;
        }

        if (controllaVittoria()) {
            long fine = System.currentTimeMillis();
            long secondi = (fine - inizio) / 1000;

            salvaRecord(nome, secondi, indice[0] + 1);

            System.out.println("\nGRANDE PROVA!");
            System.out.println("Tempo completamento: " + secondi + " secondi");
        } else {
            mostraSconfitta();
        }

        System.out.println("Premi INVIO per tornare al menu...");
        try { System.in.read(); } catch (IOException e) {}
        avvia();
    }

    /**
     * Salva o aggiorna il record di tempo per una specifica mappa del torneo.
     * Il record viene salvato solo se il tempo attuale è inferiore a quello esistente.
     * * @param nome Il nome del giocatore.
     * @param secondi Il tempo impiegato in secondi.
     * @param mappa L'identificativo numerico della mappa.
     */
    public void salvaRecord(String nome, long secondi, int mappa) {
        File fileRecord = new File("resources/record", "recordMappa_" + mappa + ".txt");
        List<String> righeClassifica = new ArrayList<>();
        boolean trovato = false;
        boolean aggiornato = false;

        if (fileRecord.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileRecord))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] parti = linea.split(":");
                    if (parti.length == 2) {
                        if (parti[0].equalsIgnoreCase(nome)) {
                            trovato = true;
                            long tempoEsistente = Long.parseLong(parti[1]);
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
                System.out.println("Errore in lettura record");
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
                System.out.println("Errore in scrittura record");
            }
        } else {
            System.out.println("Nessun miglioramento: record non aggiornato.");
        }
    }

    /**
     * Carica e visualizza il file di testo contenente la schermata di sconfitta.
     */
    public void mostraSconfitta(){
        File file = new File("resources/schermate/Sconfitta.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura della schermata sconfitta.");
        }
    }
    /**
     * Schermata di aiuto con i comandi
     */
    public void avviaComandi(){
        File file = new File("resources/schermate/comandi.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura della schermata sconfitta.");
        }
        System.out.println("Premi INVIO per tornare al menu...");
        try { System.in.read(); } catch (IOException e) {}
        avvia();
    }

    public static Clip PlayMusic(String path) {
        try {
            File music = new File(path);
            if (music.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(music);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                control.setValue((float) Math.log10(0.05f) * 20);

                clip.start();
                return clip;
            }
        } catch (Exception e){
            System.out.println("Errore durante la riproduzione della musica: ");
        }
        return null;
    }
}