package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSidePlayer extends Thread {

    char playerNumber;
    Socket socket;
    BufferedReader in;
    PrintWriter out;


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
            out.println("Fråga: Vad är 1 + 1?");
            while(true){
                String answer = in.readLine();
                if(answer == null) break;

                if(answer.equals("2"))
                    out.println("Rätt!");
                else
                    out.println("Fel!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }

    public void askQuestion(String question){
        sendMessage(question);
    }

    public String getAnswer() throws IOException {
        return in.readLine();
    }

}

