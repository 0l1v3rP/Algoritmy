package sk.uniza.fri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 21. 3. 2022 - 10:01
 *
 * @author Admin
 */
public class Digraf {
    private static final int INFINITY = 9999;

    private final int n; // pocet vrcholov grafu
    private final int m; // pocet hran grafu

    private Hrana[] h; // pole udajov o hranach
    private int[] t; //naktrasia najdena cesta
    private int[] x; // predchazajuci vrchol nejakeho vrcholu v najkratsiej ceste u - v orientovanej ceste
    private boolean[] b; //true - vrchol sa nachadza v mnozine E; false - nenachadza sa
    private ArrayList<Integer> mnozinaE; // mnozina riadiacich vrcholov

    public Digraf(int paPocetVrcholov, int paPocetHran) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new Hrana[1 + this.m];
        this.t = new int[this.n + 1];
        this.b = new boolean[this.n + 1];
        this.x = new int[this.n + 1];
        this.mnozinaE = new ArrayList<>();

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
    public void labelCorrectAlgoritmus(int u) {
        int riadiaci;
        for (int i = 1; i <= this.n; i++) {

            if (i == u) {
                this.t[i] = 0;
                this.b[i] = true;
                this.mnozinaE.add(i);
            } else {
                this.t[i] = INFINITY;
                this.b[i] = false;
            }
            this.x[i] = 0;

        }
        while (this.mnozinaE.size() != 0) {
            System.out.println("prvky mnoziny E: ");
            for (Integer i:
                 this.mnozinaE) {
                System.out.println(i);
            }
            riadiaci = this.mnozinaE.remove(0);

            for (int k = 1; k <= this.m; k++) {

                int i = this.h[k].getZaciatocny();
                int j = this.h[k].getKoncovy();
                int c = this.h[k].getCena();

                if (i == riadiaci && (this.t[j] > this.t[riadiaci] + c)) {
                    // prechadzam vsekty hrany incidentne s riadiacim vrcholom a hladam lepsiu cestu
                    this.t[j] = this.t[riadiaci] + c;
                    this.x[j] = riadiaci;
                    if (!this.b[j]) {
                        // zistujem ci sa tam dany vrchol uz nenachadza
                        this.mnozinaE.add(j);
                        this.b[j] = true;
                        System.out.println("vrchol" + j + "zaradujem do mnoziny E a menim znacku na: " + this.t[j] + "|" + i);
                    }



                }

            }
            this.b[riadiaci] = false;
            System.out.println("riadiaci vrchol" + riadiaci + "odstranujem z mnoziny E" );

        }
        //vypisem vsetky najktrasie cesty z vrchola u do kazdeho rozneho vrchola
        for (int i = 1; i <= this.n; i++) {
            if (i != u) {
                this.vypisCestu(u, i);
            }
        }
    }
    public void labelSetAlgoritmus(int u, int v) {
        int riadiaci;
        //inicializacia
        //KROK 1
        for (int i = 1; i <= this.n; i++) {

            if (i == u) {
                this.t[i] = 0;
                this.b[i] = true;
                this.mnozinaE.add(i);

            } else {
                this.t[i] = INFINITY;
                this.b[i] = false;

            }
            this.x[i] = 0;

        }
        //cyklus na hladanie najktrasiej orientovanej u - v cesty
        //KROK 2
        while (this.mnozinaE.size() != 0) {
//            System.out.println("prvky mnoziny E: ");
            for (Integer i :
                    this.mnozinaE) {
//                System.out.println(i);
            }
            int minT = 0;
            //vrchol s minimalnou znacokou z mnoziny E
            for (int i = 0; i < this.mnozinaE.size(); i++) {
                if (this.t[i] < this.t[minT]) {
                    minT = i;
                }
            }
            //za prvok riadiaci ∈ E vyberáme prvok z najmenšou značkou t()
            riadiaci = this.mnozinaE.remove(minT);

            for (int k = 1; k <= this.m; k++) {

                int i = this.h[k].getZaciatocny();
                int j = this.h[k].getKoncovy();
                int c = this.h[k].getCena();

                if (i == riadiaci && (this.t[j] > this.t[riadiaci] + c)) {
                    // prechadzam vsekty hrany incidentne s riadiacim vrcholom a hladam lepsiu cestu
                    this.t[j] = this.t[riadiaci] + c;
                    this.x[j] = riadiaci;
                    // zistujem ci sa tam dany vrchol uz nenachadza
                    if (!this.b[j]) {
                        this.mnozinaE.add(j);
                        this.b[j] = true;
                    }

//                    System.out.println("vrchol" + j + "zaradujem do mnoziny E a menim znacku na: " + this.t[j] + "|" + i);

                }

            }
            this.b[riadiaci] = false;
//            System.out.println("riadiaci vrchol" + riadiaci + "odstranujem z mnoziny E");

        }
        //vypisem vsetky najktrasie cesty z vrchola u do kazdeho rozneho vrchola
        //KROK 3

        this.vypisCestu(u, v);

    }



    public void vypisCestu(int u, int v) {
        System.out.println("Cesta z vrcholu: " + u + " do: "  + v);

        ArrayList<Integer> cesta = new ArrayList<>();
        cesta.add(v);
        int pom = v;
        int dalsi = this.x[v];
        while (dalsi != this.x[u]) {
            cesta.add(dalsi);

            dalsi = this.x[dalsi];


        }
        for (int i = cesta.size() - 1; i > 0 ; i--) {
            System.out.print(" " + cesta.get(i));
            System.out.print("(" + cesta.get(i) + ", " + cesta.get(i - 1) + ")" );
        }
        System.out.println("dlzka: " + this.t[pom]);
        System.out.println();
    }

}
