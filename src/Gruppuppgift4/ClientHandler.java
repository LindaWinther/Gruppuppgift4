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
    boolean isChooserThisRound = false;
    boolean roundFinished = false;
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

    public void run() {

        try {
            String messageToServer;
            while((messageToServer = in.readLine()) != null ) {

                if (messageToServer.startsWith("START;")) {

                    handleStart(messageToServer);
                    continue;
                }

                if (!myTurn) {
                    sendMessageToClient("INTE_DIN_TUR");
                    continue;
                }

                if(messageToServer.startsWith("REDO_FÖR_KATEGORIER;")){

                    if(!isChoosingCategory){
                        sendMessageToClient("INTE_DIN_TUR");
                        continue;
                    }

                    sendCategories();
                    continue;
                }

                if(messageToServer.startsWith("REDO_FÖR_FRÅGOR;")) {
                    handleReadyForQuestions(messageToServer);
                    continue;
                }

                if(messageToServer.startsWith("SVAR;")) {
                    handleAnswer(messageToServer);
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

    private void handleStart(String messageToServer){
        String[] parts = messageToServer.split(";");
        nickname = parts[1];
        avatarIndex = Integer.parseInt(parts[2]);
        //kontroll om båda spelarana har skrivit in användarnamn/avatar
        readyToStart = true;

        if(opponent == null || !opponent.readyToStart) {
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

        if(isChoosingCategory && chosenCategory == null) {

            String[] parts = messageToServer.split(";");
            chosenCategory = parts[1];

            genereateQuestionsForRound(chosenCategory);

            opponent.currentRoundQuestions = new ArrayList<>(currentRoundQuestions);
            opponent.chosenCategory = chosenCategory;
            opponent.questionsSent = 0;

            opponent.sendMessageToClient("KATEGORI_VALD;" + chosenCategory);

            isChoosingCategory = false;
            isAnsweringQuestions = true;

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
        roundFinished = false;
        opponent.roundFinished = false;

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

        if (questionsSent >= currentRoundQuestions.size()) {
            finishAnswering();
            return;
        }

        Questions question = currentRoundQuestions.get(questionsSent);
        questionsSent++;

        sendMessageToClient("FRÅGA;" + question.question + ";" + question.answer + ";" + question.wrong1 + ";" + question.wrong2 + ";" + question.wrong3);
    }

    private void finishAnswering(){
        isAnsweringQuestions = false;
        myTurn = false;
        roundFinished = true;

        if(!opponent.roundFinished){
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