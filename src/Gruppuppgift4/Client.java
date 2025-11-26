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
    private Socket s;

    public Client(GameGUI gui) {
        this.gui = gui;
    }

    public void start(){
        new Thread(()->{
        try{

            s = new Socket("localhost", 55555);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);

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
        if(out!=null){
            out.println(message);
            out.flush();
        }
    }
}
