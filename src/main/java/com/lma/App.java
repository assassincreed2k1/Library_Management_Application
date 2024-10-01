package com.lma;

import javax.swing.*;
/**
 * App Launcher
 *
 */

public class App {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}