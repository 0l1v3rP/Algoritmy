package sk.uniza.fri;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 7. 5. 2022
 * Time: 11:04
 */
public class Main {

    public static void main(String[] args) {
        CPM g = null;

        try {
            g = CPM.nacitajSubor("C:\\Users\\Admin\\Algoritmus_3\\CPM_midi.hrn");
            g.nacitajDobySpracovnaia("C:\\Users\\Admin\\Algoritmus_3\\CPM_midi.tim");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        g.rozvh();
    }
}
