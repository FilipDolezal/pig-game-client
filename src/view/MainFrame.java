package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    public final LoginView loginView;
    public final LobbyView lobbyView;
    public final GameView gameView;

    public MainFrame() {
        setTitle("Pig Game");
        // setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // We'll handle closing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    public void showView(String name) {
        cardLayout.show(mainPanel, name);
    }

    /*public void setCloseAction(Runnable action) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                action.run();
            }
        });
    }*/
}
