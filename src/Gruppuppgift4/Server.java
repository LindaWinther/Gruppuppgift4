package Gruppuppgift4;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(55555);
        System.out.println("Server is Running!");
        try{
            while (true){
                ServerSidePlayer player1 = new ServerSidePlayer(listener.accept(), '1');
                ServerSidePlayer player2 = new ServerSidePlayer(listener.accept(), '2');
                System.out.println("Both players connected!");

                player1.askQuestion("Vad är 1 + 1?");
                player2.askQuestion("Vad är 1 + 1?");



            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
