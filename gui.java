package gui; // O il package che stai usando

import core.Videogioco;
import model.Nemico;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class GiocoGUI extends Videogioco {
    private JFrame frame;
    private JPanel pannello;
    private final int CELLA = 40;

    public GiocoGUI() {
        super();
        setupGUI();
    }

    private void setupGUI() {
        frame = new JFrame("Robot Escape - GUI Edition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crea e aggiungi il Menu
        creaMenu();

        aggiornaDimensioniFinestra();

        pannello = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                disegnaLabirinto(g);
            }
        };

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!inCorso) return;

                String comando = "";
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP:    comando = "W"; break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  comando = "S"; break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  comando = "A"; break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: comando = "D"; break;
                }

                if (!comando.isEmpty()) {
                    elaboraTurno(comando);
                    mostraStato();

                    // Se il gioco finisce dopo questa mossa, mostra il popup
                    if (!inCorso) {
                        mostraMessaggio(messaggioFineGioco);
                    }
                }
            }
        });

        frame.add(pannello);
    }

    /**
     * NUOVO METODO: Crea la barra dei menu in alto
     */
    private void creaMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Partita");

        JMenuItem itemCarica = new JMenuItem("Carica Livello...");
        JMenuItem itemEsci = new JMenuItem("Esci");

        // Azione: Cliccando "Carica Livello..." si apre la scelta file
        itemCarica.addActionListener(e -> apriSceltaFile());

        // Azione: Esci
        itemEsci.addActionListener(e -> System.exit(0));

        menuFile.add(itemCarica);
        menuFile.addSeparator(); // Linea di divisione
        menuFile.add(itemEsci);

        menuBar.add(menuFile);
        frame.setJMenuBar(menuBar);
    }

    /**
     * NUOVO METODO: Apre la finestra JFileChooser per selezionare il .txt
     */
    private void apriSceltaFile() {
        // Apre il selettore partendo dalla cartella 'resources'
        JFileChooser fileChooser = new JFileChooser(new File("resources").getAbsolutePath());
        fileChooser.setDialogTitle("Scegli un file di testo del labirinto");

        int userSelection = fileChooser.showOpenDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileScelto = fileChooser.getSelectedFile();

            // Chiama il metodo della classe padre (Videogioco) per aggiornare i dati
            caricaLivello(fileScelto.getAbsolutePath());

            // Aggiorna la grafica
            aggiornaDimensioniFinestra();
            mostraStato();

            // IMPORTANTE: Dopo aver cliccato un menu, la finestra perde il "focus"
            // della tastiera. Dobbiamo ridarglielo altrimenti i tasti WASD non vanno!
            frame.requestFocusInWindow();
        }
    }

    /**
     * Calcola la dimensione giusta in base alla grandezza del labirinto caricato
     */
    private void aggiornaDimensioniFinestra() {
        int larghezza = labirinto.getColonne() * CELLA + 15;
        int altezza = labirinto.getRighe() * CELLA + 60; // +60 per fare spazio al Menu
        frame.setSize(larghezza, altezza);
    }

    private void disegnaLabirinto(Graphics g) {
        for (int r = 0; r < labirinto.getRighe(); r++) {
            for (int c = 0; c < labirinto.getColonne(); c++) {
                int x = c * CELLA;
                int y = r * CELLA;

                if (labirinto.isMuro(r, c)) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(x, y, CELLA, CELLA);
                } else if (labirinto.isUscita(r, c)) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, CELLA, CELLA);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, CELLA, CELLA);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(x, y, CELLA, CELLA);
                }
            }
        }

        g.setColor(Color.MAGENTA);
        for (Nemico n : nemici) {
            g.fillOval(n.getColonna() * CELLA + 5, n.getRiga() * CELLA + 5, CELLA - 10, CELLA - 10);
        }

        g.setColor(Color.RED);
        g.fillOval(robot.getColonna() * CELLA + 5, robot.getRiga() * CELLA + 5, CELLA - 10, CELLA - 10);
    }

    @Override
    public void avvia() {
        frame.setVisible(true);
        mostraStato();
        frame.requestFocusInWindow();
    }

    @Override
    protected String leggiInput() {
        return "";
    }

    @Override
    protected void mostraStato() {
        if (pannello != null) {
            pannello.repaint();
        }
    }

    @Override
    protected void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(frame, messaggio);
        // Invece di chiudere il gioco (System.exit), permettiamo di caricare un nuovo livello dal menu!
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Videogioco gioco = new GiocoGUI();
            gioco.avvia();
        });
    }
}