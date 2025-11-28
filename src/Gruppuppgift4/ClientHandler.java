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



    public ClientHandler(Socket socket, char playerNumber) {
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
            while((messageToServer = in.readLine()) != null ) {

                if(messageToServer.startsWith("REDO;")) {
                    GameClass game = new GameClass();

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
                }

                if(messageToServer.startsWith("SVAR;")) {
                    String answer = messageToServer.split(";")[1];

                    if(answer.equals(currentQuestion.answer)) {
                        sendMessageToClient("RÄTT!");
                    } else {
                        sendMessageToClient("FEL!");
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