package Gruppuppgift4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {


    public static void main(String[] args) throws IOException {

        GameClass game = new GameClass();
        Questions q = new Questions();

        String fråga = game.setGameQuestions();
        String [] svar = game.setGameAnswers();

//        game.readList();
//        game.searchCategoryFromList();
//        List<Questions> questions = game.searchQuestionsFromList();

//        Questions q1 = questions.get(0);
//        Questions q2 = questions.get(1);

        ServerSocket server = new ServerSocket(55555);
        System.out.println("Server is running!");

        Socket p1 = server.accept();
        Socket p2 = server.accept();

        ServerSidePlayer s1 = new ServerSidePlayer(p1, '1', q);
        ServerSidePlayer s2 = new ServerSidePlayer(p2, '2', q);

        s1.start();
        s2.start();

        s1.sendMessage("Fråga;" + fråga + ";" + svar[0] + ";" + svar[1] + ";" + svar[2] + ";" + svar[3]);
        s2.sendMessage("Fråga;" + fråga + ";" + svar[0] + ";" + svar[1] + ";" + svar[2] + ";" + svar[3]);

    }
}
