package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginView loginView;
    private LobbyView lobbyView;
    private GameView gameView;

    public MainFrame() {
        setTitle("Pig Game");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // We'll handle closing
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginView = new LoginView();
        lobbyView = new LobbyView();
        gameView = new GameView();

        mainPanel.add(loginView, "login");
        mainPanel.add(lobbyView, "lobby");
        mainPanel.add(gameView, "game");

        add(mainPanel);

        // Show login view by default
        cardLayout.show(mainPanel, "login");
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public LobbyView getLobbyView() {
        return lobbyView;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void showView(String name) {
        cardLayout.show(mainPanel, name);
    }

    public void setCloseAction(Runnable action) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                action.run();
            }
        });
    }
}
