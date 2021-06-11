package gui.panel;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import gui.ICommon;
import gui.ITrans;
import logic.Board;

public class BoardPanel extends JPanel implements ICommon {

  private static final long serialVersionUID = -693640021L;
  private Label[][] labelSquare;
  private ITrans listener;
  private int numSquareClosed;

  public BoardPanel() {

    addComp();
    initComp();
    addEvent();
  }

  @Override
  public void addComp() {

    Border border = BorderFactory.createLineBorder(Color.red, 1);
    labelSquare = new Label[Board.N_ROWS][Board.N_COLS];

    for (int i = 0; i < labelSquare.length; i++){
      for (int j = 0; j < labelSquare.length; j++){

        labelSquare[i][j] = new Label();
        labelSquare[i][j].setOpaque(true);
        labelSquare[i][j].setBackground(new Color(240, 240, 240));
        labelSquare[i][j].setBorder(border);
        labelSquare[i][j].setHorizontalAlignment(JLabel.CENTER);
        labelSquare[i][j].setVerticalAlignment(JLabel.CENTER);
        add(labelSquare[i][j]);
      }
    }
  }

  @Override
  public void initComp() {

    setLayout(new GridLayout(Board.N_ROWS, Board.N_COLS));
  }

  @Override
  public void addEvent() {

    for (int i = 0; i < labelSquare.length; i++) {
      for (int j = 0; j < labelSquare.length; j++) {

        labelSquare[i][j].x = i;
        labelSquare[i][j].y = j;
        labelSquare[i][j].addMouseListener(new MouseAdapter() {

          @Override
          public void mouseReleased(MouseEvent e) {

            Label label = (Label) e.getComponent();

            if (e.getButton() == MouseEvent.BUTTON1) {

              listener.play(label.x, label.y);
            } 
            else if (e.getButton() == MouseEvent.BUTTON3) {

              listener.target(label.x, label.y);
            }
          }
        });
      }
    }
  }

  private class Label extends JLabel {

    private static final long serialVersionUID = 929888149L;
    private int x;
    private int y;
  }
      
  public int getNumSquareClosed() {
    
    return numSquareClosed;
  }
}
