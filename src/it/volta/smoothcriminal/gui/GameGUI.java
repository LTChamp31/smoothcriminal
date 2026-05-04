package it.volta.smoothcriminal.gui;

import it.volta.smoothcriminal.core.LevelLoader;
import it.volta.smoothcriminal.core.VideoGame;
import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.Maze;
import it.volta.smoothcriminal.console.GameConsole;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.Clip;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code GameGUI} estende {@link VideoGame} e implementa l'interfaccia
 * grafica utente principale del gioco utilizzando la libreria Swing.
 * Gestisce il ciclo di vita dell'applicazione finestra, il passaggio tra i menu
 * e il gioco attivo, la logica delle varie modalità (Allenamento, Storia, Torneo)
 * e la persistenza dei dati (salvataggi locali e sincronizzazione record in cloud).
 *
 * @author Marco Caria & Lotan Teny
 */
public class GameGUI extends VideoGame {

    private JFrame frame;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private Clip musica;
    private boolean isMusicPlaying = false;
    private LevelLoader loader;

    private String currentMode = "";
    private String playerName = "";
    private int livelloCorrente = 1;
    private int[] indiceTorneo = new int[1];
    private long startTime;

    /**
     * Costruttore della classe {@code GameGUI}.
     * Configura il {@link JFrame} principale dell'applicazione (dimensioni,
     * posizione, icona) e inizializza i componenti base come il {@link MenuPanel}
     * e il gestore dei livelli.
     *
     * @param criminal L'oggetto {@link Criminal} iniziale del gioco.
     * @param maze     L'oggetto {@link Maze} iniziale in cui si svolge la partita.
     */
    public GameGUI(Criminal criminal, Maze maze) {
        super(criminal, maze);
        loader = new LevelLoader();

        frame = new JFrame("Smooth Criminal - GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        try {
            Image icon = javax.imageio.ImageIO.read(new File("resources/sprites/icon.png"));
            frame.setIconImage(icon);
        } catch (IOException e) {
            System.err.println("Errore caricamento icona: " + e.getMessage());
        }

        menuPanel = new MenuPanel(this);
    }

    /**
     * Implementa il metodo astratto della classe {@link VideoGame}.
     * Imposta il pannello del menu come contenuto attivo del frame principale
     * e rende visibile la finestra di gioco.
     *
     * @return {@code true} a indicare che l'avvio dell'interfaccia è andato a buon fine.
     */
    @Override
    public boolean avvia() {
        frame.setContentPane(menuPanel);
        frame.setVisible(true);
        return true;
    }

    /**
     * Inizializza e avvia una sessione in modalità "ALLENAMENTO".
     * Carica il livello standard tramite il {@link LevelLoader}, ripristina
     * la posizione del giocatore e avvia il contatore del tempo.
     */
    public void avviaAllenamento() {
        currentMode = "ALLENAMENTO";
        this.maze = loader.loadLevel();
        this.criminal = new Criminal(maze.getInizioX(), maze.getInizioY());
        this.inCorso = true;

        startTime = System.currentTimeMillis();
        avviaGioco();
    }

    /**
     * Inizializza e gestisce l'ingresso nella modalità "STORIA".
     * Richiede il nome dell'utente, verifica l'eventuale presenza di salvataggi
     * locali e propone al giocatore se riprendere dal livello precedentemente
     * raggiunto o ricominciare.
     */
    public void avviaStoria() {
        playerName = showCustomInputDialog("Modalità Storia", "Inserisci il tuo nome:");
        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }

        currentMode = "STORIA";
        int livelloSalvato = caricaProgresso(playerName);
        livelloCorrente = 1;

        if (livelloSalvato > 1) {
            int response = showCustomConfirmDialog("Salvataggio Trovato",
                    "Hai raggiunto il livello " + livelloSalvato + ".\nVuoi riprendere da lì?");

            if (response == JOptionPane.YES_OPTION) {
                livelloCorrente = livelloSalvato;
            } else {
                salvaProgresso(playerName, 1);
            }
        }

