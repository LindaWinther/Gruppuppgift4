package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameClass {

    String p1 = "Player1";
    String p2 = "Player2";
    List<Questions> completeList = new ArrayList<>();
    List<Questions> categoryList = new ArrayList<>();
    List<Questions> activeList = new ArrayList<>();
    List<Questions> wrongList = new ArrayList<>();
    String gameQuestion;
    String [] gameAnswers;


    public GameClass() {
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

    public List<Questions> searchCategoryFromList() {
        for (Questions q2 : completeList) {
            if (q2.category.equalsIgnoreCase("djur")) {
                categoryList.add(q2);
            }
        }
        return categoryList;
    }

    public Questions searchQuestionsFromList() {

        Random rand = new Random();
        int siffra = rand.nextInt(0, categoryList.size());
        categoryList.get(siffra);
//        activeList.add(categoryList.get(siffra));
//        activeList.add(categoryList.get(siffra));
//        activeList.add(categoryList.get(siffra));
        // Fixa så den lägger in 3 olika RANDOMS
        return categoryList.get(rand.nextInt(categoryList.size())); //?


//    return activeList;

    }
    public String setGameQuestions() {
        readList();
        wrongList = searchCategoryFromList();
        System.out.println(wrongList.getFirst().question);
        System.out.println(activeList.size());
        gameQuestion = wrongList.getFirst().question;
        setGameAnswers();
        return gameQuestion;
    }

    public String[] setGameAnswers() {

        gameQuestion = wrongList.get(0).question;
        gameAnswers = new String[]{wrongList.get(0).answer, wrongList.get(0).wrong1, wrongList.get(0).wrong2, wrongList.get(0).wrong3};
        wrongList.get(0).setAnswer(gameAnswers[0]);
        return gameAnswers;
    }
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
 * */

