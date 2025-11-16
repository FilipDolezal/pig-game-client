package view;

import javax.swing.*;
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
    private JLabel playerRollLabel;
    private TitledBorder playerBorder;

    // Game action components
    private JButton rollButton;
    private JButton holdButton;
    private JButton quitButton;
    private JButton leaveButton;

    // Colors for highlighting
    private final Color activeTitleColor = Color.BLACK;
    private final Color inactiveTitleColor = Color.GRAY;
    private final Color activeBackgroundColor = new Color(220, 255, 220); // Light green
    private final Color inactiveBackgroundColor = UIManager.getColor("Panel.background");


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
        leaveButton = new JButton("Leave Room");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(quitButton);
        topPanel.add(leaveButton);

        add(topPanel, BorderLayout.NORTH);
        add(gameArea, BorderLayout.CENTER);

        showLeaveButton(false); // Default to showing quit button
    }

    private JPanel createOpponentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        opponentBorder = BorderFactory.createTitledBorder("Opponent");
        opponentBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
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
        playerBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
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

        playerRollLabel = new JLabel("You rolled: -");
        playerRollLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 3;
        panel.add(playerRollLabel, gbc);

        // Buttons
        rollButton = new JButton("Roll");
        holdButton = new JButton("Hold");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(rollButton);
        buttonPanel.add(holdButton);
        gbc.gridy = 4;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    public void gameStart(String opponentNick, boolean isPlayerTurn) {
        setWaitingForOpponent(false);
        opponentBorder.setTitle(opponentNick);
        updateTurnHighlight(isPlayerTurn);
        opponentPanel.repaint(); // to show the new title
    }

    public void setWaitingForOpponent(boolean waiting) {
        if (waiting) {
            opponentTotalScoreLabel.setText("Waiting for opponent...");
            opponentTotalScoreLabel.setFont(new Font("Arial", Font.ITALIC, 18));
            opponentTurnScoreLabel.setVisible(false);
            opponentRollLabel.setVisible(false);
        } else {
            opponentTotalScoreLabel.setText("Score: 0");
            opponentTotalScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
            opponentTurnScoreLabel.setVisible(true);
            opponentRollLabel.setVisible(true);
        }
    }

    public void gameState(int myScore, int oppScore, int turnScore, int roll, boolean isPlayerTurn) {
        playerTotalScoreLabel.setText("Your Score: " + myScore);
        opponentTotalScoreLabel.setText("Score: " + oppScore);

        // Reset conditional labels
        playerRollLabel.setText("You rolled: -");
        opponentRollLabel.setText("Rolled: -");

        if (isPlayerTurn) {
            // It's my turn.
            playerTurnScoreValueLabel.setText(String.valueOf(turnScore));
            opponentTurnScoreLabel.setText("Turn: 0");

            if (turnScore == 0) { // My turn is just starting
                // Opponent's turn just ended. How?
                if (roll == 1) {
                    opponentRollLabel.setText("Opponent BUST");
                } else if (roll == 0) {
                    opponentRollLabel.setText("Opponent held");
                }
            } else { // My turn is in progress
                if (roll > 1) {
                    playerRollLabel.setText("You rolled: " + roll);
                }
            }
        } else { // It's opponent's turn.
            opponentTurnScoreLabel.setText("Turn: " + turnScore);
            playerTurnScoreValueLabel.setText("0");

            if (turnScore == 0) { // Opponent's turn is just starting
                // My turn just ended. How?
                if (roll == 1) {
                    playerRollLabel.setText("You BUST");
                } else if (roll == 0) {
                    playerRollLabel.setText("You held");
                }
            } else { // Opponent's turn is in progress
                if (roll > 1) {
                    opponentRollLabel.setText("Rolled: " + roll);
                }
            }
        }

        rollButton.setEnabled(isPlayerTurn);
        holdButton.setEnabled(isPlayerTurn);
        updateTurnHighlight(isPlayerTurn);
    }

    private void updateTurnHighlight(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            playerPanel.setBackground(activeBackgroundColor);
            playerBorder.setTitleColor(activeTitleColor);
            opponentPanel.setBackground(inactiveBackgroundColor);
            opponentBorder.setTitleColor(inactiveTitleColor);
        } else {
            playerPanel.setBackground(inactiveBackgroundColor);
            playerBorder.setTitleColor(inactiveTitleColor);
            opponentPanel.setBackground(activeBackgroundColor);
            opponentBorder.setTitleColor(activeTitleColor);
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

    public JButton getLeaveButton() {
        return leaveButton;
    }

    public void showLeaveButton(boolean show) {
        leaveButton.setVisible(show);
        quitButton.setVisible(!show);
    }

    public void resetView() {
        playerTotalScoreLabel.setText("Your Score: 0");
        opponentTotalScoreLabel.setText("Score: 0");
        playerTurnScoreValueLabel.setText("0");
        opponentTurnScoreLabel.setText("Turn: 0");
        playerRollLabel.setText("You rolled: -");
        opponentRollLabel.setText("Rolled: -");

        opponentBorder.setTitle("Opponent");

        // Reset highlighting
        playerPanel.setBackground(inactiveBackgroundColor);
        playerBorder.setTitleColor(inactiveTitleColor);
        opponentPanel.setBackground(inactiveBackgroundColor);
        opponentBorder.setTitleColor(inactiveTitleColor);

        rollButton.setEnabled(false);
        holdButton.setEnabled(false);
        quitButton.setEnabled(true); // Keep it enabled, but it might be hidden
        leaveButton.setEnabled(true);

        showLeaveButton(false); // Default to showing quit button

        setWaitingForOpponent(false);

        playerPanel.repaint();
        opponentPanel.repaint();
    }

    public void resumeGame() {
        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
    }
}

