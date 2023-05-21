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
        KruskalII g = null;

        try {
            g = KruskalII.nacitajSubor("C:\\Users\\Admin\\Desktop\\grafy\\Algoritmy\\KruskalovAlgoritmus\\pr3.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        g.kruskalovAlgoritmusII();
    }
}
