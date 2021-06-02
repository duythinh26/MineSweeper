package gui.panel;
 
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import gui.ICommon;
import gui.ITrans;
import logic.Board;
 
public class ControlPanel extends JPanel implements ICommon {
  private static final long serialVersionUID = 3141592653589793L;
  public static final boolean WIN = true;
  public static final boolean LOSE = false;
  private JLabel lbNumSquareClosed;
  private JLabel lbNotify;
  private JButton btRestart;
  private ITrans listener;
 
  public ControlPanel() {
    initComp();
    addComp();
    addEvent();
  }
 
  @Override
  public void initComp() {
    setLayout(null);
  }
 
  @Override
  public void addComp() {
    Font font = new Font("Dialog", Font.PLAIN, 20);
 
    lbNumSquareClosed = new JLabel();
    lbNumSquareClosed.setFont(font);
    lbNumSquareClosed.setText("Number of closed cells: " + Board.NUM_ROWS*Board.NUM_COLUMNS);
    lbNumSquareClosed.setBounds(10, 10, 250, 40);
    add(lbNumSquareClosed);
 
    lbNotify = new JLabel();
    lbNotify.setFont(font);
    lbNotify.setBounds(270, 10, 200, 40);
    add(lbNotify);
 
    btRestart = new JButton();
    btRestart.setFont(font);
    btRestart.setText("Play again");
    btRestart.setBounds(490, 10, 200, 40);
    add(btRestart);
  }
 
  @Override
  public void addEvent() {
    btRestart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        listener.restart();
        lbNumSquareClosed.setText("Number of closed cells: " + Board.NUM_ROWS*Board.NUM_COLUMNS);
        lbNotify.setText("");
      }
    });
  }
 
  public void addListener(ITrans event) {
    listener = event;
  }
 
  public void updateStatus(int numSquareClosed) {
    lbNumSquareClosed.setText("Number of closed cells: " + numSquareClosed);
    if (numSquareClosed == Board.NUM_MINES) {
      lbNotify.setText("YOU WIN");
      lbNotify.setForeground(Color.blue);
    } else if (numSquareClosed == 0) {
      lbNotify.setText("YOU LOSE");
      lbNotify.setForeground(Color.red);
    }
  }
}
