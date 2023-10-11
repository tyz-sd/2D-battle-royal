package ui;

public class PausePanel extends BasicPanel {
    MyButton continueButton = new MyButton("Continue");
    MyButton escButton = new MyButton("Escape");

    PausePanel(){
        super("./resources/ui/blackgun.jpg");

        continueButton.setBounds(600, 300, 200, 50);
        add(continueButton);
        escButton.setBounds(600, 400, 200, 50);
        add(escButton);
    }

}
