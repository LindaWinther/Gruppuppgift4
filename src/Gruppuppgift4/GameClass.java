package Gruppuppgift4;

import java.util.ArrayList;
import java.util.List;

public class GameClass {


    String p1 = "Player1";
    String p2 = "Player2";
    List<String> q = new ArrayList<>();


    public GameClass() {
        //    * kategori;fråga;SVAR;Felsvar;Felsvar;Felsvar;
        // * reader:  delar upp i 5 delar (kategori)(1fråga) (4svar)
    }


    public List<Medlem> readList() {
        Path m = Path.of("src/medlemsregister.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(m.toString()))) {
            br.readLine();

            String input;
            while ((input = br.readLine()) != null) {
                String[] medlem = input.split(";");

                Medlem ny = new Medlem(medlem[0], medlem[1], medlem[2], medlem[3], medlem[4], medlem[5], medlem[6]);
                list.add(ny);
            }
        }
        return List.of();
    }


    public searchCatogoryFromList {
        // tar ut djur och natur
    }

    public searchQuestionsFromList {
//            väljer ut 3 frågor om d&N
    }


}


    /*
    *
    * input namn
    * anslutning upprättad
    * fråga klient 1 väntar klient2
    * sökerKategri()
    * 10 frågor lista
    * 3frågor random3 1,5,9 (1)
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
    *
    * package sprint2.BestGymEver;

import java.time.LocalDate;


public void loadMemberFile(String file) {//Jag kan inte lämna parantesen tom men den används inte
        try (BufferedReader br = new BufferedReader(new FileReader("src/sprint2/BestGymEver/Memberfile.txt"))) {
-
* -
* -
*

    * */

