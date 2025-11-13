package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private JTextField nicknameField;
    private JButton loginButton;

    public LoginView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        add(new JLabel("Nickname:"), gbc);

        gbc.gridx = 1;
        nicknameField = new JTextField(20);
        add(nicknameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        add(loginButton, gbc);
    }

    public String getNickname() {
        return nicknameField.getText();
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}
