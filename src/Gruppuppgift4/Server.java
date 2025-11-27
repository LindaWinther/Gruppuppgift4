package Gruppuppgift4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

    public static void main(String[] args) throws IOException {

        GameClass game = new GameClass();
        game.readList();
        game.searchCategoryFromList();
        List<Questions> questions = game.searchQuestionsFromList();

        Questions q1 = questions.get(0);
        Questions q2 = questions.get(1);

        ServerSocket server = new ServerSocket(55555);
        System.out.println("Server is running!");

        Socket p1 = server.accept();
        Socket p2 = server.accept();

        ServerSidePlayer s1 = new ServerSidePlayer(p1, '1', q1);
        ServerSidePlayer s2 = new ServerSidePlayer(p2, '2',q2);

        s1.start();
        s2.start();

        s1.sendMessage("Fråga;" + q1.question + ";" + q1.answer + ";" + q1.wrong1 + ";" + q1.wrong2 + ";" + q1.wrong3);
        s2.sendMessage("Fråga;" + q2.question + ";" + q2.answer + ";" + q2.wrong1 + ";" + q2.wrong2 + ";" + q2.wrong3);

    }
}
