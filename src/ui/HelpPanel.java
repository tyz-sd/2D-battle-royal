package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextArea;

public class HelpPanel extends BasicPanel {
    MyButton backButton = new MyButton("Back");
    JTextArea textArea = new JTextArea(
        "W A S D             - Move Around\n" +
        "Left click and hold - Fire\n" +
        "F                   - Pick\n" +
        "1 2                 - Swich guns\n" +
        "3 4 5 6 7 8         - Swith medicine\n" +
        "R                   - Reload\n" +
        "Space               - Heal\n" +
        "Shift               - Sprint\n" +
        "Esc                 - Pause");
    HelpPanel(){
        super("./resources/ui/blackgun.jpg");

        backButton.setBounds(600, 700, 200, 50);
        add(backButton);

        textArea.setFont(new Font("consolas", Font.BOLD, 28));
        textArea.setBounds(350, 200, 800, 300);
        textArea.setOpaque(false);
        textArea.setForeground(Color.green);
        textArea.setEditable(false);
        add(textArea);
    }

}
