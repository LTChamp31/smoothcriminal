package it.volta.smoothcriminal.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * La classe {@code MenuPanel} estende {@link JPanel} e gestisce la schermata
 * di avvio (Main Menu) dell'applicazione. Fornisce l'interfaccia grafica per
 * la selezione delle modalità di gioco (Storia, Allenamento, Torneo), per la
 * gestione delle opzioni (come la musica) e per l'uscita.
 *
 * @author Marco Caria & Lotan Teny
 */
public class MenuPanel extends JPanel {

    private GameGUI gui;
    private Image bgImage;

    /**
     * Costruttore della classe {@code MenuPanel}.
     * Configura il layout principale, tenta il caricamento dell'immagine di sfondo
     * e assembla i componenti visivi: il grande titolo in alto e il pannello
     * centrale contenente i pulsanti di navigazione interattivi.
     *
     * @param gui Il riferimento all'istanza della classe controllore {@link GameGUI}.
     */
    public MenuPanel(GameGUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout());

        try {
            bgImage = ImageIO.read(new File("resources/sprites/menu.png"));
        } catch (IOException e) {
            System.err.println("Errore caricamento immagine menu: " + e.getMessage());
        }

        JLabel titleLabel = new JLabel("SMOOTH CRIMINAL", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = fm.getAscent() + (getHeight() - fm.getHeight()) / 2;

                g2.setColor(new Color(0, 0, 0, 180));
                g2.drawString(getText(), x + 4, y + 4);

                g2.setColor(getForeground());
                g2.drawString(getText(), x, y);
            }
        };
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 64));
        titleLabel.setForeground(new Color(255, 60, 60)); // Bright red
        titleLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 40, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(5, 1, 10, 20));
        buttonsPanel.setOpaque(false); // Make transparent to see background
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 250, 80, 250));

        JButton btnStoria = createButton("STORIA");
        JButton btnAllenamento = createButton("ALLENAMENTO");
        JButton btnTorneo = createButton("TORNEO");
        JButton btnMusica = createButton("MUSICA: OFF");
        JButton btnEsci = createButton("ESCI");

        btnMusica.setText(gui.isMusicPlaying() ? "MUSICA: ON" : "MUSICA: OFF");

        btnStoria.addActionListener(e -> gui.avviaStoria());
        btnAllenamento.addActionListener(e -> gui.avviaAllenamento());
        btnTorneo.addActionListener(e -> gui.avviaTorneo());
        btnMusica.addActionListener(e -> {
            gui.toggleMusic();
            btnMusica.setText(gui.isMusicPlaying() ? "MUSICA: ON" : "MUSICA: OFF");
        });
        btnEsci.addActionListener(e -> System.exit(0));

        buttonsPanel.add(btnStoria);
        buttonsPanel.add(btnAllenamento);
        buttonsPanel.add(btnTorneo);
        buttonsPanel.add(btnMusica);
        buttonsPanel.add(btnEsci);

        add(buttonsPanel, BorderLayout.CENTER);
    }

    /**
     * Sovrascrive il metodo di disegno del pannello per renderizzare
     * l'immagine di sfondo a tutto schermo. Se l'immagine è presente, le applica
     * un filtro di scurimento (overlay semi-trasparente) per favorire la
     * leggibilità dei pulsanti sovrastanti.
     *
     * @param g Il contesto grafico {@link Graphics} corrente.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 120));
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g.setColor(new Color(20, 20, 20));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Metodo di servizio per creare e configurare graficamente un pulsante
     * personalizzato da utilizzare all'interno del menu.
     * Imposta font, colori, bordi arrotondati, disabilita il rendering standard
     * e associa un listener per gli effetti visivi di interazione mouse (Hover).
     *
     * @param text Il testo da inserire all'interno del pulsante.
     * @return Il {@link JButton} creato, completamente stilizzato e pronto all'uso.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw rounded semi-transparent background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2); // Draw text on top
                g2.dispose();
            }
        };
        button.setFont(new Font("SansSerif", Font.BOLD, 22));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false); // Let custom paintComponent handle the fill
        button.setBackground(new Color(30, 30, 30, 200)); // Semi-transparent dark
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 20, 60, 230)); // Semi-transparent red on hover
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 30, 30, 200));
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }
}