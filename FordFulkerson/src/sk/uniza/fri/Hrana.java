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
    private int tok;

    public Hrana(int zaciatocny, int koncovy, int cena, int tok ) {
        this.zaciatocny = zaciatocny;
        this.koncovy = koncovy;
        this.cena = cena;
        this.tok = tok;
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

    public void pridajTok(int tok) {
        this.tok += tok;
    }

    public int getTok() {
        return this.tok;
    }
}
