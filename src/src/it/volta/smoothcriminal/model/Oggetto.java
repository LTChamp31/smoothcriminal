package it.volta.smoothcriminal.model;


public abstract class Oggetto {

    protected int x, y;
    protected String nome;
    protected Labirinto labirinto;
    protected Criminal criminal;


    public Oggetto(Labirinto labirinto, Criminal criminal, int x, int y, String nome) {
        this.labirinto = labirinto;
        this.criminal = criminal;
        this.x = x;
        this.y = y;
        this.nome = nome;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public String getNome(){return nome;}

    public abstract void usa(char direzione);

}
