package sk.uniza.fri;

import sk.uniza.fri.Hrana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 19. 5. 2022 - 13:52
 *
 * @author Admin
 */

public class Algoritmus {


    private final int n; // pocet vrcholov grafu
    private final int m; // pocet hran grafu

    private Hrana[] h; // pole udajov o hranach


    private int zdroj;
    private int ustie;

    private ArrayList<Hrana> tok;

    private static final int INFINITY = 9999;

    public Algoritmus(int paPocetVrcholov, int paPocetHran) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new Hrana[1 + this.m];
        this.tok = new ArrayList<>();

    }


    /*
    Nacitanie grafu zo suboru:
    Na kazdom riadku su tri cisla, prve a druhe cislo su cisla vrcholov
    a tretie cislo je ohodnotenie hrany.
    Pocet vrcholov aj pocet hran sa urci automaticky. Pocet hran je rovny
    poctu riadkov v subore a pocet vrcholov je rovny najvacsiemu cislu
    vrcholu v udajoch o hranach.
    */
    public static Algoritmus nacitajSubor(String nazovSuboru)
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
            int y = 0;
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
        Algoritmus algorimus = new Algoritmus(pocetVrcholov, pocetHran);
        // po druhy krat otvorim ten isty subor,
        // uz pozanm pocet vrcholov aj hran a mam alokovanu pamat
        s = new Scanner(new FileInputStream(nazovSuboru));
        // postune nacitam vsetky hrany
        // tentokrat si ich uz budem aj ukladat do pamate
        for (int j = 1; j <= pocetHran; j++) {
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();
            int y = 0;
            algorimus.h[j] = new Hrana(u, v, c, y);

        }

        return algorimus;
    }

    public void fordFulkersenAlgoritmus() {
        this.inicializuj();

        int[] zvCesta = this.najdiZvecsujucuPolocestu();

        while (zvCesta != null) {
            int rezerva = zistiRezervu(zvCesta);
            this.nastaToky(rezerva, zvCesta);
            zvCesta = this.najdiZvecsujucuPolocestu();
        }

        System.out.println("maximalny tok: "  + this.getMaximalnyTok());

        this.vypisTokov();
        System.out.println("zdroj: " + this.zdroj );
    }

    private void vypisTokov() {

        for (int i = 1; i < this.h.length; i++) {
            System.out.println("Hrana: (" + this.h[i].getZaciatocny() + ", " + this.h[i].getKoncovy()
                    + ")  tok:" + this.h[i].getCena() + " / " + this.h[i].getTok()) ;
        }

    }

    private int getMaximalnyTok() {
        ArrayList<Integer> fstar;
        fstar = getFstar(this.zdroj);
        int maximalnyTok = 0;
        for (Integer vrchol:
             fstar) {
            maximalnyTok += this.getHrana(this.zdroj, vrchol).getTok();
        }
        return maximalnyTok;
    }

    private void nastaToky(int rezerva, int[] cesta) {
        for (int i = 0; i < cesta.length - 1; i++) {
            Hrana hrana;
            if (cesta[i] > 0) {
                hrana = getHrana(cesta[i], cesta[i + 1 ]);
                hrana.pridajTok(rezerva);
            } else {
                hrana = getHrana(cesta[i + 1], cesta[i]);
                hrana.pridajTok(-rezerva);
            }
        }

    }

    private int zistiRezervu(int[] cesta) {
        int rezervaCesty = INFINITY;
        for (int i = 0; i < cesta.length - 1; i++) {
            Hrana hrana;
            if (cesta[i] > 0) {
                hrana = getHrana(cesta[i], cesta[i + 1 ]);
            } else {
                hrana = getHrana(cesta[i + 1], cesta[i]);
            }
            if (hrana.getCena() - hrana.getTok() < rezervaCesty) {
                rezervaCesty = hrana.getCena() - hrana.getTok();
            }
        }
        return rezervaCesty;
    }

    public void inicializuj() {
        //urci zdroj

        for (int i = 1; i <= this.n; i++) {
            if (this.getIdeg(i) == 0) {
                this.zdroj = i;

            }
        }

        // urci ustie

        for (int i = 1; i <= this.n; i++) {
            if (this.getOdeg(i) == 0) {
                this.ustie = i;

            }
        }

    }

    private int[] najdiZvecsujucuPolocestu() {

        ArrayList<Integer> mnozinaE = new ArrayList<>();

        mnozinaE.add(this.zdroj);


        int[] x = new int[this.n + 1];

        // nastav svetkym vrcholom aokrem zdroja znacku nekonecna
        for (int i = 1; i < x.length; i++) {
            if (i != this.zdroj) {
                x[i] = INFINITY;
            } else {
                x[i] = 0;
            }
        }
        //pokial mnozina E neni prazdna a ustie ma stale znacku nekonecna
        while (!mnozinaE.isEmpty() && x[this.ustie] == INFINITY) {

            // nastav a odstran prvy prvok z mnoziny E ako aktualny vrchol
            int aktualnyvrchol = mnozinaE.remove(0);

            //vystupna hviezda aktualneho vrchola
            ArrayList<Integer> fstar = this.getFstar(aktualnyvrchol);

            //prechadzaj fstar
            for (int vrchol : fstar) {


                //ak sa znacka vrchola stale rovna nekonecnu
                if (x[vrchol] == INFINITY) {
                    //pokial tok nieje maximalny
                    if (this.getHrana(aktualnyvrchol, vrchol).getCena() > this.getHrana(aktualnyvrchol, vrchol).getTok() ) {
                        x[vrchol] = aktualnyvrchol;
                        mnozinaE.add(vrchol);

                    }
                }
            }

            //vstupna hviezda aktualneho vrchola

            ArrayList<Integer> bstar = getBstar(aktualnyvrchol);
            for (int vrchol : bstar) {
                if (x[vrchol] == INFINITY) {
                    if (this.getHrana(vrchol, aktualnyvrchol).getTok() > 0) {
                        x[vrchol] = -aktualnyvrchol;
                        mnozinaE.add(vrchol);

                    }
                }
            }
                //ak E obsahuje ustie
            if (mnozinaE.contains(this.ustie)) {
                ArrayList<Integer> zvecsujucaPolcesta = new ArrayList<>();

                int aktualny = this.ustie;
                while (aktualny != this.zdroj) {
                    zvecsujucaPolcesta.add(aktualny);
                    aktualny = x[aktualny];
                }
                zvecsujucaPolcesta.add(aktualny);

                int[] vysledokVOpacnomPoradi = new int[zvecsujucaPolcesta.size()];
                for (int i = 0; i < zvecsujucaPolcesta.size(); i++) {
                    vysledokVOpacnomPoradi[i] = zvecsujucaPolcesta.get(zvecsujucaPolcesta.size() - i - 1);

                }


                return vysledokVOpacnomPoradi;
            }
        }


        return null;
    }


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


    private ArrayList<Integer> getFstar(int v) {
        ArrayList pom = new ArrayList<>();
        for (int i = 1; i < this.h.length; i++) {
            if (this.h[i].getZaciatocny() == v) {
                pom.add(this.h[i].getKoncovy());
            }
        }
        return pom;
    }
    private ArrayList<Integer> getBstar(int v) {
        ArrayList pom = new ArrayList<>();
        for (int i = 1; i < this.h.length; i++) {
            if (this.h[i].getKoncovy() == v) {
                pom.add(this.h[i].getZaciatocny());
            }
        }
        return pom;
    }


    private Hrana getHrana(int zVrchola, int doVrchola) {
        for (int i = 1; i < this.h.length; i++) {
            if (this.h[i].getZaciatocny() == zVrchola && this.h[i].getKoncovy() == doVrchola) {
                return this.h[i];
            }
        }
        return null;

    }
}
