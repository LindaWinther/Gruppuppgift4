package Gruppuppgift4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread {

    char playerNumber;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Questions currentQuestion;
    GameClass game = new GameClass();
    List<Questions> questionsList = game.completeList;

    String nickname;
    int avatarIndex;

    //hur många rundor som ska spelas i spelet
    int roundsInGame;

    ClientHandler opponent;
    boolean readyToStart;
    boolean myTurn = false;

    boolean isChoosingCategory = false;
    boolean isAnsweringQuestions = false;
    boolean isRoundFinished = false;
    boolean isRoundStarter = false;

    int questionsPerRound = 3;

    List<Questions> currentRoundQuestions = new ArrayList<>();
    int questionsSent = 0;

    String chosenCategory = null;


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

    @Override
    public void run() {

        try {
            String messageToServer;
            while((messageToServer = in.readLine()) != null ) {

                if (messageToServer.startsWith("START;")) {
                    //Gör så att spelet kan inte gå vidare försän båda har connectat, sätter så att spelare 1 börjar och får välja kategori.
                    handleStart(messageToServer);
                    continue;
                }

                //säkerhetställer att om myTurn = false så kan inte den klienten göra någonting.
                if (!myTurn) {
                    sendMessageToClient("INTE_DIN_TUR");
                    continue;
                }

                if(messageToServer.startsWith("REDO_FÖR_KATEGORIER;")){

                    if(!isChoosingCategory){
                        sendMessageToClient("INTE_DIN_TUR");
                        return;
                    }
                    //skickar bara kategorierna som finns till gameGUI
                    sendCategories();
                    continue;
                }

                if(messageToServer.startsWith("REDO_FÖR_FRÅGOR;")) {
                    //hanterar och genererar frågor beroende på kategori klickad på
                    handleReadyForQuestions(messageToServer);
                    continue;
                }

                if(messageToServer.startsWith("SVAR;")) {
                    handleAnswer(messageToServer);
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

    private void handleStart(String messageToServer){
        String[] parts = messageToServer.split(";");
        nickname = parts[1];
        avatarIndex = Integer.parseInt(parts[2]);
        readyToStart = true;

        if(opponent == null || !opponent.readyToStart) {
            return;
        }

        sendMessageToClient("FIENDEN_REGISTRERAD;" + opponent.nickname + ";" + opponent.avatarIndex);
        opponent.sendMessageToClient("FIENDEN_REGISTRERAD;" + nickname + ";" + avatarIndex);

        if(playerNumber == '1'){
            isRoundStarter = true;
            startNewRound();
        }
    }

    private void sendCategories(){
        sendMessageToClient("KATEGORIER;" + String.join(";", game.listOfCategory));
    }

    private void handleReadyForQuestions(String messageToServer){

        //körs bara om det inte är någon kategori vald ännu
        if(isChoosingCategory && chosenCategory == null) {

            String[] parts = messageToServer.split(";");
            chosenCategory = parts[1];

            // kommer generera och lägga till frågor beroende på chosenCategory i currentRoundQuestions listan.
            genereateQuestionsForRound(chosenCategory);

            //ser till att andra klienten får dom genererade frågorna också, eftersom den ska svara på samma också
            opponent.currentRoundQuestions = new ArrayList<>(currentRoundQuestions);
            opponent.chosenCategory = chosenCategory;

            //bara så att motståndaren ska veta att en kategori har blivit vald.
            opponent.sendMessageToClient("KATEGORI_VALD;" + chosenCategory);

            //eftersom vi nu har valt kategori och ska svara på frågorna sätter vi isChoosingCategory till false och isAnsweringQuestions till true.
            isChoosingCategory = false;
            isAnsweringQuestions = true;

            //skickar den första frågan och hoppar ut ur den här if satsen.
            sendNextQuestion();
            return;
        }

        if (isAnsweringQuestions){
            sendNextQuestion();
        }
    }

    private void handleAnswer(String messageToServer){

        String[] parts = messageToServer.split(";");
        String answer = parts[1];
        String index = parts[2];

        Questions question = currentRoundQuestions.get(questionsSent - 1);

        if (answer.equals(question.answer))
            sendMessageToClient("RÄTT;" + index);
        else
            sendMessageToClient("FEL;" + index);

        if (questionsSent < questionsPerRound) {
            sendMessageToClient("DIN_TUR");
        } else {
            finishAnswering();
        }
    }

    private void startNewRound(){

        chosenCategory = null;
        currentRoundQuestions = new ArrayList<>();
        questionsSent = 0;
        isRoundFinished = false;
        opponent.isRoundFinished = false;

        isChoosingCategory = true;
        isAnsweringQuestions = false;

        myTurn = true;
        opponent.myTurn = false;

        sendMessageToClient("NY_RUNDA");
        sendMessageToClient("DIN_TUR");
        opponent.sendMessageToClient("INTE_DIN_TUR");
    }

    private void genereateQuestionsForRound(String chosenCategory){
        currentRoundQuestions.clear();
        for (int i = 0; i < questionsPerRound; i++) {
            Questions question = game.getQuestions(chosenCategory, questionsList);
            currentRoundQuestions.add(question);
        }
    }

    private void sendNextQuestion(){

        //kollar så att det finns frågor i currentRoundQuestions listan, om det inte finns några kvar så kommer finishAnswering köras
        if (questionsSent >= currentRoundQuestions.size()) {
            finishAnswering();
            return;
        }

        //skickar frågan med index av questionsSent till GameGUI,questionsSent++ varenda gång en fråga blir skickad.
        Questions question = currentRoundQuestions.get(questionsSent);

        sendMessageToClient("FRÅGA;" + question.question + ";" + question.answer + ";" + question.wrong1 + ";" + question.wrong2 + ";" + question.wrong3);
        questionsSent++;
    }

    private void finishAnswering(){
        isAnsweringQuestions = false;
        myTurn = false;
        isRoundFinished = true;

        if(!opponent.isRoundFinished){
            opponent.myTurn = true;
            opponent.isAnsweringQuestions = true;
            opponent.questionsSent = 0;
            opponent.sendMessageToClient("DIN_TUR");
            return;
        }

        if (opponent.isRoundStarter) {
            opponent.isRoundStarter = false;
            this.isRoundStarter = true;
            startNewRound();
        } else {
            this.isRoundStarter = false;
            opponent.isRoundStarter = true;
            opponent.startNewRound();
        }
    }
}