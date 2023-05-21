package sk.uniza.fri;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 19. 5. 2022
 * Time: 13:52
 */
public class Main {

    public static void main(String[] args) {
        Algoritmus g = null;

        try {
            g = Algoritmus.nacitajSubor("C:\\Users\\Admin\\IdeaProjects\\FordFulkerson\\src\\Data.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        g.fordFulkersenAlgoritmus();
    }
}