package it.volta.smoothcriminal.gui;

import it.volta.smoothcriminal.core.CheckObjects;
import it.volta.smoothcriminal.core.CreateObjects;
import it.volta.smoothcriminal.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * La classe {@code GamePanel} estende {@link JPanel} ed è il componente centrale
 * dedicato al rendering grafico e all'interazione in tempo reale durante la partita.
 * Gestisce l'input del giocatore tramite tastiera (Key Bindings), il calcolo
 * del posizionamento della telecamera (camera follow) e il disegno di tutti
 * gli elementi del gioco: mappa, giocatore, nemici, ostacoli e gadget.
 *
 * @author Marco Caria & Lotan Teny
 */
public class GamePanel extends JPanel {

    private GameGUI gui;
    private Maze maze;
    private Criminal criminal;
    private List<Trap> trappole;
    private Gadget[] tuttiGadget;
    private CheckObjects controllore;
    private Gadget[] gadgetUsati = new Gadget[5];
    private String gadgetInAttesa = null;

    private int cGadgetUsati = 0;

    private Image wallImage, groundImage, criminalImage, policeImage, distuggiMuraImage, saltaMuraImage, diagonaleImage, bombImage, uscitaImage;

    /**
     * Costruttore della classe {@code GamePanel}.
     * Inizializza i controllori delle logiche di gioco, crea e posiziona gli
     * oggetti (trappole e gadget) e carica in memoria gli sprite grafici
     * necessari per il rendering.
     *
     * @param gui      Il riferimento all'istanza principale di {@link GameGUI}.
     * @param maze     L'oggetto {@link Maze} che rappresenta il livello corrente.
     * @param criminal Il personaggio {@link Criminal} controllato dall'utente.
     */
    public GamePanel(GameGUI gui, Maze maze, Criminal criminal) {
        this.gui = gui;
        this.maze = maze;
        this.criminal = criminal;

        this.trappole = CreateObjects.creaTrappole(maze, criminal);
        this.tuttiGadget = CreateObjects.creaGadget(maze, criminal);
        this.controllore = new CheckObjects(trappole);

        try {
            wallImage = javax.imageio.ImageIO.read(new java.io.File("resources/sprites/wall2.gif"));
            groundImage = javax.imageio.ImageIO.read(new java.io.File("resources/sprites/ground2.png"));
            criminalImage = javax.imageio.ImageIO.read(new java.io.File("resources/sprites/criminal.png"));
            policeImage = javax.imageio.ImageIO.read(new java.io.File("resources/sprites/police.png"));
            distuggiMuraImage = javax.imageio.ImageIO.read(new java.io.File("resources/sprites/wall_destroy.png"));
            saltaMuraImage = javax.imageio.ImageIO.read(new java.io.File("resources/sprites/pogo.png"));
            diagonaleImage = javax.imageio.ImageIO.read(new java.io.File("resources/sprites/diagonal.png"));
            bombImage = javax.imageio.ImageIO.read(new java.io.File("resources/sprites/bomb.png"));
            uscitaImage = javax.imageio.ImageIO.read(new java.io.File("resources/sprites/uscita.png"));

        } catch (java.io.IOException e) {
            System.err.println("Errore caricamento sprite: " + e.getMessage());
        }

        setBackground(new Color(20, 20, 20));
        setFocusable(true);
        setupKeyBindings();
    }

    /**
     * Configura le scorciatoie da tastiera (Key Bindings) mappando specifici
     * tasti (frecce direzionali, WASD, tasti numerici) alle stringhe di azione
     * interne che innescano il movimento o l'uso di gadget.
     */
    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        bindKey(im, am, KeyEvent.VK_W, "moveUp", 'w');
        bindKey(im, am, KeyEvent.VK_UP, "moveUpArrow", 'w');
        bindKey(im, am, KeyEvent.VK_S, "moveDown", 's');
        bindKey(im, am, KeyEvent.VK_DOWN, "moveDownArrow", 's');
        bindKey(im, am, KeyEvent.VK_A, "moveLeft", 'a');
        bindKey(im, am, KeyEvent.VK_LEFT, "moveLeftArrow", 'a');
        bindKey(im, am, KeyEvent.VK_D, "moveRight", 'd');
        bindKey(im, am, KeyEvent.VK_RIGHT, "moveRightArrow", 'd');
        bindKey(im, am, KeyEvent.VK_Q, "moveUpLeft", 'q');
        bindKey(im, am, KeyEvent.VK_E, "moveUpRight", 'e');
        bindKey(im, am, KeyEvent.VK_Z, "moveDownLeft", 'z');
        bindKey(im, am, KeyEvent.VK_C, "moveDownRight", 'c');

