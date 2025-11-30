package Gruppuppgift4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    public static void main(String[] args) throws IOException {

//        GameClass game = new GameClass();
//        Questions q = new Questions();
//
//        String fråga = game.setGameQuestions();
//        String [] svar = game.setGameAnswers();
//
//        game.readList();
//        game.searchCategoryFromList();
//        List<Questions> questions = game.searchQuestionsFromList();
//        Questions q1 = game.searchQuestionsFromList();
//        Questions q2 = game.searchQuestionsFromList();
        //Questions q1 = questions.get(0)
        // Questions q2 = questions.get(1);

        ServerSocket server = new ServerSocket(55555);
        System.out.println("Server is running!");

        Socket p1 = server.accept();
        Socket p2 = server.accept();

        ClientHandler s1 = new ClientHandler(p1, '1');
        ClientHandler s2 = new ClientHandler(p2, '2');

        s1.opponent = s2;
        s2.opponent = s1;

        s1.start();
        s2.start();

    }
}
