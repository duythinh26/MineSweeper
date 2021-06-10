package logic;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.CellType;

public class Board extends JPanel implements ActionListener{

    private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    public static final int N_MINES = 100;
    public static final int N_ROWS = 30;
    public static final int N_COLS = 30;

    private final int BOARD_WIDTH = N_ROWS * CELL_SIZE + 1;
    private final int BOARD_HEIGHT = N_COLS * CELL_SIZE + 1;

    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private Image[] img;

    private int allCells;
    private final JLabel status;
    private static JButton undo;
    private static JButton rule;

    protected static Square[][] gameBoard;
    private Stack step = new Stack();

    public Board(JLabel status, JButton undo, JButton rule) {   

        this.status = status;
        
        this.undo = undo;
        this.undo.addActionListener(this);

        this.rule = rule;
        this.rule.addActionListener(this);

        initBoard();
    }

    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "C:/src/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        showRules();
        newGame();
    }

    private void newGame() {

        int cell;

        var random = new Random();
        inGame = true;
        minesLeft = N_MINES;

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {

            field[i] = COVER_FOR_CELL;
        }

        status.setText(Integer.toString(minesLeft));

        int i = 0;

        while (i < N_MINES) {

            int position = (int) (allCells * random.nextDouble());

            if ((position < allCells)
                    && (field[position] != COVERED_MINE_CELL)) {

                int current_col = position % N_COLS;
                field[position] = COVERED_MINE_CELL;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - N_COLS;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + N_COLS - 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - N_COLS;
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position + N_COLS;
                if (cell < allCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (N_COLS - 1)) {
                    cell = position - N_COLS + 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + N_COLS + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }
    }

    private void find_empty_cells(int j) {

        int current_col = j % N_COLS;
        int cell;

        step.push(j *N_COLS + 1);

        if (current_col > 0) {
            cell = j - N_COLS - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS - 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - N_COLS;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + N_COLS;
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (N_COLS - 1)) {
            cell = j - N_COLS + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {

            for (int j = 0; j < N_COLS; j++) {

                int cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {

                    inGame = false;
                }

                if (!inGame) {

                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }

                } else {

                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }

        if (uncover == 0 && inGame) {

            inGame = false;
            status.setText("You won!");

        } else if (!inGame) {
            step.clear();
            status.setText("Game Over!");
        }

        if (step.empty()) {
            this.undo.setEnabled(false);
        } 
        else {
            this.undo.setEnabled(true);
        }
    }

    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            boolean doRepaint = false;

            if (!inGame) {

                newGame();
                repaint();
                step.clear();
            }

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {

                        doRepaint = true;

                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {

                            if (minesLeft > 0) {
                                field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                status.setText(msg);
                            } else {
                                status.setText("No marks left");
                            }

                            step.push(cRow * N_COLS + cCol);
                        } else {

                            field[(cRow * N_COLS) + cCol] -= MARK_FOR_CELL;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            status.setText(msg);
                        }
                    }

                } else {

                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {

                        return;
                    }

                    if ((field[(cRow * N_COLS) + cCol] > MINE_CELL)
                            && (field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL)) {

                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                        doRepaint = true;

                        step.push(cRow * N_COLS + cCol);

                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {
                            inGame = false;
                        }

                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
                            find_empty_cells((cRow * N_COLS) + cCol);
                        }
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals("Rules")) {
            	showRules();
            }
            else if (e.getActionCommand().equals("Undo")) {
                this.undo();
            }
        }
        catch (Exception oe) {
            oe.printStackTrace();
        }
    }
    
    //undo features
    private void undo() {
        if (!step.empty()) {
            int i = (Integer) step.pop();

            Square cell = gameBoard[i / N_COLS][i % N_ROWS];

            if (cell.isCoveredCell()) {
                cell.changeWhetherMarked();
                if (cell.isMarkedCell()) {
                    minesLeft = minesLeft - 1;
                }
                else {
                    minesLeft = minesLeft + 1;
                    if (!inGame) {
                        inGame = true;
                    }
                }
            }

            else if (cell.getCellType() == CellType.Bomb) {
                cell.isCovered = true;
                inGame = true;
            }

            else if (cell.getCellType() == CellType.BombNeighbor) {
                cell.isCovered = true;
            }

            if (cell.getCellType() == CellType.Empty) {
                cell.isCovered = true;
                while (!step.empty()) {
                    int j = (Integer) step.pop();
                    Square cellNext = gameBoard[j / N_COLS][j % N_ROWS];
                    if (cellNext.getCellType().equals(CellType.BombNeighbor)) {
                        step.push(j);
                        break;
                    }
                    else {
                        cellNext.isCovered = true;
                    }
                }
            }

            repaint();
        }
    }

    //show rules features
    private void showRules() {
        JOptionPane.showMessageDialog(null, "GAME RULES: \n" + "\n"
        			+ "The goal is to sweep all the “mines” or bombs from a 30x30 mine field."
        			+ " There are 100 in total. \n" + 
        			"\n" + 
        			"To obtain information on where the bomb is, left click to uncover the cells." +"\n" 
        			+ " A cell with a number reveals the number of neighboring cells containing bombs. "+"\n" 
        			+ "A cell that does not contain a bomb in its direct neighbor cells (the 8 most direct ones surrounding it)"+"\n" 
        			+ " is an empty cell,"
        			+ " and when clicked on, will reveal the entire region of all empty cells until a cell is no longer empty."+"\n" 
        			+ " Use this information plus guess work to avoid the bombs. \n" + 
        			"To mark a cell you think is a bomb, right-click on the cell and a flag will appear."+"\n" 
        			+ "You have 40 flags in total, one for each bomb. "+"\n" 
        			+ "You will be notified when you have used up all your 40 flags with a count of "+"\n" 
        			+ "how many flags you have left in the lower left corner. \n" + 
        			"The game is won when the user has successfully identified all the cells that"+"\n" 
        			+ " contain bombs and the game is lost when the player clicks on a cell "+"\n" 
        			+ "which contains a bomb. \n" +
        			"The user can “unflag” a cell by right clicking the cell again."+"\n" 
        			+ "The user can undo any number of moves for any type of move, which includes"+"\n" 
        			+ " clicking on flagged cells, empty cells, and neighbor cells."+"\n");
    }
}
