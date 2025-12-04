package Gruppuppgift4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//extends thread så den körs i separata trådar
public class ClientHandler extends Thread {
    Config config = new Config();//

    char playerNumber;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    GameClass game = new GameClass();
    List<Questions> completeList = game.completeList;

    String nickname;
    int avatarIndex;

    //hur många rundor som ska spelas i spelet
    //int roundsInGame;

    ClientHandler opponent;
    boolean readyToStart;
    boolean myTurn = false;

    boolean isChoosingCategory = false;
    boolean isAnsweringQuestions = false;
    boolean isRoundFinished = false;
    boolean isRoundStarter = false;

    List<Questions> currentRoundQuestions = new ArrayList<>();

    int questionsPerRound = config.getQuestionsinRound();
    int roundsInGame = config.getRoundsInGame();
    int questionIndex = 0;
    int roundCounter = 0;
    int roundScore = 0;
    int totalMatchScore = 0;
    int totalQuestionsInGame = roundsInGame * questionsPerRound;


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
                    //Gör så att spelet kan inte gå vidare försän båda har connectat, sätter så att spelare 1 börjar och får välja kategori.
                    handleStart(messageToServer);
                    continue;
                }

                //säkerhetställer att om myTurn = false så kan inte den klienten göra någonting.
//                if (!myTurn) {
//                    sendMessageToClient("INTE_DIN_TUR");
//                    continue;
//                }

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
                    //kommer köras varenda gång en användare klickar på en svarknapp i GameGUI:n
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

    //todo fixa så att om questionsperround är > unused frågor i en kateogri, skicka inte kategorin.

    private void sendCategories(){
        List<String> categoriesLeft = new ArrayList<>();

        for (String category : game.listOfCategory){

            int amountOfUnusedLeft = 0;

            for(Questions question : completeList){
                if (question.category.equalsIgnoreCase(category) && (question.unused == true)){
                    amountOfUnusedLeft++;
                }
            }
            if (amountOfUnusedLeft >= questionsPerRound){
                categoriesLeft.add(category);
            }
            //bara för utskrift i konsollen
            System.out.println(category + " har" + amountOfUnusedLeft + " frågor kvar");
        }

        sendMessageToClient("KATEGORIER;" + String.join(";", categoriesLeft));
    }

    private void handleReadyForQuestions(String messageToServer){

        //körs bara om det inte är någon kategori vald ännu
        if(isChoosingCategory && chosenCategory == null) {

            String[] parts = messageToServer.split(";");
            chosenCategory = parts[1];

            // kommer generera och lägga till frågor beroende på chosenCategory i currentRoundQuestions listan.
            generateQuestionsForRound(chosenCategory);

            //ser till att andra klienten får dom genererade frågorna också, eftersom den ska svara på samma också
            opponent.currentRoundQuestions = currentRoundQuestions;
            System.out.println(opponent.currentRoundQuestions.getFirst().unused);
            System.out.println(opponent.currentRoundQuestions.size());
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

        //kommer köra så länge isAnsweringQuestions är true, och det är den tills currentRoundQuestions inte har några frågor kvar.
        if (isAnsweringQuestions){
            sendNextQuestion();
        }
    }

    private void handleAnswer(String messageToServer){

        String[] parts = messageToServer.split(";");
        //svaret, aka stringen som kommer från knappen som klickades
        String answer = parts[1];
        //index sparas för att veta vilken knapp det är som ska färgas
        String index = parts[2];

        //lagrar vilken fråga det är som behandlas nu
        Questions question = currentRoundQuestions.get(questionIndex - 1);

        //kollar om stringen på knappen som klickas på är lika med det question objektet som behandlas answer.
        if (answer.equals(question.answer)){
            roundScore++;
            totalMatchScore++;
            sendMessageToClient("RÄTT;" + index);
        }
        else {
            sendMessageToClient("FEL;" + index + ";" + answer);
        }

        //fortsätter med spelarens tur tills questionsSent blir samma som questionsPerRound, eftersom det är limiten mängden frågor i rundan.
        if (questionIndex < questionsPerRound) {
            sendMessageToClient("DIN_TUR");
        } else {
            //om vi har nått limiten så kör vi finishAnswering för att inte kolla på frågor utanför index.
            finishAnswering();
        }
    }

    private void generateQuestionsForRound(String chosenCategory){
        currentRoundQuestions.clear();
        for (int i = 0; i < questionsPerRound; i++) {
            Questions question = game.getQuestions(chosenCategory, completeList);
            currentRoundQuestions.add(question);
            opponent.completeList = completeList;
        }
    }

    private void sendNextQuestion(){

        //kollar så att det finns frågor i currentRoundQuestions listan, om det inte finns några kvar så kommer finishAnswering köras
        if (questionIndex >= currentRoundQuestions.size()) {
            //den här kommer sätta isAnsweringQuestions till false så vi slutar skicka frågor!
            finishAnswering();
            return;
        }

        //skickar frågan med index av questionsSent till GameGUI, questionsSent++ varenda gång en fråga blir skickad.
        Questions question = currentRoundQuestions.get(questionIndex);

        sendMessageToClient("FRÅGA;" + question.question + ";" + question.answer + ";" + question.wrong1 + ";" + question.wrong2 + ";" + question.wrong3);
        questionIndex++;
    }

    private void finishAnswering(){
        //sätter så att man är klar med frågorna och ens turn är över, vilket i sin tur betyder att DEN HÄR klientens round är finished.
        isAnsweringQuestions = false;
        myTurn = false;
        isRoundFinished = true;
        sendMessageToClient("INTE_DIN_TUR");
        //kollar om motståndaren har fått svara på sina frågor, om den inte har det så får den köra sitt tur och svara på frågorna.

        if(!opponent.isRoundFinished){
            opponent.myTurn = true;
            opponent.isAnsweringQuestions = true;
            opponent.questionIndex = 0;
            opponent.sendMessageToClient("DIN_TUR");
            return;
        }

        roundCounter++;
        opponent.roundCounter = roundCounter;

        //skickar resultaten när båda användarna är klara med sin round
        String resultString = "Rond" + roundCounter + ":du " + roundScore + "/" + questionsPerRound + " -motståndare" + opponent.roundScore + " / " + questionsPerRound;
        sendMessageToClient("RESULTAT;" + resultString + ";" + totalMatchScore + ";" + totalQuestionsInGame);

        String opponentResultString = "Rond" + roundCounter + ":du " + opponent.roundScore + "/" + questionsPerRound + " -motståndare" + roundScore + " / " + questionsPerRound;
        opponent.sendMessageToClient("RESULTAT;" + opponentResultString +";" + opponent.totalMatchScore + ";" + totalQuestionsInGame);

        //test
        System.out.println(opponentResultString);
        System.out.println(resultString);


        //om conditionen fylls så stängs spelet och man kommer till score-screen
        if (roundCounter >= roundsInGame) {
            sendMessageToClient("DIN_TUR");
            opponent.sendMessageToClient("DIN_TUR");
            endGame();
            return;
        }
        // när båda har svarat så kommer vi hit och beroende på vem som var roundstarter så bestäms det vem nästa roundstartern ska vara. Så efter den första
        // rundan till exmepel så blir motståndaren den nya round startern.
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

    private void startNewRound(){

        //den här metoden fungerar som en reset för rundan. Alla värden som vilken kategori/fråga som hade blivit vald resettas.
        //den sätter också att en spelare är isChoosingCategory igen/vems tur det är.

        chosenCategory = null;
        currentRoundQuestions = new ArrayList<>();
        questionIndex = 0;

        roundScore = 0;
        opponent.roundScore = 0;

        isRoundFinished = false;
        opponent.isRoundFinished = false;

        isChoosingCategory = true;
        isAnsweringQuestions = false;

        myTurn = true;
        opponent.myTurn = false;

        //skickar så att båda klienterna vet att detbehövs väljas en ny kategori.
        sendMessageToClient("NY_RUNDA");

        sendMessageToClient("DIN_TUR");
        opponent.sendMessageToClient("INTE_DIN_TUR");
    }

    private void endGame(){
        sendMessageToClient("GAME_OVER");
        opponent.sendMessageToClient("GAME_OVER");
    }
}