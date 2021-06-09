package minesweeper;
import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import logic.Board;

public class Minesweeper extends JFrame {
    private JLabel status;
    private JPanel buttonPanel;
    private JButton undoButton;
    private JButton rulesButton;
    protected Board board;

    public Minesweeper() throws IOException {
        buttonPanel = new JPanel();
        undoButton = new JButton("Undo");
        rulesButton = new JButton("Rules");
    	status = new JLabel("");

        add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.add(undoButton, BorderLayout.SOUTH);
        buttonPanel.add(rulesButton, BorderLayout.NORTH);

        add(status, BorderLayout.SOUTH);
        
        board = new Board(status, undoButton, rulesButton);
        add(board);

        setResizable(false);
        pack();

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException {
     	Minesweeper ms = new Minesweeper();
    	ms.setVisible(true);
    }
}
