package Gruppuppgift4;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameGUI extends JFrame {

    private JPanel mainPanel;
    private JButton startButton;
    private JLabel titleLabel;

    public GameGUI(){
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(27, 47, 112));
        setTitle("Quizduellen");

     JPanel centerPanel = new JPanel(new BorderLayout());
     centerPanel.setBackground(new Color(27, 47, 112));
        add(centerPanel, BorderLayout.CENTER);

        titleLabel = new JLabel("Quizduellen", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(40, 0, 40, 0));

        centerPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(27, 47, 112));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20,0));

        startButton = new JButton("Starta nytt spel");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        startButton.setForeground(Color.white);
        startButton.setBackground(new Color(31, 169, 164));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(startButton);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new GameGUI();
    }
}
