package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerSidePlayer extends Thread {

    char playerNumber;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Questions currentQuestion;



    public ServerSidePlayer(Socket socket, char playerNumber) {
        this.socket = socket;
        this.playerNumber = playerNumber;

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
            String messageToServer;
            while( (messageToServer = in.readLine()) != null ) {

                if(messageToServer.startsWith("Redo;")) {

                    String category = messageToServer.split(";")[1];

                    GameClass game = new GameClass();
                    game.readList();
                    List<Questions> list = game.searchCategoryFromList();

                    currentQuestion = list.get(0);

                    sendMessageToClient("Fråga;" + currentQuestion.question + ";" + currentQuestion.answer + ";" + currentQuestion.wrong1 + ";" + currentQuestion.wrong2 + ";" + currentQuestion.wrong3);
                }

                if(messageToServer.startsWith("Svar;")) {
                    String answer = messageToServer.split(";")[1];

                    if(answer.equals(currentQuestion.answer)) {
                        sendMessageToClient("Rätt!");
                    } else {
                        sendMessageToClient("Fel!");
                    }
                }
            }
        } catch (IOException e) {}
    }

    public void sendMessageToClient(String message){
        out.println(message);
        out.flush();
    }
}