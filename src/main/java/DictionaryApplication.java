package src.main.java;

import javax.swing.*;

import java.awt.Font;
import java.awt.Image;

public class DictionaryApplication extends JFrame {
    public DictionaryApplication() {
        super("DctionaryApplication");

        ImageIcon appIcon = new ImageIcon("src/main/resources/icon.png");
        Image image = appIcon.getImage();
        
        setIconImage(image);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(540, 540);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(true);
        
        //Logo();
        Search_Box();
    }

    private void Logo() {

    }
    
    private void Search_Box() {
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 520, 60);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 30));
    
        add(searchTextField);
    }
}
