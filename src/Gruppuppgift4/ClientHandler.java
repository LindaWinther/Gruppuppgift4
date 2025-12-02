package Gruppuppgift4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientHandler extends Thread {

    char playerNumber;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Questions currentQuestion;
    List<Questions> questionsList = new ArrayList<>();

    ClientHandler opponent;
    boolean readyToStart;
    boolean myTurn = false;
    String nickname;
    int avatarIndex;

    String chosenCategory = null;
    int questionsPerRound = 3;

    //hur många rundor som ska spelas i spelet
    int roundsInGame;
    int questionsSent = 0;


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
        GameClass game = new GameClass();
        questionsList = game.completeList;
        Set<String> categories;

        try {

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

                if(messageToServer.startsWith("REDO_FÖR_KATEGORIER;") && chosenCategory == null){

                    categories = game.listOfCategory;

                    sendMessageToClient("KATEGORIER;" + String.join(";",categories));
                    continue;
                }

                if(messageToServer.startsWith("REDO_FÖR_FRÅGOR;")) {

                    if(chosenCategory == null){
                        chosenCategory = messageToServer.split(";")[1];
                    }

                    currentQuestion = game.getQuestions(chosenCategory, questionsList);
                    //TODO
                    // lägger in randomfråga, men gör "två" listor en för varje klient. men det borde lösa sig när vi bara väljer kategori från en spelare
//
//                    System.out.println(temp);
//                    System.out.println(messageToServer);
                    sendMessageToClient("FRÅGA;" + currentQuestion.question + ";" + currentQuestion.answer + ";" + currentQuestion.wrong1 + ";" + currentQuestion.wrong2 + ";" + currentQuestion.wrong3);
                    questionsSent++;
                    continue;
                }

                if(messageToServer.startsWith("SVAR;")) {
                    String answer = messageToServer.split(";")[1];
                    String index =  messageToServer.split(";")[2];

                    if(answer.equals(currentQuestion.answer)) {
                        sendMessageToClient("RÄTT;" + index);
                    } else {
                        sendMessageToClient("FEL;" + index);
                    }
                    if (questionsSent< questionsPerRound){
                        sendMessageToClient("DIN_TUR");
                    }
                    else {
                        questionsSent = 0;
                        chosenCategory = null;
                        myTurn = false;
                        opponent.myTurn = true;

                        opponent.sendMessageToClient("NY_RUNDA");
                        opponent.sendMessageToClient("DIN_TUR");

                        sendMessageToClient("INTE_DIN_TUR");
                    }
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToClient(String message){
        out.println(message);
        out.flush();
    }
}