        bindKey(im, am, KeyEvent.VK_1, "distruggi mura", '1');
        bindKey(im, am, KeyEvent.VK_2, "salta mura", '2');
        bindKey(im, am, KeyEvent.VK_3, "muove diagonale", '3');
        bindKey(im, am, KeyEvent.VK_4, "bomba", '4');
        bindKey(im, am, KeyEvent.VK_5, "avvicina uscita", '5');

        bindKey(im, am, KeyEvent.VK_ESCAPE, "escape", 'x');
        bindKey(im, am, KeyEvent.VK_X, "exitX", 'x');
    }

    /**
     * Metodo di supporto per associare in modo pulito un codice tasto a un comando
     * carattere, registrandolo nell'InputMap e nell'ActionMap del componente.
     *
     * @param im       L'oggetto InputMap corrente.
     * @param am       L'oggetto ActionMap corrente.
     * @param keyCode  Il codice del tasto premuto (es. {@code KeyEvent.VK_W}).
     * @param name     Il nome identificativo univoco dell'azione.
     * @param moveChar Il carattere associato all'azione (es. 'w', 'a', '1').
     */
    private void bindKey(InputMap im, ActionMap am, int keyCode, String name, char moveChar) {
        im.put(KeyStroke.getKeyStroke(keyCode, 0), name);
        am.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                eseguiMossa(moveChar);
            }
        });
    }

    /**
     * Interpreta l'input del giocatore ed esegue la conseguente logica di gioco.
     * Gestisce i movimenti standard, l'attivazione dei gadget (sia quelli diretti
     * che quelli che richiedono una direzione successiva) e verifica le
     * condizioni di vittoria/sconfitta o innesco trappole.
     *
     * @param move Il carattere che definisce l'azione o la direzione.
     */
    private void eseguiMossa(char move) {
        boolean playerMoved = false;

        if (gadgetInAttesa != null) {
            playerMoved = usaGadgetConDirezione(move);
            gadgetInAttesa = null;
        } else {
            switch (move) {
                case 'x':
                    gui.tornaAlMenu();
                    return;
                case 'w', 'a', 's', 'd':
                    criminal.muovi(move, maze);
                    playerMoved = true;
                    break;
                case '1':
                    gestisciUsoGadgetMenu(criminal, "distruggi mura");
                    break;
                case '2':
                    gestisciUsoGadgetMenu(criminal, "salta mura");
                    break;
                case '3':
                    gestisciUsoGadgetMenu(criminal, "muove diagonale");
                    break;
                case '4':
                    playerMoved = gestisciUsoGadgetMenu(criminal, "bomba");
                    break;
                case '5':
                    playerMoved = gestisciUsoGadgetMenu(criminal, "avvicina uscita");
                    break;
            }
        }

        if (playerMoved) {
            controllore.controllaTrappole(criminal.getX(), criminal.getY());
            controllore.controllaGadget(criminal.getX(), criminal.getY(), tuttiGadget, maze, criminal);

            boolean perso = gui.controllaPerdita();
            boolean vinto = gui.controllaVittoria();

            repaint();

            if (vinto) {
                gui.mostraVittoria();
            } else if (perso) {
                gui.mostraSconfitta();
            }
        }
    }

    /**
     * Verifica la disponibilità di un gadget nell'inventario del criminale
     * e decide se attivarlo immediatamente o metterlo in attesa di una direzione.
     *
     * @param criminal L'oggetto giocatore.
     * @param nome     Il nome identificativo del gadget richiesto.
     * @return {@code true} se l'azione consuma un turno/movimento, {@code false}
     *         se il gadget viene messo in attesa o non è disponibile.
     */
    private boolean gestisciUsoGadgetMenu(Criminal criminal, String nome) {
        Gadget daUsare = null;

        for (Gadget g : criminal.getGadgetUtilizzabili()) {
            if (g != null && g.getNome().equals(nome)) {
                daUsare = g;
                break;
            }
        }

        if (daUsare == null) {
            return false;
        }

        if (nome.equals("distruggi mura") || nome.equals("salta mura") || nome.equals("muove diagonale")) {
            gadgetInAttesa = nome;
            return false;
        }

        daUsare.usa(' ');
        finalizzaUso(daUsare);
        return true;
    }

    /**
     * Utilizza un gadget precedentemente selezionato fornendogli una specifica
     * direzione, verificando che la direzione sia valida per quel particolare gadget.
     *
     * @param direzione Il carattere che indica la direzione di esecuzione del gadget.
     * @return {@code true} se il gadget è stato utilizzato correttamente,
     *         {@code false} in caso di direzione non valida o errore.
     */
    private boolean usaGadgetConDirezione(char direzione) {
        Gadget daUsare = null;

        for (Gadget g : criminal.getGadgetUtilizzabili()) {
            if (g != null && g.getNome().equals(gadgetInAttesa)) {
                daUsare = g;
                break;
            }
        }

        if (daUsare != null) {
            if (gadgetInAttesa.equals("muove diagonale")) {
                if ("qezc".indexOf(direzione) == -1) return false;
            } else {
                if ("wasd".indexOf(direzione) == -1) return false;
            }

            daUsare.usa(direzione);
            finalizzaUso(daUsare);
            return true;
        }
        return false;
    }

    /**
     * Registra il completamento dell'uso di un gadget, rimuovendolo dall'inventario
     * disponibile del giocatore e aggiungendolo alla lista di quelli consumati.
     *
     * @param g L'oggetto {@link Gadget} appena utilizzato.
     */
    private void finalizzaUso(Gadget g) {
        criminal.rimuoviGadget(g);
        if (cGadgetUsati < gadgetUsati.length) {
            gadgetUsati[cGadgetUsati] = g;
            cGadgetUsati++;
        }
    }

    /**
     * Verifica se un determinato gadget è già stato consumato in questa sessione.
     *
     * @param g L'oggetto {@link Gadget} da cercare.
     * @return {@code true} se il gadget è già stato usato, {@code false} altrimenti.
     */
    public boolean gadgetUsato(Gadget g) {
        for (int i=0; i<cGadgetUsati; i++) {
            if (gadgetUsati[i].equals(g)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sovrascrive il metodo di pittura principale del pannello.
     * Si occupa di calcolare la centratura della visuale sul giocatore e di
     * disegnare su schermo pavimenti, muri, uscita, gadget a terra, nemici
     * e infine il giocatore stesso.
     *
     * @param g Il contesto grafico {@link Graphics} fornito da Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (maze == null) return;

        int righe = maze.getRighe();
        int colonne = maze.getColonne();

        int cellSize = 40;

        int playerPixelX = (criminal != null ? criminal.getX() : 0) * cellSize + (cellSize / 2);
        int playerPixelY = (criminal != null ? criminal.getY() : 0) * cellSize + (cellSize / 2);

        int offsetX = (getWidth() / 2) - playerPixelX;
        int offsetY = (getHeight() / 2) - playerPixelY;

        int mazePixelWidth = colonne * cellSize;
        int mazePixelHeight = righe * cellSize;

        if (mazePixelWidth < getWidth()) {
            offsetX = (getWidth() - mazePixelWidth) / 2;
        } else {
            offsetX = Math.max(-(mazePixelWidth - getWidth()), Math.min(0, offsetX));
        }

        if (mazePixelHeight < getHeight()) {
            offsetY = (getHeight() - mazePixelHeight) / 2;
        } else {
            offsetY = Math.max(-(mazePixelHeight - getHeight()), Math.min(0, offsetY));
        }

        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {
                int drawX = offsetX + x * cellSize;
                int drawY = offsetY + y * cellSize;

                if (maze.isMuro(x, y)) {
                    if (wallImage != null) {
                        g.drawImage(wallImage, drawX, drawY, cellSize, cellSize, this);
                    } else {
                        g.setColor(new Color(50, 50, 50));
                        g.fillRect(drawX, drawY, cellSize, cellSize);
                        g.setColor(new Color(70, 70, 70));
                        g.drawRect(drawX, drawY, cellSize, cellSize);
                    }
                } else {
                    if (groundImage != null) {
                        g.drawImage(groundImage, drawX, drawY, cellSize, cellSize, this);
                    } else {
                        g.setColor(new Color(30, 30, 30));
                        g.fillRect(drawX, drawY, cellSize, cellSize);
                    }
                    if (maze.isUscita(x, y)) {
                        g.setColor(new Color(34, 139, 34, 100));
                        g.fillRect(drawX, drawY, cellSize, cellSize);
                    }
                }
            }
        }

        if (tuttiGadget != null) {
            g.setColor(new Color(0, 191, 255));
            for (Gadget gad : tuttiGadget) {
                if (gad != null && !gadgetPreso(gad) && !gadgetUsato(gad)) {
                    int drawX = offsetX + gad.getX() * cellSize;
                    int drawY = offsetY + gad.getY() * cellSize;
                    int spriteSize = (int) (cellSize * 1.2);
                    int spriteOffset = (spriteSize - cellSize) / 2;
                    switch (gad.getNome()) {
                        case "distruggi mura":
                            g.drawImage(distuggiMuraImage, drawX - spriteOffset, drawY - spriteOffset, spriteSize, spriteSize, this);
                            break;
                        case "salta mura":
                            g.drawImage(saltaMuraImage, drawX - spriteOffset, drawY - spriteOffset, spriteSize, spriteSize, this);
                            break;
                        case "muove diagonale":
                            g.drawImage(diagonaleImage, drawX - spriteOffset, drawY - spriteOffset, spriteSize, spriteSize, this);
                            break;
                        case "bomba":
                            g.drawImage(bombImage, drawX - spriteOffset, drawY - spriteOffset, spriteSize, spriteSize, this);
                            break;
                        case "avvicina uscita":
                            g.drawImage(uscitaImage, drawX - spriteOffset, drawY - spriteOffset, spriteSize, spriteSize, this);
                            break;
                        default:
                            g.fillRect(drawX + cellSize / 3, drawY + cellSize / 3, cellSize / 3, cellSize / 3);
                            break;
                    }
                }
            }
        }

        if (maze.getNemici() != null) {
            g.setColor(Color.RED);
            for (Enemy enemy : maze.getNemici()) {
                int drawX = offsetX + enemy.getX() * cellSize;
                int drawY = offsetY + enemy.getY() * cellSize;
                if (policeImage != null) {
                    int spriteSize = (int) (cellSize * 1.2);
                    int spriteOffset = (spriteSize - cellSize) / 2;
                    g.drawImage(policeImage, drawX - spriteOffset, drawY - spriteOffset, spriteSize, spriteSize, this);
                } else {
                    g.fillOval(drawX + 4, drawY + 4, cellSize - 8, cellSize - 8);
                }
            }
        }

        if (criminal != null) {
            int drawX = offsetX + criminal.getX() * cellSize;
            int drawY = offsetY + criminal.getY() * cellSize;
            if (criminalImage != null) {
                int spriteSize = (int) (cellSize * 1.2);
                int spriteOffset = (spriteSize - cellSize) / 2;
                g.drawImage(criminalImage, drawX - spriteOffset, drawY - spriteOffset, spriteSize, spriteSize, this);
            } else {
                g.setColor(new Color(220, 20, 60));
                g.fillOval(drawX + 4, drawY + 4, cellSize - 8, cellSize - 8);
            }
        }

        drawHUD(g);
    }

    /**
     * Verifica se un gadget sulla mappa è già stato raccolto dal giocatore,
     * in modo da non renderizzarlo più sul terreno.
     *
     * @param g Il gadget da controllare.
     * @return {@code true} se è nell'inventario del criminale, {@code false} altrimenti.
     */
    private boolean gadgetPreso(Gadget g) {
        for (Gadget cg : criminal.getGadgetUtilizzabili()) {
            if (cg != null && cg.equals(g)) return true;
        }
        return false;
    }

    /**
     * Disegna l'Heads-Up Display (HUD) fornendo al giocatore le informazioni
     * essenziali sui controlli e sui gadget attualmente disponibili nell'inventario.
     *
     * @param g Il contesto grafico {@link Graphics} corrente.
     */
    private void drawHUD(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString("Mossa: Freccette/WASD | Esci: ESC/X", 10, 20);

        if (criminal != null && criminal.getGadgetCriminal()) {
            String inventoryStr = "";
            for (Gadget gad : criminal.getGadgetUtilizzabili()) {
                if (gad != null) {
                    // Mappa il nome del gadget al tasto corrispondente per chiarezza
                    String key = switch (gad.getNome()) {
                        case "distruggi mura" -> "1";
                        case "salta mura" -> "2";
                        case "muove diagonale" -> "3";
                        case "bomba" -> "4";
                        case "avvicina uscita" -> "5";
                        default -> "?";
                    };
                    inventoryStr += "[" + key + " - " + gad.getNome() + "]  ";
                }
            }
            g.drawString("Gadget: " + inventoryStr, 10, 540);
        }
    }
}