        caricaLivelloStoria();
    }

    /**
     * Metodo di supporto per caricare l'istanza del labirinto corrispondente
     * al livello corrente della modalità Storia. Avvia quindi la sessione di gioco.
     */
    private void caricaLivelloStoria() {
        int[] indice = new int[] { livelloCorrente - 1 };
        this.maze = loader.loadLevel(indice, 'S');
        this.criminal = new Criminal(maze.getInizioX(), maze.getInizioY());
        this.inCorso = true;
        avviaGioco();
    }

    /**
     * Inizializza e avvia una sessione in modalità "TORNEO".
     * Richiede il nome dell'utente per la registrazione del record, carica
     * la mappa specifica da torneo e avvia il timer competitivo.
     */
    public void avviaTorneo() {
        playerName = showCustomInputDialog("Modalità Torneo", "Inserisci il tuo nome per il Torneo:");
        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }

        currentMode = "TORNEO";
        this.maze = loader.loadLevel(indiceTorneo, 'T');
        this.criminal = new Criminal(maze.getInizioX(), maze.getInizioY());
        this.inCorso = true;

        startTime = System.currentTimeMillis();
        avviaGioco();
    }

    /**
     * Gestisce la transizione grafica dal menu alla sessione giocabile.
     * Istanziando un nuovo {@link GamePanel}, lo imposta come pannello principale
     * e gli assegna il focus per la lettura degli input da tastiera.
     */
    private void avviaGioco() {
        gamePanel = new GamePanel(this, maze, criminal);
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }

    /**
     * Interrompe la schermata di gioco attuale e ripristina la visualizzazione
     * del menu principale.
     */
    public void tornaAlMenu() {
        frame.setContentPane(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Attiva o disattiva la riproduzione del tema musicale in background.
     * Se la musica è in esecuzione viene fermata, altrimenti viene avviata.
     */
    public void toggleMusic() {
        String smooth = "resources/sound/smooth_criminal.wav";
        if (musica != null && musica.isRunning()) {
            musica.stop();
            isMusicPlaying = false;
        } else {
            musica = GameConsole.PlayMusic(smooth);
            isMusicPlaying = true;
        }
    }

    /**
     * Restituisce lo stato attuale della riproduzione musicale.
     *
     * @return {@code true} se la colonna sonora è attiva, {@code false} altrimenti.
     */
    public boolean isMusicPlaying() {
        return isMusicPlaying;
    }

    /**
     * Riproduce l'animazione speciale di vittoria ("Moonwalk") in sovrimpressione,
     * utilizzando il {@code GlassPane} del JFrame.
     *
     * @param onComplete Oggetto {@link Runnable} contenente il codice da eseguire
     *                   al termine dell'animazione (es. messaggi di vittoria o salvataggi).
     */
    private void playMoonwalkAnimation(Runnable onComplete) {
        JPanel glassPane = (JPanel) frame.getGlassPane();
        glassPane.removeAll();
        glassPane.setLayout(null);
        glassPane.setOpaque(false);

        ImageIcon icon = new ImageIcon("resources/sprites/moonwalk.gif");
        JLabel gifLabel = new JLabel(icon);

        int w = icon.getIconWidth() > 0 ? icon.getIconWidth() : 150;
        int h = icon.getIconHeight() > 0 ? icon.getIconHeight() : 150;

        int startX = frame.getWidth();
        int startY = frame.getHeight() / 2 - h / 2;
        gifLabel.setBounds(startX, startY, w, h);

        glassPane.add(gifLabel);
        glassPane.setVisible(true);

        Timer timer = new Timer(15, null);
        timer.addActionListener(e -> {
            int x = gifLabel.getX() - 5;
            if (x < -w) {
                timer.stop();
                glassPane.remove(gifLabel);
                glassPane.setVisible(false);
                if (onComplete != null) {
                    onComplete.run();
                }
            } else {
                gifLabel.setLocation(x, startY);
            }
        });
        timer.start();
    }

    /**
     * Gestisce la logica di fine livello in caso di vittoria.
     * Calcola il tempo di completamento, avvia l'animazione di vittoria ed esegue
     * azioni differenziate in base alla modalità corrente (salvataggio progressi,
     * pubblicazione record, o avanzamento al livello successivo).
     */
    public void mostraVittoria() {
        long tempoCalc = (System.currentTimeMillis() - startTime) / 1000;
        playMoonwalkAnimation(() -> {
            if (currentMode.equals("ALLENAMENTO")) {
                showCustomMessageDialog("Allenamento Completato",
                        "Hai Vinto!\nTempo impiegato: " + tempoCalc + " secondi.");
                tornaAlMenu();
            } else if (currentMode.equals("TORNEO")) {
                salvaRecord(playerName, tempoCalc, indiceTorneo[0] + 1);
                showCustomMessageDialog("Torneo Completato",
                        "GRANDE PROVA!\nTempo: " + tempoCalc + " secondi.\nRecord salvato!");
                tornaAlMenu();
            } else if (currentMode.equals("STORIA")) {
                int totaleLivelli = contaLivelliStoria();
                if (livelloCorrente < totaleLivelli) {
                    livelloCorrente++;
                    salvaProgresso(playerName, livelloCorrente);
                    showCustomMessageDialog("Vittoria", "Livello Completato! Preparati per il prossimo...");
                    caricaLivelloStoria();
                } else {
                    showCustomMessageDialog("Gioco Finito!",
                            "Complimenti " + playerName + "! Hai completato la modalità Storia!");
                    tornaAlMenu();
                }
            } else {
                showCustomMessageDialog("Vittoria", "Hai Vinto!");
                tornaAlMenu();
            }
        });
    }

    /**
     * Mostra una finestra di dialogo per notificare la sconfitta (cattura
     * da parte di un nemico) e riporta l'utente al menu principale.
     */
    public void mostraSconfitta() {
        showCustomMessageDialog("Sconfitta", "Sei stato catturato!");
        tornaAlMenu();
    }

    /**
     * Metodo di utilità per contare il numero totale di livelli disponibili
     * per la modalità Storia leggendo il file di configurazione apposito.
     *
     * @return Il numero di livelli della modalità Storia.
     */
    private int contaLivelliStoria() {
        int contatore = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("resources/livelli/livelliStoria.txt"))) {
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
     * Verifica l'esistenza di un salvataggio per un dato utente nel file locale
     * e ne restituisce il livello massimo raggiunto.
     *
     * @param nome Il nome dell'utente di cui recuperare i progressi.
     * @return Il numero del livello salvato, o {@code 1} se non vi sono salvataggi.
     */
    private int caricaProgresso(String nome) {
        File file = new File("resources/record/salvataggiStoria.txt");
        if (!file.exists())
            return 1;
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
     * Scrive sul file locale il progresso della modalità storia di un utente,
     * aggiornandolo solo se il livello raggiunto è superiore al precedente salvato.
     *
     * @param nome Il nome dell'utente.
     * @param livelloRaggiunto L'indice del livello appena completato/sbloccato.
     */
    private void salvaProgresso(String nome, int livelloRaggiunto) {
        File fileSalvataggi = new File("resources/record/salvataggiStoria.txt");
        fileSalvataggi.getParentFile().mkdirs();
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
            } catch (IOException e) {
            }
        }
    }

    /**
     * Salva il tempo di completamento della mappa di Torneo su file locale e,
     * tramite un Thread separato, inoltra il record su un database Firebase Firestore.
     * L'aggiornamento locale avviene solo se il nuovo tempo è inferiore (migliore)
     * del precedente.
     *
     * @param nome    Il nome del giocatore.
     * @param secondi Il tempo impiegato in secondi.
     * @param mappa   L'identificativo della mappa giocata.
     */
    private void salvaRecord(String nome, long secondi, int mappa) {
        File fileRecord = new File("resources/record", "recordMappa_" + mappa + ".txt");
        fileRecord.getParentFile().mkdirs();
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
            } catch (IOException e) {
            }
        }

        new Thread(() -> {
            try {
                URL url = new URL(
                        "https://firestore.googleapis.com/v1/projects/smoothcriminal-1c96c/databases/(default)/documents/leaderboard");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = "{"
                        + "\"fields\": {"
                        + "\"username\": { \"stringValue\": \"" + nome + "\" },"
                        + "\"mapName\": { \"stringValue\": \"Mappa " + mappa + "\" },"
                        + "\"time\": { \"doubleValue\": " + secondi + ".0 }"
                        + "}"
                        + "}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                System.out.println("Firebase Sync Response Code: " + code);
            } catch (Exception e) {
                System.err.println("Errore durante il salvataggio su Firebase: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Genera e mostra una finestra modale personalizzata per l'inserimento di
     * un input testuale da parte dell'utente.
     *
     * @param title   Il titolo della finestra.
     * @param message Il messaggio descrittivo da mostrare.
     * @return La stringa inserita dall'utente, oppure {@code null} in caso di annullamento.
     */
    private String showCustomInputDialog(String title, String message) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(new Color(30, 30, 30));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel(message, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        content.add(titleLabel, BorderLayout.NORTH);

        JTextField inputField = new JTextField(15);
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        inputField.setBackground(new Color(50, 50, 50));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        content.add(inputField, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setOpaque(false);

        final String[] result = { null };

        JButton btnOk = createCustomMenuButton("OK");
        btnOk.addActionListener(e -> {
            result[0] = inputField.getText();
            dialog.dispose();
        });

        JButton btnAnnulla = createCustomMenuButton("Annulla");
        btnAnnulla.setBackground(new Color(100, 30, 30, 200));
        btnAnnulla.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(btnOk);
        buttonsPanel.add(btnAnnulla);

        content.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.add(content);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        return result[0];
    }

    /**
     * Genera e mostra una finestra modale di conferma personalizzata con due
     * pulsanti (Sì/No).
     *
     * @param title   Il titolo della finestra.
     * @param message La domanda posta all'utente.
     * @return {@link JOptionPane#YES_OPTION} se l'utente accetta,
     *         {@link JOptionPane#NO_OPTION} altrimenti.
     */
    private int showCustomConfirmDialog(String title, String message) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));

        JPanel content = new JPanel(new BorderLayout(10, 20));
        content.setBackground(new Color(30, 30, 30));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel msgLabel = new JLabel(
                "<html><div style='text-align: center;'>" + message.replace("\n", "<br>") + "</div></html>",
                SwingConstants.CENTER);
        msgLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        msgLabel.setForeground(Color.WHITE);
        content.add(msgLabel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setOpaque(false);

        final int[] result = { JOptionPane.NO_OPTION };

        JButton btnSi = createCustomMenuButton("Sì");
        btnSi.addActionListener(e -> {
            result[0] = JOptionPane.YES_OPTION;
            dialog.dispose();
        });

        JButton btnNo = createCustomMenuButton("No");
        btnNo.setBackground(new Color(100, 30, 30, 200));
        btnNo.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(btnSi);
        buttonsPanel.add(btnNo);

        content.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.add(content);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        return result[0];
    }

    /**
     * Genera e mostra una finestra modale di solo avviso (con il singolo pulsante OK)
     * personalizzata graficamente.
     *
     * @param title   Il titolo dell'avviso.
     * @param message Il messaggio da mostrare all'utente.
     */
    private void showCustomMessageDialog(String title, String message) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));

        JPanel content = new JPanel(new BorderLayout(10, 20));
        content.setBackground(new Color(30, 30, 30));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel msgLabel = new JLabel(
                "<html><div style='text-align: center;'>" + message.replace("\n", "<br>") + "</div></html>",
                SwingConstants.CENTER);
        msgLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        msgLabel.setForeground(Color.WHITE);
        content.add(msgLabel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setOpaque(false);

        JButton btnOk = createCustomMenuButton("OK");
        btnOk.addActionListener(e -> dialog.dispose());
        buttonsPanel.add(btnOk);

        content.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.add(content);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    /**
     * Metodo factory di supporto per creare pulsanti personalizzati (JButton)
     * coerenti con lo stile visivo dell'interfaccia. Gestisce bordi arrotondati
     * e transizioni di colore in fase di hover del mouse.
     *
     * @param text L'etichetta del pulsante.
     * @return L'istanza del pulsante configurata.
     */
    private JButton createCustomMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBackground(new Color(50, 50, 50, 200));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color originalBg;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                originalBg = button.getBackground();
                button.setBackground(new Color(220, 20, 60, 230));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (originalBg != null) {
                    button.setBackground(originalBg);
                }
            }
        });
        return button;
    }
}