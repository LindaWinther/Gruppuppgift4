package Gruppuppgift4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        //Servern lyssnar på port 55555 och väntar på att två klienter ansluter.
        //När två klienter ansuter så skapas två ClientHandler-trådar som kopplas samman. Därefter startar båda trådarna.

        ServerSocket server = new ServerSocket(55555);
        System.out.println("Server is running!");

        try {
            while (true) {

                //väntar på att två clients ska ansluter på socketen
                Socket p1 = server.accept();
                Socket p2 = server.accept();

                //skapar två clienthandler objekt
                ClientHandler s1 = new ClientHandler(p1, '1');
                ClientHandler s2 = new ClientHandler(p2, '2');

                //referenser till varandra
                s1.opponent = s2;
                s2.opponent = s1;

                //skapar nya trådar och låter den tråden sen köra run() i clienthandlern.
                s1.start();
                s2.start();

            }
        } finally {
            server.close();
        }
    }
}
