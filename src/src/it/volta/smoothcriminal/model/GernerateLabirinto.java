package it.volta.smoothcriminal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GernerateLabirinto {
    private int altezza, larghezza;
    private char[][] matrice;
    private Integer[][] direzioni = {{0,2},{0,-2},{2,0},{-2,0}};
    List<Integer[]> dirList = Arrays.asList(direzioni);

    public GernerateLabirinto(int altezza, int larghezza){
        this.altezza = altezza;
        this.larghezza = larghezza;
        matrice = new char[altezza][larghezza];

        for (int i=0; i<altezza; i++) {
            for (int j=0; j<larghezza; j++) {
                matrice[i][j] = '#';
            }
        }

    }

    public char[][] generateLabirinto(){
        crearePassagio(1,1);
        return matrice;
    }

    public void crearePassagio(int i, int j){
        matrice[i][j] = ' ';
        Collections.shuffle(dirList);

        for (Integer[] dir : dirList) {
            int prossimoI = i + dir[0];
            int prossimoJ = j + dir[1];
            if (prossimoI >= 0 && prossimoI < altezza && prossimoJ >= 0 && prossimoJ < larghezza && matrice[prossimoI][prossimoJ] == '#') {
                matrice[i + dir[0]/2][j + dir[1]/2] = ' ';

                crearePassagio(prossimoI, prossimoJ);
            }
        }
    }


}
