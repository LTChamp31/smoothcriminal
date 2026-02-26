package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;

import java.util.List;

/**
 * La classe astratta {@code VideoGame} definisce la logica di base e le regole
 * fondamentali per il funzionamento del gioco
 * Seguendo il pattern della logica di controllo, questa classe
 * gestisce lo stato del gioco, verifica le collisioni con i nemici e determina
 * il raggiungimento dell'obiettivo finale.
 * * @author Marco Caria & Lotan Teny
 */
public abstract class VideoGame {

    protected Criminal criminal;
    protected Maze maze;
    protected boolean inCorso;

    /**
     * Costruttore della classe {@code VideoGame}.
     * Inizializza i riferimenti al giocatore e al mondo di gioco, impostando
     * lo stato della partita come attivo.
     *
     * @param criminal L'oggetto {@link Criminal} controllato dal giocatore.
     * @param maze     L'oggetto {@link Maze} in cui sono posizionati ostacoli e
     *                 nemici.
     */
    public VideoGame(Criminal criminal, Maze maze) {
        this.criminal = criminal;
        this.maze = maze;
        inCorso = true;
    }

    /**
     * Verifica se il giocatore ha raggiunto la posizione d'uscita del maze.
     * Se le coordinate del criminale coincidono con l'uscita, il gioco viene
     * terminato.
     *
     * @return {@code true} se il giocatore ha vinto, {@code false} altrimenti.
     */
    public boolean controllaVittoria() {
        int x = criminal.getX();
        int y = criminal.getY();
        if (maze.isUscita(x, y)) {
            inCorso = false;
            return true;
        }
        return false;
    }

    /**
     * Gestisce la logica dei nemici e verifica la condizione di sconfitta.
     * Il metodo esegue due operazioni principali per ogni nemico presente nel maze:
     * Controlla se le coordinate del nemico coincidono con quelle del giocatore
     * (collisione).
     * Se non c'è collisione, comanda al nemico di eseguire il proprio movimento
     * verso il giocatore.
     * 
     * @return {@code true} se un nemico ha catturato il criminale, {@code false} se
     *         il giocatore è ancora al sicuro.
     */
    public boolean controllaPerdita() {
        int cx = criminal.getX();
        int cy = criminal.getY();
        int nx, ny;
        List<Enemy> enemy = maze.getNemici();

        for (int i = 0; i < enemy.size(); i++) {
            nx = enemy.get(i).getX();
            ny = enemy.get(i).getY();

            if (nx == cx && ny == cy) {
                return true;
            } else {
                enemy.get(i).muovi(maze, criminal);
                if (enemy.get(i).getX() == cx && enemy.get(i).getY() == cy) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Metodo astratto che deve essere implementato dalle classi derivate
     * per inizializzare e far partire il flusso di gioco specifico.
     *
     * @return {@code true} se l'avvio è avvenuto con successo.
     */
    public abstract boolean avvia();

}