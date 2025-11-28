package Gruppuppgift4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {

    char playerNumber;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Questions currentQuestion;

    ClientHandler opponent;
    boolean myTurn = false;

    public ClientHandler(Socket socket, char playerNumber) {
        this.socket = socket;
        this.playerNumber = playerNumber;

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            GameClass game = new GameClass();
            String messageToServer;

            while((messageToServer = in.readLine()) != null ) {

                if (messageToServer.equals("START") && playerNumber == '1') {
                    myTurn = true;
                    sendMessageToClient("DIN_TUR");
                    opponent.sendMessageToClient("INTE_DIN_TUR");
                    continue;
                }

                if (!myTurn) {
                    sendMessageToClient("INTE_DIN_TUR");
                    continue;
                }

                if(messageToServer.startsWith("REDO_FÖR_KATEGORIER;")){

                    //TODO!!
                    //Just nu är kategorierna hårdkodade i en lista, kod ska vara här som väljer ut kategorierna beronde på questions dokumentet med hjälp av
                    //någon GameClass metod exempelvis
                    List<String> testCategories = List.of("Djur", "Natur", "Sport", "Mat");

                    sendMessageToClient("KATEGORIER;" + String.join(";",testCategories));
                    //test
                    System.out.println("KATEGORIER;" + String.join(";",testCategories));
                    continue;
                }

                if(messageToServer.startsWith("REDO_FÖR_FRÅGOR;")) {

                    //TODO!!!!
                    //Delen om hur man delar och skickar frågorna till servern fungerar inte riktigt som det ska (igen lol), jag vet inte riktgit hur man fixar det.
                    //Just nu skickar den alltid samma fråga så det här måste ändras. Jag gjorde bara såhär för att kunna fixa så jag kunde fixa protokollet.
                    //Måste också få in på något sätt hur man passerar vilken categori som ska väljas ifrån, just nu så bryr den sig inte om kategori.

                    game.readList();
                    List<Questions> list = game.searchCategoryFromList();
                    currentQuestion = list.get(0);

                    //just nu skickar jag en hel frågestring och splittar det senare i recieveFromServer i GameGUI. Det var kanske det vi ville undvika egentligen.
                    //Jag vet inte riktigt hur man löser det snyggt.

                    sendMessageToClient("FRÅGA;" + currentQuestion.question + ";" + currentQuestion.answer + ";" + currentQuestion.wrong1 + ";" + currentQuestion.wrong2 + ";" + currentQuestion.wrong3);
                    continue;
                }

                if(messageToServer.startsWith("SVAR;")) {
                    String answer = messageToServer.split(";")[1];

                    if(answer.equals(currentQuestion.answer)) {
                        sendMessageToClient("RÄTT");
                    } else {
                        sendMessageToClient("FEL");
                    }
                }

                myTurn = false;
                opponent.myTurn = true;
                opponent.sendMessageToClient("DIN_TUR");
                sendMessageToClient("INTE_DIN_TUR");

            }
        } catch (IOException e) {}
    }

    public void sendMessageToClient(String message){
        out.println(message);
        out.flush();
    }
}