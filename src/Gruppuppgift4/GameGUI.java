package Gruppuppgift4;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class GameGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Startsidan
    private JPanel startPanel;
    private JButton startButton;

    // Frågesidan
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JButton[] answerButtons;


    private JLabel titleLabel;

    // Test för fråga och svar
    /*    private String gameQuestion = "Vilken dag kommer efter måndag?";
    private String[] gameAnswers = {
            "Tisdag", "Fredag", "Söndag", "Torsdag"
    };*/

    // Lägger in svar från gameClass
    private String gameQuestion;
    private String[] gameAnswers;

    private int correctAnswer = 0;
    GameClass game = new GameClass();
    Questions q = new Questions();
    List<Questions> questions =  new ArrayList<Questions>();

    boolean unused = true;

    public GameGUI() {

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(27, 47, 112));
        setTitle("Quizduellen");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        buildStartPanel();
        buildQuestionPanel();

        cardLayout.show(mainPanel, "START");
        setVisible(true);

    }

    // STARTSIDAN

    private void buildStartPanel() {
        startPanel = new JPanel(new BorderLayout());
        startPanel.setBackground(new Color(50, 75, 136));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(27, 47, 112));
        startPanel.add(centerPanel, BorderLayout.CENTER);

        titleLabel = new JLabel("Quizduellen", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(40, 0, 40, 0));
        centerPanel.add(titleLabel, BorderLayout.NORTH);






        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(27, 47, 112));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        startButton = new JButton("Starta nytt spel");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        startButton.setForeground(Color.white);
        startButton.setBackground(new Color(82, 217, 41));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        startButton.addActionListener(e -> {
            loadQuestion(setGameQuestions(),setGameAnswers());
            lockAnswerButtons(true);
            cardLayout.show(mainPanel, "QUESTION");
        });

        buttonPanel.add(startButton);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(startPanel, "START");
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

        if (index == correctAnswer) {
            answerButtons[index].setBackground(new Color(0, 180, 0)); // Grönt för rätt
        } else {
            answerButtons[index].setBackground(new Color(180, 0, 0)); // Rött för fel
        }
        lockAnswerButtons(false);
    }
    public String setGameQuestions() {

        game.readList();
        questions = game.searchCategoryFromList();
        gameQuestion = questions.getFirst().question;
        setGameAnswers();
        return gameQuestion;
    }

    public String [] setGameAnswers() {
        gameQuestion = questions.get(0).question;
        gameAnswers = new String[]{questions.get(0).answer, questions.get(0).wrong1, questions.get(0).wrong2, questions.get(0).wrong3};
        questions.get(0).setAnswer(gameAnswers[0]);
        return gameAnswers;
    }

    public static void main(String[] args) {
        new GameGUI();
    }
}


//game.readList();
//        game.searchCategoryFromList();
//questions = game.searchQuestionsFromList();
//gameQuestion = questions.get(0).question;
//gameAnswers = new String[]{questions.get(0).answer, questions.get(0).wrong1, questions.get(0).wrong2, questions.get(0).wrong3};
//        questions.get(0).setAnswer(gameAnswers[0]);
//        System.out.println(questions.get(0).question);
