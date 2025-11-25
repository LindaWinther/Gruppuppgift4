package Gruppuppgift4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public Client(){

        try(
                Socket s = new Socket("localhost", 55555);
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        ){

            //Kod från sigruns BilregisterMultiuser föreläsning, den måste nog ändras sen, funkar typ.

            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {

                if (fromServer.equals("GAME_OVER")) {
                    System.out.println("Server is shutting down, disconnecting client.");
                    break;
                }

                System.out.println(fromServer);

                fromUser = userInput.readLine();
                out.println(fromUser);
                System.out.println("Sent to server: " + fromUser);
            }

            System.out.println("Client closed");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void main(String[] args) {
        Client k = new Client();
    }
}
