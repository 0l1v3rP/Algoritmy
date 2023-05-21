package sk.uniza.fri;

/**
 * 21. 3. 2022 - 10:01
 *
 * @author Admin
 */
public class Hrana {
    private int zaciatocny;
    private int koncovy;
    private int cena;

    public Hrana(int zaciatocny, int koncovy, int cena) {
        this.zaciatocny = zaciatocny;
        this.koncovy = koncovy;
        this.cena = cena;
    }

    public int getZaciatocny() {
        return this.zaciatocny;
    }

    public int getKoncovy() {
        return this.koncovy;
    }

    public int getCena() {
        return this.cena;
    }
}

