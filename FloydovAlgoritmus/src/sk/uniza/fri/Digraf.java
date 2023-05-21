package sk.uniza.fri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 20. 3. 2022 - 20:32
 *
 * @author Admin
 */
public class Digraf {
    private static final int INFINITY = 9999;

    private final int n; // pocet vrcholov grafu
    private final int m; // pocet hran grafu
    private Hrana[] h; // pole udajov o hranach
    private int[][] maticaX;
    private int[][] maticaC;

    public Digraf(int paPocetVrcholov, int paPocetHran) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new Hrana[1 + this.m];
        this.maticaC = new int[this.n + 1][this.n + 1];
        this.maticaX = new int[this.n + 1][this.n + 1];
    }


    /*
    Nacitanie grafu zo suboru:
    Na kazdom riadku su tri cisla, prve a druhe cislo su cisla vrcholov
    a tretie cislo je ohodnotenie hrany.
    Pocet vrcholov aj pocet hran sa urci automaticky. Pocet hran je rovny
    poctu riadkov v subore a pocet vrcholov je rovny najvacsiemu cislu
    vrcholu v udajoch o hranach.
    */
    public static Digraf nacitajSubor(String nazovSuboru)
            throws FileNotFoundException {
        // otvorim subor a pripravim Scanner pre nacitavanie
        Scanner s = new Scanner(new FileInputStream(nazovSuboru));

        // najskor len zistim pocet vrcholov a pocet hran
        int pocetVrcholov = 1;
        int pocetHran = 0;
        // prejdem cely subor
        while (s.hasNext()) {
            // nacitam udaje o hrane
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();

            // nacital som hranu, zvysim ich pocet o 1
            pocetHran++;

            // skontrolujem, ci netreba zvysit pocet vrcholov
            if (pocetVrcholov < u) {
                pocetVrcholov = u;
            }
            if (pocetVrcholov < v) {
                pocetVrcholov = v;
            }
        }
        // ukoncim nacitavanie zo suboru
        s.close();

        // vytvorim objekt grafu s potrebnym poctom vrcholo  aj hran
        Digraf digraf = new Digraf(pocetVrcholov, pocetHran);

        // po druhy krat otvorim ten isty subor,
        // uz pozanm pocet vrcholov aj hran a mam alokovanu pamat
        s = new Scanner(new FileInputStream(nazovSuboru));

        // postune nacitam vsetky hrany
        // tentokrat si ich uz budem aj ukladat do pamate
        for (int j = 1; j <= pocetHran; j++) {
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();

            digraf.h[j] = new Hrana(u, v, c);

        }

        return digraf;
    }

    public void inicializujMatice() {
        for (int i = 1; i < this.maticaC.length; i++) {
            for (int j = 1; j < this.maticaC[i].length; j++) {
                if (i == j) {
                    this.maticaC[i][j] = 0;
                } else {
                    for (int k = 1; k <= this.m; k++) {

                        if (this.h[k].getZaciatocny() == i && this.h[k].getKoncovy() == j) {
                            this.maticaC[i][j] = this.h[k].getCena();
                        }
                    }
                    if (this.maticaC[i][j] == 0) {
                        this.maticaC[i][j] = INFINITY;
                    }


                }

            }

        }
        for (int i = 1; i < this.maticaX.length; i++) {
            for (int j = 1; j < this.maticaX[i].length; j++) {
                if (i == j) {
                    this.maticaX[i][j] = i;
                } else {
                    for (int k = 1; k <= this.m; k++) {
                        if (this.h[k].getZaciatocny() == i && this.h[k].getKoncovy() == j) {
                            this.maticaX[i][j] = i;
                        }
                    }
                    if (this.maticaX[i][j] == 0) {
                        this.maticaX[i][j] = INFINITY;
                    }


                }
            }
        }

    }

    public void floydovAlgoritmus() {
        for (int k = 1; k <= this.n; k++) {
            for (int i = 1; i < this.maticaC.length; i++) {
                if (k != i && this.maticaC[i][k] != INFINITY) {
                    for (int j = 1; j < this.maticaC[k].length; j++) {
                        if (k != i && this.maticaC[k][j] != INFINITY) {
                            if (this.maticaC[i][j] > this.maticaC[i][k] + this.maticaC[k][j]) {
                                this.maticaC[i][j] = this.maticaC[i][k] + this.maticaC[k][j];
                                this.maticaX[i][j] = this.maticaX[k][j];
                            }
                        }
                    }
                }
            }
        }
    }

    public void najdiNajkratsiuCestu(int i, int j) {
        System.out.println("Cesta z vrcholu: " + i + " do: "  + j);
        int doVrchola = j;
        ArrayList<Integer> cesta = new ArrayList<>();
        cesta.add(j);
        while (this.maticaX[i][j] != i) {
            cesta.add(this.maticaX[i][j]);
            j = this.maticaX[i][j];
        }
        cesta.add(i);
        for (int f = cesta.size() - 1; f > 0 ; f--) {
            System.out.print(" " + cesta.get(f));
            System.out.print("(" + cesta.get(f) + ", " + cesta.get(f - 1) + ")");

        }
        System.out.println("Cesta ma dlzku: " + this.maticaC[i][doVrchola]);

    }
}
