package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GameView extends JPanel {

    // Opponent components
    private JPanel opponentPanel;
    private JLabel opponentTotalScoreLabel;
    private JLabel opponentTurnScoreLabel;
    private JLabel opponentRollLabel;
    private TitledBorder opponentBorder;

    // Player components
    private JPanel playerPanel;
    private JLabel playerTotalScoreLabel;
    private JLabel playerTurnScoreValueLabel; // The big one
    private TitledBorder playerBorder;

    // Game action components
    private JButton rollButton;
    private JButton holdButton;
    private JButton quitButton;

    // Colors for highlighting
    private final Color activeTurnColor = Color.GREEN;
    private final Color inactiveTurnColor = Color.LIGHT_GRAY;

    public GameView() {
        super(new BorderLayout());

        // Create opponent and player panels
        opponentPanel = createOpponentPanel();
        playerPanel = createPlayerPanel();

        // Main game area with split view
        JPanel gameArea = new JPanel(new GridLayout(1, 2, 10, 10));
        gameArea.add(opponentPanel);
        gameArea.add(playerPanel);
        gameArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for the quit button
        quitButton = new JButton("Quit Game");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(quitButton);

        add(topPanel, BorderLayout.NORTH);
        add(gameArea, BorderLayout.CENTER);
    }

    private JPanel createOpponentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        opponentBorder = BorderFactory.createTitledBorder("Opponent");
        panel.setBorder(opponentBorder);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        opponentTotalScoreLabel = new JLabel("Score: 0");
        opponentTotalScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(opponentTotalScoreLabel, gbc);

        opponentTurnScoreLabel = new JLabel("Turn: 0");
        opponentTurnScoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        panel.add(opponentTurnScoreLabel, gbc);

        opponentRollLabel = new JLabel("Rolled: -");
        opponentRollLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 2;
        panel.add(opponentRollLabel, gbc);

        return panel;
    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        playerBorder = BorderFactory.createTitledBorder("You");
        panel.setBorder(playerBorder);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        playerTotalScoreLabel = new JLabel("Your Score: 0");
        playerTotalScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(playerTotalScoreLabel, gbc);

        JLabel turnScoreTitleLabel = new JLabel("Current Turn Score");
        turnScoreTitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        panel.add(turnScoreTitleLabel, gbc);

        playerTurnScoreValueLabel = new JLabel("0");
        playerTurnScoreValueLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gbc.gridy = 2;
        panel.add(playerTurnScoreValueLabel, gbc);

        // Buttons
        rollButton = new JButton("Roll");
        holdButton = new JButton("Hold");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(rollButton);
        buttonPanel.add(holdButton);
        gbc.gridy = 3;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    public void gameStart(String opponentNick, boolean isPlayerTurn) {
        opponentBorder.setTitle(opponentNick);
        updateTurnHighlight(isPlayerTurn);
        opponentPanel.repaint(); // to show the new title
    }

    public void gameState(int myScore, int oppScore, int turnScore, int roll, boolean isPlayerTurn) {
        playerTotalScoreLabel.setText("Your Score: " + myScore);
        opponentTotalScoreLabel.setText("Score: " + oppScore);

        if (isPlayerTurn) {
            playerTurnScoreValueLabel.setText(String.valueOf(turnScore));
            opponentTurnScoreLabel.setText("Turn: 0");
            opponentRollLabel.setText("Rolled: -");
        } else {
            opponentTurnScoreLabel.setText("Turn: " + turnScore);
            opponentRollLabel.setText("Rolled: " + roll);
            playerTurnScoreValueLabel.setText("0");
        }

        rollButton.setEnabled(isPlayerTurn);
        holdButton.setEnabled(isPlayerTurn);
        updateTurnHighlight(isPlayerTurn);
    }

    private void updateTurnHighlight(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            playerBorder.setTitleColor(activeTurnColor);
            opponentBorder.setTitleColor(inactiveTurnColor);
        } else {
            playerBorder.setTitleColor(inactiveTurnColor);
            opponentBorder.setTitleColor(activeTurnColor);
        }
        playerPanel.repaint();
        opponentPanel.repaint();
    }

    public JButton getRollButton() {
        return rollButton;
    }

    public JButton getHoldButton() {
        return holdButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }
}

