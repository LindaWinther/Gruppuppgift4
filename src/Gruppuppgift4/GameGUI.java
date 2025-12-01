package Gruppuppgift4;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Headerpanel med bägge spelares avatar/användarnamn
    private JPanel headerInfoPanel;
    private JLabel myAvatarLabel, myUsernameLabel;
    private JLabel oppAvatarLabel, oppUsernameLabel;
    private JLabel vsLabel;

    private String myNickname;
    private int myAvatarIndex = -1;
    private ImageIcon[] avatarIcons;

    // Startsidan
    private JPanel startPanel;
    private JButton startButton;
    private JTextField nicknameField;
    private int selectedAvatarIndex = -1;

    // Kategorisidan
    private JPanel categoryPanel;
    private JPanel categoryButtonsPanel;

    // Frågesidan
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JButton[] answerButtons;
    private JLabel titleLabel;

    // Lägger in svar från gameClass
    private String gameQuestion ;
    private String[] gameAnswers;

    private int correctAnswer = 0;
    GameClass game = new GameClass();
    Questions q = new Questions();
    List<Questions> questions = new ArrayList<Questions>();

    boolean unused = true;

    private Client client;

    public GameGUI() {
        client = new Client(this);
        client.start();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(27, 47, 112));
        setTitle("Quizkampen");

        // Layout runt hela fönstret
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(27, 47, 112));

        // Cardlayout för sidorna
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // HEADER FÖR SPELARES AVATAR OCH ANVÄNDARNAMN
        buildHeaderInfoPanel();
        getContentPane().add(headerInfoPanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // DÖLJER HEADER TILLS SPELET BÖRJAR
        headerInfoPanel.setVisible(false);

        // Alla sidorna
        buildStartPanel();
        buildCategoryPanel();
        buildQuestionPanel();

        cardLayout.show(mainPanel, "START");
        setVisible(true);
    }

    // HEADER (spelares avatar/användarnamn)
    private void buildHeaderInfoPanel() {
        headerInfoPanel = new JPanel(new BorderLayout());
        headerInfoPanel.setBackground(new Color(27, 47, 112));
        headerInfoPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Till vänster, "min spelare"
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));

        myAvatarLabel = new JLabel();
        myUsernameLabel = new JLabel("Jag");
        myUsernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        myUsernameLabel.setForeground(Color.WHITE);

        leftPanel.add(myAvatarLabel);
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(myUsernameLabel);

        // I MITTEN, VS
        vsLabel = new JLabel("VS", SwingConstants.CENTER);
        vsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        vsLabel.setForeground(Color.WHITE);

        // TILL HÖGER, MOSTÅNDAREN
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));

        oppUsernameLabel = new JLabel("Motståndare");
        oppUsernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        oppUsernameLabel.setForeground(Color.WHITE);
        oppAvatarLabel = new JLabel();

        rightPanel.add(oppUsernameLabel);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(oppAvatarLabel);

        headerInfoPanel.add(leftPanel, BorderLayout.WEST);
        headerInfoPanel.add(vsLabel, BorderLayout.CENTER);
        headerInfoPanel.add(rightPanel, BorderLayout.EAST);
    }

   // TODO!!
   // Metoderna uppdaterar headern med båda spelarnas avatar och anv.namn
   // Ens egna info på vänster sida, uppdateras direkt, på höger sida ligger motståndarens och det måste komma från servern
   // Visar nu info om ena spelaren, men inte motståndarens, servern måste skicka det till klienten

    private void updateMyPlayerHeader(){
        if(myNickname != null){
            myUsernameLabel.setText(myNickname);
        }
        if (avatarIcons != null && myAvatarIndex >= 0 && myAvatarIndex < avatarIcons.length){
            myAvatarLabel.setIcon(avatarIcons[myAvatarIndex]);
        }
    }

    private void updateOppHeader(String name, int avatarIndex){
        oppUsernameLabel.setText(name);
        if (avatarIcons != null && avatarIndex >= 0 && avatarIndex < avatarIcons.length) {
            oppAvatarLabel.setIcon(avatarIcons[avatarIndex]);
        }
    }


    // Ladda in rätt storlek på ikoner
    private ImageIcon loadAvatarIcon(String fileName){
        String path =  "src/Gruppuppgift4/avatarImages/" + fileName;
        ImageIcon original = new ImageIcon(path);
        Image scaled = original.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // STARTSIDAN
    private void buildStartPanel() {
        startPanel = new JPanel(new BorderLayout());
        startPanel.setBackground(new Color(50, 75, 136));

        // Centerpanel, titel, formulär, knapp
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(27, 47, 112));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        startPanel.add(centerPanel, BorderLayout.CENTER);

        // Titel
        titleLabel = new JLabel("Quizduellen", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(40, 0, 40, 0));
        centerPanel.add(titleLabel);

        // Användarnamn
        JLabel nickLabel = new JLabel("Ange användarnamn:");
        nickLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        nickLabel.setForeground(Color.WHITE);

        nicknameField = new JTextField(15);
        nicknameField.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        // Avatar
        JLabel avatarLabel = new JLabel("Välj avatar:");
        avatarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        avatarLabel.setForeground(Color.WHITE);

        // Ladda PNG-bilder
        ImageIcon chicken = loadAvatarIcon("chicken.png");
        ImageIcon panda = loadAvatarIcon("panda(1).png");
        ImageIcon bear = loadAvatarIcon("bear.png");
        avatarIcons = new ImageIcon[] { chicken, panda, bear };


        // Panel för avatarknappar
        JPanel avatarPanel = new JPanel();
        avatarPanel.setBackground(new Color(27, 47, 112));
        avatarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));

        for (int i = 0; i < avatarIcons.length; i++) {
            int idx = i;
            JButton avatarButton = new JButton(avatarIcons[i]);
            avatarButton.setPreferredSize(new Dimension(70, 70));
            avatarButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            avatarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            avatarButton.setFocusPainted(false);
            avatarButton.setBackground(new Color(27, 47, 112));

            avatarButton.addActionListener(e -> {
                selectedAvatarIndex = idx;
                highlightSelectedAvatar(avatarPanel, idx);
            });
            avatarPanel.add(avatarButton);
        }

        // Formpanel för två rader: användarnamn/avatar
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(27, 47, 112));
        formPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel nickRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        nickRow.setOpaque(false);
        nickRow.add(nickLabel);
        nickRow.add(nicknameField);

        JPanel avatarRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        avatarRow.setOpaque(false);
        avatarRow.add(avatarLabel);
        avatarRow.add(avatarPanel);

        formPanel.add(nickRow);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(avatarRow);

        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalGlue());

        // Panel för starta spel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(27, 47, 112));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        startButton = new JButton("Starta nytt spel");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        startButton.setForeground(Color.white);
        startButton.setBackground(new Color(82, 217, 41));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        startButton.addActionListener(e -> {
            String nickname = nicknameField.getText().trim();

            if  (nickname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Skriv in ett användarnamn först!");
                return;
            }
            if (selectedAvatarIndex == -1) {
                JOptionPane.showMessageDialog(this, "Välj en avatar först!");
                return;
            }

            myNickname = nickname;
            myAvatarIndex = selectedAvatarIndex;

            updateMyPlayerHeader();
            headerInfoPanel.setVisible(true);

            // SKICKAR BÅDE ANVÄNDARNAMN OCH AVATAR TILL SERVERN
            client.sendMessageToServer("START;" + nickname + ";" + myAvatarIndex);
        });

        buttonPanel.add(startButton);
        centerPanel.add(buttonPanel);

        mainPanel.add(startPanel, "START");
    }

    // Hovereffekt på avatarknappar
    private void highlightSelectedAvatar(JPanel panel, int selectedIndex) {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            JButton btn = (JButton) panel.getComponent(i);
            if (i == selectedIndex) {
                btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
            } else {
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        }
    }

    // KATEGORISIDAN
    private void buildCategoryPanel() {
        categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setBackground((new Color(27, 47, 112)));
        categoryPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel categoryLabel = new JLabel("Välj kategori", SwingConstants.CENTER);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

        categoryPanel.add(categoryLabel, BorderLayout.NORTH);

        categoryButtonsPanel = new JPanel();
        categoryButtonsPanel.setBackground(new Color(27, 47, 112));

        categoryPanel.add(categoryButtonsPanel, BorderLayout.CENTER);

        mainPanel.add(categoryPanel, "CATEGORY");
    }

    public void loadCategories(List<String> categories) {
        categoryButtonsPanel.removeAll();
        categoryButtonsPanel.setLayout(new GridLayout(0,2, 20, 20));
        categoryButtonsPanel.setBackground(new Color(27, 47, 112));

        for (String category: categories) {
            JButton btn = new JButton(category);
            styleCategoryButton(btn);

            btn.addActionListener(e-> {
                // Den får kategorin från server och när den skickar REDO, till servern så får den ut frågorna från servern beroende på kategorin
                client.sendMessageToServer("REDO_FÖR_FRÅGOR;" + category);
                lockAnswerButtons(true);
                cardLayout.show(mainPanel, "QUESTION");
            });

            categoryButtonsPanel.add(btn);
        }

        categoryButtonsPanel.revalidate();
        categoryButtonsPanel.repaint();

    }

    private void styleCategoryButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(31, 169, 164));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // FRÅGESIDAN
    private void buildQuestionPanel() {
        questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground((new Color(27, 47, 112)));
        questionPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        questionLabel = new JLabel("Frågan här", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        questionPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel answersPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        answersPanel.setBackground(new Color(27, 47, 112));

        answerButtons = new JButton[4];

        for (int i = 0; i < 4; i++) {
            JButton btn = new JButton("Svar " + (i + 1));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            btn.setBackground(new Color(31, 169, 164));
            btn.setForeground(Color.WHITE);

            int index = i;
            btn.addActionListener(e -> checkAnswer(index));

            answerButtons[i] = btn;
            answersPanel.add(btn);
        }
        questionPanel.add(answersPanel, BorderLayout.CENTER);
        mainPanel.add(questionPanel, "QUESTION");
    }

    // FRÅGELOGIK

    private void loadQuestion(String question, String[] answers) {
        questionLabel.setText(question);

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText(answers[i]);
            answerButtons[i].setEnabled(true);
            answerButtons[i].setBackground(new Color(50, 44, 133));
        }
    }

    private void lockAnswerButtons(boolean enabled) {
        for (JButton btn : answerButtons) {
            btn.setEnabled(enabled);
        }
    }

    // Färgar svaret
    private void checkAnswer(int index) {
        client.sendMessageToServer("SVAR;" + answerButtons[index].getText());
        if (index == correctAnswer) {
            answerButtons[index].setBackground(new Color(0, 180, 0)); // Grönt för rätt
        } else {
            answerButtons[index].setBackground(new Color(180, 0, 0)); // Rött för fel
        }
        lockAnswerButtons(false);
    }

