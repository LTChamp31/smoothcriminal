package it.volta.smoothcriminal.gui;

import it.volta.smoothcriminal.core.CheckObjects;
import it.volta.smoothcriminal.core.CreateObjects;
import it.volta.smoothcriminal.core.LevelLoader;
import it.volta.smoothcriminal.model.Criminal;
import it.volta.smoothcriminal.model.Gadget;
import it.volta.smoothcriminal.model.Maze;
import it.volta.smoothcriminal.model.Trap;

import javafx.scene.input.KeyEvent;
import java.util.List;
import java.io.*;
import java.util.ArrayList;

public class GUIGameController {

    private Maze maze;
    private Criminal criminal;
    private List<Trap> traps;
    private Gadget[] allGadgets;
    private CheckObjects controllore;
    
    private GUIApplication app;
    private int[] currentLevelIndex;
    private char mode; // 'S' storia, 'A' allenamento, 'T' torneo
    private long startTime;
    private String playerName;

    public GUIGameController(GUIApplication app) {
        this.app = app;
    }

    public void startStoryMode(String name, int level) {
        this.mode = 'S';
        this.playerName = name;
        this.currentLevelIndex = new int[]{level - 1};
        loadLevel();
    }
    
    public void startTrainingMode() {
        this.mode = 'A';
        LevelLoader loader = new LevelLoader();
        this.maze = loader.loadLevel();
        initGameEntities();
        this.startTime = System.currentTimeMillis();
        app.showGameScreen();
    }
    
    public void startTournamentMode(String name) {
        this.mode = 'T';
        this.playerName = name;
        this.currentLevelIndex = new int[]{0}; 
        loadLevel();
    }

    private void loadLevel() {
        LevelLoader loader = new LevelLoader();
        if (mode == 'A') {
            this.maze = loader.loadLevel();
        } else {
            this.maze = loader.loadLevel(this.currentLevelIndex, mode);
        }
        initGameEntities();
        this.startTime = System.currentTimeMillis();
        app.showGameScreen();
    }
    
    private void initGameEntities() {
        this.criminal = new Criminal(maze.getInizioX(), maze.getInizioY());
        this.traps = CreateObjects.creaTrappole(maze, criminal);
        this.allGadgets = CreateObjects.creaGadget(maze, criminal);
        this.controllore = new CheckObjects(traps);
    }

    public Maze getMaze() { return maze; }
    public Criminal getCriminal() { return criminal; }

    public void handleKeyPress(KeyEvent event) {
        if (maze == null || criminal == null) return;

        boolean moved = false;
        Gadget gadgetToUse = null;

        switch (event.getCode()) {
            case W:
            case UP:
                criminal.muovi('w', maze);
                moved = true;
                break;
            case A:
            case LEFT:
                criminal.muovi('a', maze);
                moved = true;
                break;
            case S:
            case DOWN:
                criminal.muovi('s', maze);
                moved = true;
                break;
            case D:
            case RIGHT:
                criminal.muovi('d', maze);
                moved = true;
                break;
            case DIGIT1:
            case NUMPAD1:
                gadgetToUse = findGadgetByKey(1);
                break;
            case DIGIT2:
            case NUMPAD2:
                gadgetToUse = findGadgetByKey(2);
                break;
            case DIGIT3:
            case NUMPAD3:
                gadgetToUse = findGadgetByKey(3);
                break;
            case DIGIT4:
            case NUMPAD4:
                gadgetToUse = findGadgetByKey(4);
                break;
            case DIGIT5:
            case NUMPAD5:
                gadgetToUse = findGadgetByKey(5);
                break;
            case X:
            case ESCAPE:
                app.showMainMenu();
                return;
            default:
                break;
        }

        if (gadgetToUse != null) {
            handleGadgetUsage(gadgetToUse);
        }

        if (moved || gadgetToUse != null) {
            controllore.controllaTrappole(criminal.getX(), criminal.getY());
            controllore.controllaGadget(criminal.getX(), criminal.getY(), allGadgets, maze, criminal);
            
            checkGameState();
            app.updateGameScreen();
        }
    }

    private Gadget findGadgetByKey(int key) {
        for (Gadget g : criminal.getGadgetUtilizzabili()) {
            if (g != null && g.getTasto() == key) {
                return g;
            }
        }
        return null;
    }

