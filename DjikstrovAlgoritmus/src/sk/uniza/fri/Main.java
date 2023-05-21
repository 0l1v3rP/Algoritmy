package sk.uniza.fri;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20. 3. 2022
 * Time: 17:35
 */
public class Main {

    public static void main(String[] args) {
        Digraf g = null;

        try {
            g = Digraf.nacitajSubor("C:\\Users\\Admin\\Desktop\\grafy\\Algoritmy\\DjikstrovAlgoritmus\\Djikstrov.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        g.djikstrovDoJednehoVrchola(3, 4);
        g.djikstrovDoVsetkychVrcholov(3);

    }
}
