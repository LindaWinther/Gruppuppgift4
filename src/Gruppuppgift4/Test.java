package Gruppuppgift4;

import java.util.ArrayList;
import java.util.List;

public class Test {

        GameClass game =  new GameClass();
        List<String> tryMe = new ArrayList<>();
    public Test() {

//        sendCategories();
//        sendCategories();

//        tryMe = game.getAvailableCategories(2);
//        System.out.println("Got " + tryMe.toString());
//        tryMe = game.getAvailableCategories(2);
//        System.out.println(tryMe.get(1));
//        System.out.println("Got " + tryMe.toString());



    }
//    private void sendCategories(){
//        List<String> categoriesLeft = new ArrayList<>();
//        for (String category : game.listOfCategory){
//            int amountOfUnusedLeft = 0;
//            for(Questions question : game.completeList){
//                if (question.category.equalsIgnoreCase(category) && (question.unused == true)){
//                    amountOfUnusedLeft++;
//                }
//            }
//            if (amountOfUnusedLeft >= 2){
//                categoriesLeft.add(category);
//            }
//            System.out.println(category + "har" + amountOfUnusedLeft + " frågr kvar");
//        }
//    }


    void main(){}
}

//    List<Questions> questions = new ArrayList<Questions>();
//    List<Questions> hamburgare =  new ArrayList<>();
//    GameClass game = new GameClass();
//    Set<String> list = new HashSet<String>();
//    Questions q;
//
//
//        questions = game.readList();
//       hamburgare = game.getQuestions("djur",questions,hamburgare);
//        System.out.println(hamburgare.size());
//        System.out.println(hamburgare.getFirst().isUnused());
//        System.out.println(hamburgare.get(1).isUnused());
//        q = game.getNextQ(hamburgare);
//
//        System.out.println(q.category);
//        q =  game.getNextQ(hamburgare);
//        System.out.println(q.category);

//        String [] stringers = {"hej","då", "halå", "hejdå"};
//
//        shuffle(stringers);
//        for (int i = 0; i < stringers.length; i++){
//            System.out.println(stringers[i]+" ");
//        }
//
//
//    }
//    public void shuffle(Object[]array ){
//        int antalfrågor = array.length;
//
//        for (int i = 0; i <antalfrågor; i++){
//            int s = i + (int) (Math.random() * (antalfrågor - i));
//
//            Object temp = array[s];
//            array[s] = array[i];
//            array[i] = temp;
//        }
//
//
//    }



//        hamburgare = game.getQuestions("djur",questions,hamburgare,3);
//        q = hamburgare.getFirst();
//        System.out.println(q.getQuestion());



//        list = game.checkCategorys(questions);
//        System.out.println(list.size());
//        System.out.println(list);


//
//    void main() {
//    }
//}
//

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