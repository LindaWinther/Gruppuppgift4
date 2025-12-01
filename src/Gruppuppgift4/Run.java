package Gruppuppgift4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Run {

    List<Questions> questions = new ArrayList<Questions>();
    List<Questions> hamburgare =  new ArrayList<>();
    GameClass game = new GameClass();
    Set<String> list = new HashSet<String>();
    Questions q;

    public Run() {
        questions = game.readList();
//        hamburgare = game.getQuestions("djur",questions,hamburgare);
        System.out.println(hamburgare.size());
        System.out.println(hamburgare.getFirst().isUnused());
        System.out.println(hamburgare.get(1).isUnused());
        q = game.getNextQ(hamburgare);

        System.out.println(q.category);
        q =  game.getNextQ(hamburgare);
        System.out.println(q.category);
//        hamburgare = game.getQuestions("djur",questions,hamburgare,3);
//        q = hamburgare.getFirst();
//        System.out.println(q.getQuestion());



//        list = game.checkCategorys(questions);
//        System.out.println(list.size());
//        System.out.println(list);

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