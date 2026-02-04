package it.volta.smoothcriminal.core;

public class Entita {
    protected int x;
    protected int y;

    public Entita(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY(){
        return y;
    }
    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }
}
