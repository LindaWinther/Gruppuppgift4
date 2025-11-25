package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSidePlayer {

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

    public void close(){
        try{
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message){
        out.println(message);
    }

    public void askQuestion(String question){
        sendMessage(question);
    }

    public String getAnswer() throws IOException {
        return in.readLine();
    }

}

