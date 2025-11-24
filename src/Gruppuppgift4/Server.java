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
                player1.sendMessage("Welcome player1, waiting for player2 to connect");
                ServerSidePlayer player2 = new ServerSidePlayer(listener.accept(), '2');
                player2.sendMessage("Welcome player2");

                System.out.println("Both players connected!");

                player1.sendMessage("All clients connected!");
                player2.sendMessage("All clients connected!");

                player1.askQuestion("Vad är 1 + 1?");
                player2.askQuestion("Vad är 1 + 1?");

                String answer1 = player1.getAnswer();
                String answer2 = player2.getAnswer();
                System.out.println("player 1 answer: " + answer1);
                System.out.println("player 2 answer: " + answer2);

                if (answer1.equals("2")){
                    player1.sendMessage("Rätt svar!");
                } else {
                    player1.sendMessage("Fel svar!");
                }

                if (answer2.equals("2")){
                    player2.sendMessage("Rätt svar!");
                } else {
                    player2.sendMessage("Fel svar!");
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
