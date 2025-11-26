package Gruppuppgift4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(55555);
        System.out.println("Server is running!");

        Socket p1 = server.accept();
        Socket p2 = server.accept();

        ServerSidePlayer s1 = new ServerSidePlayer(p1, '1');
        ServerSidePlayer s2 = new ServerSidePlayer(p2, '1');

        s1.start();
        s2.start();

        s1.sendMessage("Fråga: Vad är 1+1?");
        s2.sendMessage("Fråga: Vad är 1+1?");

    }

}
