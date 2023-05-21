//package sk.uniza.fri;
//
//import java.io.FileNotFoundException;
//
///**
// * Created by IntelliJ IDEA.
// * User: Admin
// * Date: 21. 3. 2022
// * Time: 10:01
// */
//public class Main {
//
//    public static void main(String[] args) {
//        Digraf g = null;
//
//        try {
//            g = Digraf.nacitajSubor("C:\\Users\\Admin\\Desktop\\grafy\\Algoritmy\\KruskalovAlgoritmus\\pr3.txt");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        g.kruskalovAlgoritmusII();
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
public class KruskalII {


    private final int n; // pocet vrcholov grafu
    private final int m; // pocet hran grafu

    private Hrana[] h; // pole udajov o hranach
    private int[] k; //znacka k
    private ArrayList<Hrana> kostra; // kostra grafu

    public KruskalII(int paPocetVrcholov, int paPocetHran) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new Hrana[1 + this.m];
        this.kostra =  new ArrayList<>();
        this.k = new int[this.n + 1];

    }


    /*
    Nacitanie grafu zo suboru:
    Na kazdom riadku su tri cisla, prve a druhe cislo su cisla vrcholov
    a tretie cislo je ohodnotenie hrany.
    Pocet vrcholov aj pocet hran sa urci automaticky. Pocet hran je rovny
    poctu riadkov v subore a pocet vrcholov je rovny najvacsiemu cislu
    vrcholu v udajoch o hranach.
    */
    public static KruskalII nacitajSubor(String nazovSuboru)
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
        KruskalII kruskalII = new KruskalII(pocetVrcholov, pocetHran);

        // po druhy krat otvorim ten isty subor,
        // uz pozanm pocet vrcholov aj hran a mam alokovanu pamat
        s = new Scanner(new FileInputStream(nazovSuboru));

        // postune nacitam vsetky hrany
        // tentokrat si ich uz budem aj ukladat do pamate
        for (int j = 1; j <= pocetHran; j++) {
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();

            kruskalII.h[j] = new Hrana(u, v, c);

        }

        return kruskalII;
    }



    private void vymenHranu(int i, int j) {
        Hrana docasna = this.h[i];
        this.h[i] = this.h[j];
        this.h[j] = docasna;
    }

    //BUBBLE SORT
    public void sortH() {
        int zlepsenie = 1;
        while (zlepsenie != 0) {
            zlepsenie = 0;
            for (int i = 1; i < this.h.length - 1 ; i += 1) {
                if (this.h[i].getCena() > this.h[i + 1].getCena()) {
                    this.vymenHranu(i, i + 1);
                    zlepsenie = 1;

                }
            }
        }
    }

    public void kruskalovAlgoritmusII() {
        // sort podla ceny hran
        this.sortH();
        ArrayList<Hrana> postupnostP = new ArrayList<>();
        // nastav postupnost P
        for (int i = 1; i < this.h.length; i++) {
            postupnostP.add(this.h[i]);
        }
        //Pre každý vrchol i ∈ V polož k(i) = i.
        for (int i = 1; i < this.n; i++) {
            this.k[i] = i;
        }

        //hladanie najlacnejsiej kostry
        // Ak je počet vybraných hrán rovný |V| − 1 alebo ak je postupnosť P prázdna, STOP.
        while (this.kostra.size() < this.n - 1 && !postupnostP.isEmpty()) {
            //Vylúč hranu {u, v} z postupnosti P
            Hrana aktualnaHrana = postupnostP.remove(0);
            //Ak k(u) != k(v), zaraď hranu {u, v} do kostry
            if (this.k[aktualnaHrana.getZaciatocny()] != this.k[aktualnaHrana.getKoncovy()]) {
                this.kostra.add(aktualnaHrana);
                int kmin;
                int kmax;
                // najdi ktore k je max a ktore min
                if (this.k[aktualnaHrana.getZaciatocny()] > this.k[aktualnaHrana.getKoncovy()]) {
                    kmin = this.k[aktualnaHrana.getZaciatocny()];
                    kmax = this.k[aktualnaHrana.getKoncovy()];
                } else {
                    kmax = this.k[aktualnaHrana.getZaciatocny()];
                    kmin = this.k[aktualnaHrana.getKoncovy()];
                }
                //pokial sa kmax zhoduje s nejakym inym k, tak ho prepis na kmin
                for (int i = 1; i < this.n + 1; i++) {
                    if (this.k[i] == kmax) {
                        this.k[i] = kmin;
                    }
                }
            }
        }
        int cenaKostry = 0;
        System.out.println("vypis najlacnejsiej kostry");
        for (Hrana hrana:
            this.kostra) {
            System.out.println(hrana.getZaciatocny() + " " + hrana.getKoncovy() + " " + hrana.getCena());
            cenaKostry += hrana.getCena();
        }
        System.out.println("najlacnejsia cena kostry je: " + cenaKostry);
    }

}
