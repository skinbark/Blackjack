// package cards;

import java.net.URL;
import javax.swing.*;

class Blackjack extends JFrame {

    public static void main(String[] args) {
        Blackjack window = new Blackjack();
        window.setTitle("Blackjack");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameModel game = new GameModel();
        window.setContentPane(new CardTable(game));
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
