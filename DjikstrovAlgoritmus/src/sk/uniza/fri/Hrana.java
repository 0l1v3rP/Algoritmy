package sk.uniza.fri;

/**
 * 20. 3. 2022 - 17:35
 *
 * @author Admin
 */
public class Hrana {
    private int zaciatocny;
    private int koncovy;
    private int cena;

    public Hrana(int zaciatocna, int koncova, int cena) {
        this.zaciatocny = zaciatocna;
        this.koncovy = koncova;
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
