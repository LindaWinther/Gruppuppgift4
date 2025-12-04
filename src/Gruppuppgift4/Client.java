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
        //sköter kommunikatinen mellan gamegui och clienthandlern/servern
        //funkar som en middleman kan man säga.
        new Thread(()->{
        try{

            //ansluter till servern via socket
            Socket socket = new Socket("localhost", 55555);
            //tar emot från servern
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //skickar text till servern
            out = new PrintWriter(socket.getOutputStream(), true);

            //läser in från servern
            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                //skickar vidare vad den får från servern till GUI
                gui.receiveFromServer(fromServer);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }).start();
    }

    //tar en sträng från gui och skriver ut den via out. Servern får då meddelandet via sin socket.
    public void sendMessageToServer(String message){
        if(out!=null){
            out.println(message);
            out.flush();
        }
    }
}
