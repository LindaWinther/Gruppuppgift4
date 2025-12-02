package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class GameClass {


//    Config config = new Config();
//    int QuestionsinRound = config.getQuestionsinRound();
//    int roundsInGame = config.getRoundsInGame();


    List<Questions> completeList = new ArrayList<>();
    List<Questions> categoryList = new ArrayList<>();
    Set<String> listOfCategory = new HashSet<String>();


    public GameClass() {
        readList();
        checkCategorys(completeList);


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

