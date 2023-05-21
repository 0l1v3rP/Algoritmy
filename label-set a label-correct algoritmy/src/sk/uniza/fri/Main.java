package sk.uniza.fri;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 21. 3. 2022
 * Time: 10:01
 */
public class Main {

    public static void main(String[] args) {
        Digraf g2 = null;

        try {
            g2 = Digraf.nacitajSubor("C:\\Users\\Admin\\Downloads\\ATG_DAT\\ShortestPath\\pr1.hrn");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        g2.labelSetAlgoritmus(19, 422);
    }
}
