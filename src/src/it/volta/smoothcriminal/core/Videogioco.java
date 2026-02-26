package it.volta.smoothcriminal.core;

import it.volta.smoothcriminal.model.*;

import java.util.List;

/**
 * La classe astratta {@code Videogioco} definisce la logica di base e le regole
 * fondamentali per il funzionamento del gioco
 * Seguendo il pattern della logica di controllo, questa classe
 * gestisce lo stato del gioco, verifica le collisioni con i nemici e determina
 * il raggiungimento dell'obiettivo finale.
 * * @author Marco Caria & Lotan Teny
 */
public abstract class Videogioco {

    protected Criminal criminal;
    protected Labirinto labirinto;
    protected boolean inCorso;

    /**
     * Costruttore della classe {@code Videogioco}.
     * Inizializza i riferimenti al giocatore e al mondo di gioco, impostando
     * lo stato della partita come attivo.
     *
     * @param criminal  L'oggetto {@link Criminal} controllato dal giocatore.
     * @param labirinto L'oggetto {@link Labirinto} in cui sono posizionati ostacoli e nemici.
     */
    public Videogioco(Criminal criminal, Labirinto labirinto) {
        this.criminal = criminal;
        this.labirinto = labirinto;
        inCorso = true;
    }

    /**
     * Verifica se il giocatore ha raggiunto la posizione d'uscita del labirinto.
     * Se le coordinate del criminale coincidono con l'uscita, il gioco viene terminato.
     *
     * @return {@code true} se il giocatore ha vinto, {@code false} altrimenti.
     */
    public boolean controllaVittoria() {
        int x = criminal.getX();
        int y = criminal.getY();
        if (labirinto.isUscita(x,y)) {
            inCorso = false;
            return true;
        }
        return false;
    }

    /**
     * Gestisce la logica dei nemici e verifica la condizione di sconfitta.
     * Il metodo esegue due operazioni principali per ogni nemico presente nel labirinto:
     * Controlla se le coordinate del nemico coincidono con quelle del giocatore (collisione).
     * Se non c'è collisione, comanda al nemico di eseguire il proprio movimento verso il giocatore.
     * @return {@code true} se un nemico ha catturato il criminale, {@code false} se il giocatore è ancora al sicuro.
     */
    public boolean controllaPerdita() {
        int cx = criminal.getX();
        int cy = criminal.getY();
        int nx, ny;
        List<Nemico> nemico = labirinto.getNemici();

        for (int i = 0; i < nemico.size(); i++) {
            nx = nemico.get(i).getX();
            ny = nemico.get(i).getY();

            if(nx == cx && ny == cy) {
                return true;
            } else {
                nemico.get(i).muovi(labirinto, criminal);
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