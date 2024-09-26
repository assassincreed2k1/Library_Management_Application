package src.main.java;

import javax.swing.SwingUtilities;

public class AppLauncher {
    public static void main(String args[]) {
        // Launch App code
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
