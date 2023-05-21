//package sk.uniza.fri;
//
//import java.io.FileNotFoundException;
//
///**
// * Created by IntelliJ IDEA.
// * User: Admin
// * Date: 7. 5. 2022
// * Time: 11:04
// */
//public class Main {
//
//    public static void main(String[] args) {
//        CPM g = null;
//
//        try {
//            g = CPM.nacitajSubor("C:\\Users\\Admin\\Algoritmus_3\\CPM_midi.hrn");
//            g.nacitajDobySpracovnaia("C:\\Users\\Admin\\Algoritmus_3\\CPM_midi.tim");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        g.rozvh();
//    }
//}

//package sk.uniza.fri;
//
///**
// * 21. 3. 2022 - 10:01
// *
// * @author Admin
// */
//public class Hrana {
//    private int zaciatocny;
//    private int koncovy;
//    private int cena;
//
//    public Hrana(int zaciatocny, int koncovy, int cena) {
//        this.zaciatocny = zaciatocny;
//        this.koncovy = koncovy;
//        this.cena = cena;
//    }
//
//    public int getZaciatocny() {
//        return this.zaciatocny;
//    }
//
//    public int getKoncovy() {
//        return this.koncovy;
//    }
//
//    public int getCena() {
//        return this.cena;
//    }
//}

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
public class CPM {


    private final int n; // pocet vrcholov grafu
    private final int m; // pocet hran grafu

    private Hrana[] h; // pole udajov o hranach
    private int[] z; // znacka z
    private int[] x; // znacka x
    private ArrayList<Integer> dobySpracovania;

