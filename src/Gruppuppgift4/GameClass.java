package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class GameClass {


//    Config config = new Config();//
//    int QuestionsinRound = config.getQuestionsinRound();
//    int roundsInGame = config.getRoundsInGame();


    List<Questions> completeList = new ArrayList<>();
    List<Questions> listOfGameQuest = new ArrayList<>();

    //    List<Questions> completeListUse = new ArrayList<>();
    List<Questions> categoryList = new ArrayList<>();
//    List<Questions> categoryListDjur = new ArrayList<>();
//    List<Questions> categoryListNatur = new ArrayList<>();
//    List<Questions> categoryListSport = new ArrayList<>();
//    List<Questions> categoryListCategory = new ArrayList<>();
//    List<Questions> activeList = new ArrayList<>();
//    List<Questions> wrongList = new ArrayList<>();
    //    List<Questions> listOfCategory = new ArrayList<>();

    Set<String> listOfCategory = new HashSet<String>();
    String natur = "natur";
    Questions question;


    public GameClass() {
        readList();
//        categoryListDjur = searchCategoryFromList("DJUR");
//        categoryListNatur = searchCategoryFromList(natur);
//        categoryListSport = searchCategoryFromList("SPORT");
//        categoryListCategory = searchCategoryFromList("CATEGORY");

        checkCategorys(completeList);
//        listOfCategory=checkCategorys(completeList);
//        question=randomQuestion("djur");


    }

    public List<Questions> readList() {
        Path path = Path.of("src/Gruppuppgift4/questions");

        try (BufferedReader br = new BufferedReader(new FileReader(path.toString()))) {

            String input;
            while ((input = br.readLine()) != null) {
                String[] ql = input.split(";");

                Questions quest = new Questions(ql[0], ql[1], ql[2], ql[3], ql[4], ql[5]);
                completeList.add(quest);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return completeList;
    }

    public Set<String> checkCategorys(List<Questions> questions) {
        for (Questions q : completeList) {
            listOfCategory.add(q.category);
//            System.out.println(listOfCategory.size());

        }

        return listOfCategory;
    }


    public List<Questions> searchCategoryFromList(String category) {
        for (Questions q2 : completeList) {
            if (q2.category.equalsIgnoreCase(category)) {
                categoryList.add(q2);
            }

        }

        return categoryList;
    }
    public List<Questions> clearList(List<Questions> questions) {
        questions.clear();
        return questions;
    }

    public List<Questions> listOfGameQuestions (List<Questions> categoryList) {
        Collections.shuffle(categoryList);
        for (Questions q : categoryList) {
            if(q.unused){
                listOfGameQuest.add(q);
                q.unused = false;
            }
        }
        return listOfGameQuest;
    }

}





//    public
//}

//    public Questions randomQuestion(String category) {}

//        Random rand = new Random();
//        int siffra = rand.nextInt(0, categoryList.size());
//        if (category.equalsIgnoreCase("DJUR")) {
//            categoryListDjur = searchCategoryFromList("DJUR");
//            return categoryListDjur.get(rand.nextInt(categoryListDjur.size()));
//        }
//        categoryList.get(siffra);
//        activeList.add(categoryList.get(siffra));
//        activeList.add(categoryList.get(siffra));
//        activeList.add(categoryList.get(siffra));
        // Fixa så den lägger in 3 olika RANDOMS

//            return categoryList.get(rand.nextInt(categoryList.size())); //?}



//    return activeList;

//    }
//}
//    public String setGameQuestions() {
//        readList();
//        wrongList = searchCategoryFromList();
//        System.out.println(wrongList.getFirst().question);
//        System.out.println(activeList.size());
//        gameQuestion = wrongList.getFirst().question;
//        setGameAnswers();
//        return gameQuestion;
//    }

//    public String[] setGameAnswers() {
//
//        gameQuestion = wrongList.get(0).question;
//        gameAnswers = new String[]{wrongList.get(0).answer, wrongList.get(0).wrong1, wrongList.get(0).wrong2, wrongList.get(0).wrong3};
//        wrongList.get(0).setAnswer(gameAnswers[0]);
//        return gameAnswers;
//    }


/*
 *
 * input namn
 * anslutning upprättad
 * fråga klient 1 väntar klient2
 * sökerKategri()  -
 * 10 frågor lista
 * 3frågor random3 1,5,9 (1)  -
 *
 * SkrivUtFråga()
 * knapp1 = SVAR
 * knapp2 = Felsvar
 * knapp3 = Felsvar
 * knapp4 = Felsvar
 * *Collections.shullfe
 *
 *
 *     * kategori;fråga;SVAR;Felsvar;Felsvar;Felsvar;
 * reader:  delar upp i 5 delar (kategori)(1fråga) (4svar)
 *alltid svar 1 rätt.
 * lägger in i en arraylist

 *lägger ut på
 *
 *
 *     public List<Questions> searchCategoryFromList(String category) {
        for (Questions q2 : completeList) {
            if (q2.category.equalsIgnoreCase(category)) {
                categoryList.add(q2);
            }
        }if (category.equalsIgnoreCase("DJUR")) {
            return  categoryListDjur;
        }else if (category.equalsIgnoreCase("Natur")) {
            return  categoryListNatur;
        }
        else if (category.equalsIgnoreCase("SPORT")) {
            return  categoryListSport;
        }else if (category.equalsIgnoreCase("Category")) {
            return  categoryList;
        }else
        return null;
    }
 * */

