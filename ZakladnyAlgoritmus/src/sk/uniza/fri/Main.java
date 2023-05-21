package sk.uniza.fri;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20. 3. 2022
 * Time: 15:28
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Digraf g = null;

        try {
            g = Digraf.nacitajSubor("C:\\Users\\Admin\\Desktop\\grafy\\Algoritmy\\ZakladnyAlgoritmus\\zakladny.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        g.zakladny(3);
        g.printInfo();
    }
}