    public CPM(int paPocetVrcholov, int paPocetHran) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new Hrana[1 + this.m];
        this.x = new int[this.n + 1];
        this.z = new int[this.n + 1];
        this.dobySpracovania = new ArrayList<>();
    }


    /*
    Nacitanie grafu zo suboru:
    Na kazdom riadku su tri cisla, prve a druhe cislo su cisla vrcholov
    a tretie cislo je ohodnotenie hrany.
    Pocet vrcholov aj pocet hran sa urci automaticky. Pocet hran je rovny
    poctu riadkov v subore a pocet vrcholov je rovny najvacsiemu cislu
    vrcholu v udajoch o hranach.
    */
    public static CPM nacitajSubor(String nazovSuboru)
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
        // vytvorim objekt grafu s potrebnym poctom vrcholov  aj hran
        CPM cpm = new CPM(pocetVrcholov, pocetHran);
        // po druhy krat otvorim ten isty subor,
        // uz pozanm pocet vrcholov aj hran a mam alokovanu pamat
        s = new Scanner(new FileInputStream(nazovSuboru));
        // postune nacitam vsetky hrany
        // tentokrat si ich uz budem aj ukladat do pamate
        for (int j = 1; j <= pocetHran; j++) {
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();
            cpm.h[j] = new Hrana(u, v, c);
        }
        return cpm;
    }
    public void rozvh() {
        //algoritmusNaUrcenieNajskorMoznychZaciatkov
        //Krok 1. Vytvor monotonne ocislovanie vrcholov digrafu
        ArrayList<Integer> monotonneOcislovanieVrcholov = this.monotonneOcislovanieVrcholov();
//       Krok 2. Kazdemu vrcholu v ∈ V prirad dve znacky z(v), x(v).
//       Pre kazde v ∈ V inicializacne poloz x(v) := 0, z(v) := 0.
        for (int i = 1; i < this.n + 1; i++) {
            this.x[i] = 0;
            this.z[i] = 0;
        }
        //Krok 3. Postupne pre k = 1, 2, . . . , n − 1 urob:
        int r = 0;
        ArrayList<Integer> fstar ;
        for (int i = 0; i < this.n ; i++) {
            //Poloz r = vk
            r = monotonneOcislovanieVrcholov.get(i);
            //Pre vsetky take vrcholy w vystupnej hviezdy vrchola r,
            fstar = this.getFstar(r);
            for (int w :
                    fstar) {
                //ze w != r, urob:
                if (r != w) {
                    //Ak z(w) < z(r) + p(r),
                    //potom z(w) := z(r) + p(r) a x(w) := r.
                    if (this.z[w] < (this.z[r] + this.dobySpracovania.get( r - 1 ))) {
                        this.z[w] = this.z[r] + this.dobySpracovania.get( r - 1) ;
                        this.x[w] = r;
                    }
                }
            }
        }
        // vypocet trvania projektu
        int trvanie = 0;
        for (int i = 1; i < this.n + 1; i++) {
            if (trvanie < this.z[i] + this.dobySpracovania.get( r - 1) && this.getOdeg(i) == 0 ) {
                trvanie = this.z[i] + this.dobySpracovania.get( r - 1);
            }
        }
        //algoritmusNaUrcenieNajneskor moznych koncov
        int[] k = new int[this.n + 1];
        int[] y = new int[this.n + 1];
        for (int i = 1; i < this.n + 1; i++) {
            k[i] = trvanie;
            y[i] = 0;
        }

        //Postupne pre i = n − 1, n − 2, . . . , 1 urob:
        for (int i = monotonneOcislovanieVrcholov.size() - 1; i >= 0 ; i--) {
            //Poloz r = vi
            r = monotonneOcislovanieVrcholov.get(i);
            //Pre vsetky take vrcholy w vystupnej hviezdy vrchola r,
            fstar = this.getFstar(r);
            for (int w :
                    fstar) {
                //ze w != r, urob:
                if (r != w) {
                    //Ak k(r) > k(w) − p(w),
                    //potom k(r) := k(w) − p(w) a y(r) := w .
                    if (k[r] > (k[w] - this.dobySpracovania.get( w - 1 ))) {
                        k[r] = k[w] - this.dobySpracovania.get( w - 1) ;
                        y[r] = w;
                    }
                }
            }
        }
        ArrayList<Integer> kritickaCesta = new ArrayList<>();
        for (int i = 1; i < this.n + 1; i++) {
            if (0 == (k[monotonneOcislovanieVrcholov.get( i - 1 )] - this.z[monotonneOcislovanieVrcholov.get( i - 1 )]
                    - this.dobySpracovania.get(monotonneOcislovanieVrcholov.get( i - 1 )  -1 ))) {
                kritickaCesta.add(monotonneOcislovanieVrcholov.get( i - 1 ));
            }
        }

        int[][] tabulka = new int[this.n + 1][5];
        for (int i = 1; i <= monotonneOcislovanieVrcholov.size() ; i++) {
            tabulka[i][0] = monotonneOcislovanieVrcholov.get( i - 1 );
            tabulka[i][1] = this.dobySpracovania.get(monotonneOcislovanieVrcholov.get( i - 1 ) - 1);
            tabulka[i][2] = this.z[monotonneOcislovanieVrcholov.get(i - 1)];
            tabulka[i][3] = k[monotonneOcislovanieVrcholov.get(i - 1) ];
            tabulka[i][4] = k[monotonneOcislovanieVrcholov.get(i - 1)] - this.dobySpracovania.get(monotonneOcislovanieVrcholov.get( i - 1 ) - 1) - this.z[monotonneOcislovanieVrcholov.get(i - 1) ];

        }
        System.out.println("1.stlpec - usporiadane vrcholy \n" +
                "2.stlpec - doby spracovania elemntarnych operacii \n" +
                "3.stlpec - najskor mozne zaciatky \n" +
                "4.stlpec - najneskor mozne konce \n" +
                "5.stlpec - rezerva");
        int s = 0;
        for (int i = 1; i < tabulka.length; i++) {
            for (int j = 0; j < tabulka[i].length; j++) {
                System.out.print(tabulka[i][j]);
                //formatovanie
                s = (int)(Math.log10(tabulka[i][j])) + 1 ;
                if (tabulka[i][j] == 0) {
                    s = 1;
                }
                for (int f = 0; f < 4 - s; f++) {
                    System.out.print(" ");
                }
                if (j != 4) {
                    System.out.print("| ");
                }
            }
            System.out.println();
        }




        System.out.println();
        System.out.println("vypis kritickej cesty: ");


        for (int i:
             kritickaCesta) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println("doba trvania: ");
        System.out.println(trvanie);
    }

    public void nacitajDobySpracovnaia(String nazovSuboru)
            throws FileNotFoundException  {
        // otvorim subor a pripravim Scanner pre nacitavanie
        Scanner s = new Scanner(new FileInputStream(nazovSuboru));
        while (s.hasNext()) {
            this.dobySpracovania.add(s.nextInt());
        }
    }

    private ArrayList<Integer> monotonneOcislovanieVrcholov() {
        //k - vysledna postupnost
        ArrayList<Integer> k = new ArrayList<>();
        ArrayList<Integer> fstar;
        // d(v) := ideg(v);
        int[] d = new int[this.n + 1];
        // pre kazdy jeden vrchol zisti jeho ideg
        for (int i = 1; i < d.length; i++) {
            d[i] = getIdeg(i);
            // vrcholy s ideg0
            if (getIdeg(i) == 0) {
                k.add(i);
            }
        }
        //Poloz i(pom) := 1(0)
        int pom = 0;
        while (k.size() != this.n) {
            //fstar vrchola i s postupnosti k
            fstar = getFstar(k.get(pom));
            //Postupne pre kazdy vrchol w vystupnej hviezdy vrchola Vi taky ze w!= Vi, urob:
            for (int w :
                    fstar) {
                if (w != k.get(pom)) {
                    d[w]--;
                }
                if (d[w] == 0) {
                    k.add(w);
                }
            }
            pom++;
        }
        return k;
    }
    // VYPOCITAJ ODEG VRCHOLA
    private int getOdeg(int vrchol) {
        int odeg = 0;

        for (int i = 1; i < this.h.length; i++) {
            if (this.h[i].getZaciatocny() == vrchol) {
                odeg++;
            }
        }
        return odeg;
    }
    // VYPOCITAJ IDEG VRCHOLA
    private int getIdeg(int vrchol) {
        int ideg = 0;

        for (int i = 1; i < this.h.length; i++) {
            if (this.h[i].getKoncovy() == vrchol) {
                ideg++;
            }
        }
        return ideg;
    }

    // VYSTUPNA HVIEZDA
    private ArrayList<Integer> getFstar(int v) {
        ArrayList pom = new ArrayList<>();
        for (int i = 1; i < this.h.length; i++) {
            if (this.h[i].getZaciatocny() == v) {
                pom.add(this.h[i].getKoncovy());
            }
        }
        return pom;
    }
}

