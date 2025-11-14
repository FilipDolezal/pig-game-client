package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private final JTextField ipField;
    private final JTextField portField;
    private final JTextField nicknameField;
    private final JButton loginButton;

    public LoginView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        add(new JLabel("IP:"), gbc);

        gbc.gridx = 1;
        ipField = new JTextField("localhost", 20);
        add(ipField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Port:"), gbc);

        gbc.gridx = 1;
        portField = new JTextField("12345", 20);
        add(portField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Nickname:"), gbc);

        gbc.gridx = 1;
        nicknameField = new JTextField(20);
        add(nicknameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        add(loginButton, gbc);
    }

    public String getIp() {
        return ipField.getText();
    }

    public String getPort() {
        return portField.getText();
    }

    public String getNickname() {
        return nicknameField.getText();
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}

