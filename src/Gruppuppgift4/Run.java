package Gruppuppgift4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Run {

    List<Questions> questions = new ArrayList<Questions>();
    GameClass game = new GameClass();
    Set<String> list = new HashSet<String>();

    public Run() {
        questions = game.readList();
        list = game.checkCategorys(questions);
        System.out.println(list.size());
        System.out.println(list);

    }


    void main() {
    }
}


//    List<Questions> lista = new ArrayList<Questions>();
//    public Main() {
//
//    {
//     System.out.println("Hello World");
//        GameClass g = new GameClass();
//        g.readList();
//        g.searchCategoryFromList();
//        lista = g.searchQuestionsFromList();
//        System.out.println(lista.get(1).answer);
//        System.out.println("11:04");
//    }
//
//

//