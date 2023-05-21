package sk.uniza.fri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 20. 3. 2022 - 15:28
 *
 * @author Admin
 */
public class Digraf {
    private static final int INFINITY = 9999;

    private final int n; // pocet vrcholov grafu
    private final int m; // pocet hran grafu

    private Hrana[] h; // pole udajov o hranach
    private int[] t;
    private int[] x;

    public Digraf(int paPocetVrcholov, int paPocetHran) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new Hrana[1 + this.m];
        this.t  = new int[this.n + 1];
        this.x  = new int[this.n + 1];

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
    //u je zaciatocny vrchol
    public void zakladny(int u) {

        for ( int i = 1; i <= this.n; i++) {

            if (i == u ) {
                this.t[i] = 0;
            } else {
                this.t[i] = INFINITY;
            }
            this.x[i] = 0;

        }
        //Pre každý vrchol i ∈ V priraď dve značky t(i) a x(i).
        //{Značka t(i) predstavuje horný odhad dĺžky doteraz nájdenej najlepšej
        //u–i cesty a x(i) jej predposledný vrchol.}
        //Polož t(u) := 0, t(i) := ∞ pre i ∈ V, i =! u a x(i) := 0 pre každé i ∈ V .

        boolean jeZmena;
        do {
            jeZmena = false;
            for (int k = 1; k <= this.m ; k++) {
                int i = this.h[k].getZaciatocny();
                int j = this.h[k].getKoncovy();
                int c = this.h[k].getCena();

                if (this.t[j] > this.t[i] + c) {
                    this.t[j] = this.t[i] + c;
                    this.x[j] = i;

                    jeZmena = true;
                    System.out.println("Upravujem znacku pri vrchole " + j + ", na hodnotu: " + this.t[j] + "|" + this.x[j] + ".");
                }
            }
            if  (jeZmena) {
                System.out.println("Krok 2 treba opakovat");
            } else {
                System.out.println("Algoritmus konci");
            }

        } while (jeZmena);

        System.out.println("Idem vypisat vsetky najkratsie u - v cesty");
        for (int v = 1; v <= this.n; v++) {
            if (u != v) {
                this.vypisCestu(u, v);
            }
        }

    }

    public void vypisCestu(int u, int v) {
        System.out.println("Cesta z vrcholu: " + u + " do: "  + v);

        ArrayList<Integer> cesta = new ArrayList<>();
        cesta.add(v);
        int dalsi = this.x[v];
        while (dalsi != this.x[u]) {
            cesta.add(dalsi);
            System.out.println(dalsi);
            dalsi = this.x[dalsi];


        }
        for (int i = cesta.size() - 1; i >= 0 ; i--) {
            System.out.print(" " + cesta.get(i));
            if (i > 0) {
                System.out.print("(" + cesta.get(i) + ", " + cesta.get(i - 1) + ")" );
            }
        }
        System.out.println();
    }

    public void printInfo() {

        System.out.println("Pocet vrcholov: " + this.n);
        System.out.println("Pocet hran: " + this.m);
        System.out.println("Hrany: ");
        for (Hrana hr:
             this.h) {
            if (hr != null) {
                System.out.println( hr.getZaciatocny() + " " + hr.getKoncovy());
            }
        }
    }

}
