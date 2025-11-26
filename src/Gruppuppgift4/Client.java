package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private GameGUI gui;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    public Client(GameGUI gui) {
        this.gui = gui;
    }

    public void start(){
        new Thread(()->{

        try(
                Socket s = new Socket("localhost", 55555);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        ){

            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                gui.receiveFromServer(fromServer);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }).start();
    }

    public void send(String message){
        out.println(message);
    }
}
