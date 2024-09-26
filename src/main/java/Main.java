package src.main.java;

import javax.swing.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    public Main() {
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
        //Text Field
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 520, 50);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 30));
        
        add(searchTextField);

        //Get Search button image
        ImageIcon icon = new ImageIcon("src/main/resources/Search_Button.png");
        Image img = icon.getImage();
        Image ScaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(ScaledImg);

        //Create Search button
        JButton Search_Button = new JButton(resizedIcon);
        Search_Button.setBounds(15 + 525 , 15, 50, 50);
        
        add(Search_Button);

        Search_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String SearchText = searchTextField.getText();
                JOptionPane.showMessageDialog(searchTextField,"Searched: " + SearchText);
            }
        });

    }
}
