package view;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JLabel turnLabel;
    private JLabel turnTotalLabel;
    private JButton rollButton;
    private JButton holdButton;

    public GameView() {
        setLayout(new BorderLayout());

        JPanel scorePanel = new JPanel(new GridLayout(2, 2));
        scorePanel.add(new JLabel("Player 1 Score:"));
        player1ScoreLabel = new JLabel("0");
        scorePanel.add(player1ScoreLabel);
        scorePanel.add(new JLabel("Player 2 Score:"));
        player2ScoreLabel = new JLabel("0");
        scorePanel.add(player2ScoreLabel);
        add(scorePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        turnLabel = new JLabel("Your Turn");
        centerPanel.add(turnLabel, gbc);

        gbc.gridy = 1;
        turnTotalLabel = new JLabel("Turn Total: 0");
        centerPanel.add(turnTotalLabel, gbc);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        rollButton = new JButton("Roll");
        holdButton = new JButton("Hold");
        buttonPanel.add(rollButton);
        buttonPanel.add(holdButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setPlayer1Score(int score) {
        player1ScoreLabel.setText(String.valueOf(score));
    }

    public void setPlayer2Score(int score) {
        player2ScoreLabel.setText(String.valueOf(score));
    }

    public void setTurnLabel(String text) {
        turnLabel.setText(text);
    }

    public void setTurnTotal(int total) {
        turnTotalLabel.setText("Turn Total: " + total);
    }

    public JButton getRollButton() {
        return rollButton;
    }

    public JButton getHoldButton() {
        return holdButton;
    }
}
