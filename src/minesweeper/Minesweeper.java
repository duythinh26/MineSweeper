package minesweeper;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import logic.Board;

public class Minesweeper extends JFrame {
    private JLabel status;

    public Minesweeper() {
    	status = new JLabel("");
        add(status, BorderLayout.SOUTH);
        add(new Board(status));
        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
     	Minesweeper ms = new Minesweeper();
    	ms.setVisible(true);
    }
}
