package it.volta.smoothcriminal.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneraLabirinto {
    private int altezza, larghezza;
    private char[][] matrice;
    private Integer[][] direzioniTemplate = {{0,2},{0,-2},{2,0},{-2,0}};

    public GeneraLabirinto(int altezza, int larghezza){
        this.altezza = altezza;
        this.larghezza = larghezza;
        matrice = new char[altezza][larghezza];

        for (int i=0; i<altezza; i++) {
            for (int j=0; j<larghezza; j++) {
                matrice[i][j] = '█';
            }
        }

    }

    public List<List<Character>> generateLabirinto(){
        crearePassagio(1,1);
        matrice[altezza-2][larghezza-1] = 'U';
        if (matrice[altezza-2][larghezza-2] == '█') {
            matrice[altezza-2][larghezza-2] = ' ';
        }

        List<List<Character>> mat = new ArrayList<>();

        for (char[] row : matrice) {
            List<Character> listRow = new ArrayList<>();
            for (char c : row) {
                listRow.add(c);
            }
            mat.add(listRow);
        }
        return mat;
    }

    public void crearePassagio(int i, int j){
        matrice[i][j] = ' ';
        List<Integer[]> localDirezioni = new ArrayList<>(Arrays.asList(direzioniTemplate));
        Collections.shuffle(localDirezioni);

        for (Integer[] dir : localDirezioni) {
            int prossimoI = i + dir[0];
            int prossimoJ = j + dir[1];

            if (prossimoI >= 0 && prossimoI < altezza-1 && prossimoJ >= 0 && prossimoJ < larghezza-1 && matrice[prossimoI][prossimoJ] == '█') {
                matrice[i + dir[0]/2][j + dir[1]/2] = ' ';

                crearePassagio(prossimoI, prossimoJ);
            }
        }
    }

    public int getAltezza() { return altezza; }
    public int getLarghezza() { return larghezza; }


}
