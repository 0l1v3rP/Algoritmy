package sk.uniza.fri;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20. 3. 2022
 * Time: 20:32
 */
public class Main {

    public static void main(String[] args) {
        Digraf g = null;

        try {
            g = Digraf.nacitajSubor("C:\\Users\\Admin\\Desktop\\grafy\\Algoritmy\\FloydovAlgoritmus\\floydov.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        g.inicializujMatice();
        g.floydovAlgoritmus();
        g.najdiNajkratsiuCestu(3, 4);

    }
}
