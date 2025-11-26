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

        Questions q = questions.get(0);


        ServerSocket server = new ServerSocket(55555);
        System.out.println("Server is running!");

        Socket p1 = server.accept();
        Socket p2 = server.accept();

        ServerSidePlayer s1 = new ServerSidePlayer(p1, '1', q);
        ServerSidePlayer s2 = new ServerSidePlayer(p2, '1',q);

        s1.start();
        s2.start();

        s1.sendMessage("Fråga:" + q.question + ";" + q.answer + ";" + q.wrong1 + ";" + q.wrong2 + ";" + q.wrong3);
        s2.sendMessage("Fråga:" + q.question + ";" + q.answer + ";" + q.wrong1 + ";" + q.wrong2 + ";" + q.wrong3);

    }

}