    private void handleGadgetUsage(Gadget g) {
        char dir = ' ';
        if (g.getNome().equals("distruggi mura") || g.getNome().equals("salta mura") || g.getNome().equals("muove diagonale")) {
            String dirStr = app.askDirectionForGadget(g.getNome());
            if (dirStr != null && !dirStr.isEmpty()) {
                dir = Character.toLowerCase(dirStr.charAt(0));
            } else {
                return; 
            }
        }
        g.usa(dir);
        criminal.rimuoviGadget(g);
    }

    private void checkGameState() {
        if (controllaPerdita()) {
            app.showDefeatScreen();
            return;
        }
        if (controllaVittoria()) {
            long seconds = (System.currentTimeMillis() - startTime) / 1000;
            if (mode == 'S') {
                int nextLevel = currentLevelIndex[0] + 2; 
                salvaProgressoStoria(playerName, nextLevel);
                if (nextLevel <= contaLivelliStoria()) {
                    currentLevelIndex[0]++;
                    loadLevel();
                } else {
                    app.showVictoryScreen("Hai completato la modalità Storia, " + playerName + "!");
                }
            } else if (mode == 'A') {
                app.showVictoryScreen("Allenamento completato in " + seconds + " secondi!");
            } else if (mode == 'T') {
                salvaRecordTorneo(playerName, seconds, currentLevelIndex[0] + 1);
                app.showVictoryScreen("Torneo completato in " + seconds + " secondi!\\nGRANDE PROVA!");
            }
        }
    }

    private boolean controllaVittoria() {
        return maze.isUscita(criminal.getX(), criminal.getY());
    }

    private boolean controllaPerdita() {
        int cx = criminal.getX();
        int cy = criminal.getY();
        if (maze.getNemici() != null) {
            for (it.volta.smoothcriminal.model.Enemy e : maze.getNemici()) {
                if (e.getX() == cx && e.getY() == cy) return true;
                e.muovi(maze, criminal);
                if (e.getX() == cx && e.getY() == cy) return true;
            }
        }
        return false;
    }

    private int contaLivelliStoria() {
        int contatore = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("resources/livelli/livelliStoria.txt"))) {
            String riga;
            while ((riga = br.readLine()) != null) {
                if (riga.equals("---")) contatore++;
            }
        } catch (Exception e) {}
        return contatore;
    }

    private void salvaProgressoStoria(String nome, int livelloRaggiunto) {
        File f = new File("resources/record/salvataggiStoria.txt");
        List<String> righe = new ArrayList<>();
        boolean aggiornato = false;
        if (f.exists()) {
            try (BufferedReader r = new BufferedReader(new FileReader(f))) {
                String l;
                while ((l = r.readLine()) != null) {
                    String[] p = l.split(":");
                    if (p.length == 2 && p[0].equalsIgnoreCase(nome)) {
                        int esistente = Integer.parseInt(p[1]);
                        if (livelloRaggiunto > esistente) {
                            righe.add(nome + ":" + livelloRaggiunto);
                            aggiornato = true;
                        } else righe.add(l);
                    } else righe.add(l);
                }
            } catch (Exception e) {}
        }
        if (!aggiornato) righe.add(nome + ":" + livelloRaggiunto);
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            for (String riga : righe) { w.write(riga); w.newLine(); }
        } catch (Exception e) {}
    }

    private void salvaRecordTorneo(String nome, long secondi, int mappa) {
        File f = new File("resources/record", "recordMappa_" + mappa + ".txt");
        List<String> righe = new ArrayList<>();
        boolean trovato = false;
        if (f.exists()) {
            try (BufferedReader r = new BufferedReader(new FileReader(f))) {
                String l;
                while ((l = r.readLine()) != null) {
                    String[] p = l.split(":");
                    if (p.length == 2 && p[0].equalsIgnoreCase(nome)) {
                        trovato = true;
                        long esistente = Long.parseLong(p[1]);
                        if (secondi < esistente) righe.add(nome + ":" + secondi);
                        else righe.add(l);
                    } else righe.add(l);
                }
            } catch (Exception e) {}
        }
        if (!trovato) righe.add(nome + ":" + secondi);
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            for (String riga : righe) { w.write(riga); w.newLine(); }
        } catch (Exception e) {}
    }

    public int caricaProgressoStoria(String nome) {
        File f = new File("resources/record/salvataggiStoria.txt");
        if (f.exists()) {
            try (BufferedReader r = new BufferedReader(new FileReader(f))) {
                String l;
                while ((l = r.readLine()) != null) {
                    String[] p = l.split(":");
                    if (p.length == 2 && p[0].equalsIgnoreCase(nome)) {
                        return Integer.parseInt(p[1]);
                    }
                }
            } catch (Exception e) {}
        }
        return 1;
    }
}
