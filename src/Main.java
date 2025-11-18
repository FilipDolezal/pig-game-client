import controller.NetworkController;
import controller.ViewController;
import view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            ViewController viewController = new ViewController(mainFrame);
            NetworkController networkController = new NetworkController(viewController);
            viewController.setNetworkController(networkController);
            mainFrame.setCloseAction(viewController::onCloseAction);
            mainFrame.setVisible(true);
        });
    }
}