//    public String setGameQuestions() {
//        game.readList();
//        questions = game.searchCategoryFromList();
//        gameQuestion = questions.getFirst().question;
//        setGameAnswers();
//        return gameQuestion;
//    }
//
//    public String[] setGameAnswers() {
//        gameQuestion = questions.get(0).question;
//        gameAnswers = new String[]{questions.get(0).answer, questions.get(0).wrong1, questions.get(0).wrong2, questions.get(0).wrong3};
//        questions.get(0).setAnswer(gameAnswers[0]);
//        return gameAnswers;
//    }

    public void receiveFromServer(String messageFromServer) {
        SwingUtilities.invokeLater(() -> {
            if (messageFromServer.equals("DIN_TUR")) {
                client.sendMessageToServer("REDO_FÖR_KATEGORIER;");
            }
            if (messageFromServer.equals("INTE_DIN_TUR")) {
                JOptionPane.showMessageDialog(this,"Vänta. Din motståndare svarar på frågorna.");
            }
            if(messageFromServer.startsWith("KATEGORIER;")){
                cardLayout.show(mainPanel, "CATEGORY");
                String[] parts =  messageFromServer.split(";");
                //jag måste göra om det från en string till en list eftersom sendMessageToClient tar bara en sträng just nu, och loadCategories tar en list.
                //Det är nog någonting som går att ändra antingen i sendMessageToClient eller loadCategories. För det här känns inte supersmart, men det funkar.
                List<String> stringToList = new ArrayList<>();
                for(int i = 1; i < parts.length; i++){
                    stringToList.add(parts[i]);
                }
                //test
                System.out.println(stringToList);
                loadCategories(stringToList);
            }
            if (messageFromServer.startsWith("FRÅGA;")) {     // Ta bort hela if stycke?
                cardLayout.show(mainPanel, "QUESTION");
                String[] parts = messageFromServer.split(";");
                loadQuestion(parts[1], new String[]{parts[2],parts[3],parts[4],parts[5]});
            }
            if (messageFromServer.equals("RÄTT")) {
                JOptionPane.showMessageDialog(this, "Rätt!");
            }
            if (messageFromServer.equals("FEL")) {
                JOptionPane.showMessageDialog(this, "Fel svar!");
            }
            //gör inget just nu, kan användas för en exit knapp i framtiden
            if (messageFromServer.equals("GAME_OVER")) {
                JOptionPane.showMessageDialog(this, "Spelet är slut!");
            }
        });
    }

    public static void main(String[] args) {
        new GameGUI();
    }
}
