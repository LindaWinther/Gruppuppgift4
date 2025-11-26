package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSidePlayer extends Thread {

    GameClass game = new GameClass();

    char playerNumber;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Questions question;



    public ServerSidePlayer(Socket socket, char playerNumber, Questions question) {
        this.socket = socket;
        this.playerNumber = playerNumber;
        this.question = question;

        try{
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            out.println("Fråga;" + question.question + ";"
                    + question.answer + ";"
                    + question.wrong1 + ";"
                    + question.wrong2 + ";"
                    + question.wrong3);

            String answer;
            while ((answer = in.readLine()) != null) {
                if (answer.equals(question.answer)) {
                    out.println("Rätt!");
                } else {
                    out.println("Fel!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }
}