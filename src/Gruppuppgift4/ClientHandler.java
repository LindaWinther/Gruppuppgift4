package Gruppuppgift4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientHandler extends Thread {

    char playerNumber;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Questions currentQuestion;
    List<Questions> listanSomSkapas = new ArrayList<>();

    ClientHandler opponent;
    boolean readyToStart;
    boolean myTurn = false;
    String nickname;
    int avatarIndex;


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
            listanSomSkapas = game.readList();
            String messageToServer;
            while((messageToServer = in.readLine()) != null ) {

                if (messageToServer.startsWith("START;")) {
                    String[] parts = messageToServer.split(";");
                    nickname = parts[1];
                    avatarIndex = Integer.parseInt(parts[2]);

                    //kontroll om båda spelarana har skrivit in användarnamn/avatar
                    readyToStart = true;
                    if (!opponent.readyToStart) {
                        continue;
                    }

                    sendMessageToClient("FIENDEN_REGISTRERAD;" + opponent.nickname + ";" + opponent.avatarIndex);
                    opponent.sendMessageToClient("FIENDEN_REGISTRERAD;" + nickname + ";" + avatarIndex);

                    if (playerNumber == '1') {
                        myTurn = true;
                        sendMessageToClient("DIN_TUR");
                        opponent.sendMessageToClient("INTE_DIN_TUR");
                    }

                    continue;
                }

                if (!myTurn) {
                    sendMessageToClient("INTE_DIN_TUR");
                    continue;
                }

                if(messageToServer.startsWith("REDO_FÖR_KATEGORIER;")){

                    //Kod för att välja kategorier här
                    //List<String> testCategories = List.of("Djur", "Natur", "Sport", "Mat");

                    Set<String> testCategories = new HashSet<String>(); //byt nman
                    testCategories = game.checkCategorys(listanSomSkapas);
                    System.out.println(testCategories);


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

//                    game.readList();
                    System.out.println(messageToServer);
                    String temp =  messageToServer.split(";")[1];
//                    String temp =  messageToServer.substring(messageToServer.indexOf(";")+1);
                    System.out.println(temp);
                        listanSomSkapas = game.searchCategoryFromList(temp);// ta in värde på kategori Kör (Sträng?) i loopen?
                             System.out.println(listanSomSkapas);
                    System.out.println(listanSomSkapas.size());
                        currentQuestion = game.randomQuestion();
//                    Set<String> testCategories = game.searchCategoryFromList();
//                   List<Questions> list = game.searchCategoryFromList();
//                    currentQuestion = listanSomSkapas.get(0);

                    //just nu skickar jag en hel frågestring och splittar det senare i recieveFromServer i GameGUI. Det var kanske det vi ville undvika egentligen.
                    //Jag vet inte riktigt hur man löser det snyggt.


                    // frågorna kommer random men p1 och p2 får inte samma fråga, är det ett serverproblem?
                    // pingpong?
                    // p1 ansluter, p2, ansluter, server generar kategori, genererar fråga, sparar den, skicckar till p1, väntar på svar, när svar fås skicka till p2 ? programeras i spel eller server?

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