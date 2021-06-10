package logic;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.ImageName;
import gui.SquareType;

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

    public static int N_MINES = 100;
    public static int N_ROWS = 30;
    public static int N_COLS = 30;

    private final int BOARD_WIDTH = N_ROWS * CELL_SIZE + 1;
    private final int BOARD_HEIGHT = N_COLS * CELL_SIZE + 1;

    private boolean inGame;
    private int minesLeft;

    private int allCells;
    private final JLabel status;
    private static JButton undo;
    private static JButton rule;

    private java.util.Map<String, Image> images;

    protected static Square[][] field;
    private Stack step = new Stack();

    public Board(JLabel status, JButton undo, JButton rule) throws IOException {   

        this.status = status;
        
        this.undo = undo;
        this.undo.addActionListener(this);

        this.rule = rule;
        this.rule.addActionListener(this);

        initBoard();
    }

    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        images = new java.util.HashMap<>();

        for (int i = 1; i < 8; i++) {
            String path = "resources/" + i + ".png";
            images.put(Integer.toString(i), (new ImageIcon(path)).getImage());
        }

        images.put("Bomb", (new ImageIcon("resources/9.png")).getImage());
        images.put("Covered", (new ImageIcon("resources/10.png")).getImage());
        images.put("Empty", (new ImageIcon("resources/0.png")).getImage());
        images.put("Marked", (new ImageIcon("resources/11.png")).getImage());
        images.put("Wrongmarked", (new ImageIcon("resources/12.png")).getImage());

        addMouseListener(new MinesAdapter());

        showRules();

        newGame();
    }

    private void newGame() {

        Random random = new Random();
        inGame = true;
        minesLeft = N_MINES;

        allCells = N_ROWS * N_COLS;

        field = new Square[N_ROWS][N_COLS];

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {

                field[i][j] = new EmptySquare();
            }
        }

        status.setText(Integer.toString(minesLeft));

        int i = 0;

        while (i < N_MINES) {

            int positionX = (int) (random.nextInt(N_ROWS));
            int positionY = (int) (random.nextInt(N_COLS));

            if (field[positionX][positionY].getSquareType() != SquareType.Bomb) {

                field[positionX][positionY] = new BombSquare();

                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        if ((x != 0 || y != 0) 
                            && positionX + x < N_COLS 
                            && positionY + y < N_ROWS 
                            && positionX + x >= 0 && positionY + y >=0) {
                                SquareType typeOfSquare = field[positionX + x][positionY + y].getSquareType();
                                if (typeOfSquare != SquareType.Bomb) {
                                    if (typeOfSquare != SquareType.BombNeighbor) {
                                        NeighborBomb neighbor = new NeighborBomb();
                                        neighbor.squareCount();
                                        field[positionX + x][positionY + y] = neighbor;
                                    }
                                    else {
                                        field[positionX + x][positionY + y].squareCount();
                                    }
                                }
                        }
                    }
                }
                i++;
            }
        }   
    }

    private void find_empty_cells(int x, int y) {

        field[x][y].flipUp();

        step.push(x *N_COLS + y);
        
        for(int dx = -1; dx <= 1; dx++) {
        	for(int dy = -1; dy <= 1; dy++) {
        		if((dx != 0 || dy != 0) 
                    && x + dx < N_COLS 
                    && y + dy < N_ROWS 
        			&& x + dx >= 0 
                    && y + dy >= 0) {
                            
        			    SquareType typeOfSquare = field[x + dx][y + dy].getSquareType();
                        
                        if(typeOfSquare == SquareType.BombNeighbor && field[x + dx][y + dy].isCoveredSquare()) {
                            field[x + dx][y + dy].flipUp();
                        }
                        else if (typeOfSquare == SquareType.Empty && field[x + dx][y + dy].isCoveredSquare()) {
                                find_empty_cells(x + dx, y + dy);
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

                Square square = field[i][j];
                String imgName = square.getImageName(); 

                if (inGame 
                    && square.getSquareType() == SquareType.Bomb 
                    && !square.isCoveredSquare()) {

                    inGame = false;
                }

                if (!inGame) {

                    if (square.getSquareType() == SquareType.Bomb && !square.isMarkedSquare()) {
                        square.flipUp();
                        imgName = ImageName.Bomb.toString();
                    } 
                    else if (square.isCoveredSquare() && square.getSquareType() == SquareType.Bomb && square.isMarkedSquare()) {
                        imgName = ImageName.Marked.toString();
                    } 
                    else if (square.isCoveredSquare() && square.getSquareType() != SquareType.Bomb && square.isMarkedSquare()) {//wrongly marked squares
                        imgName = ImageName.Wrongmarked.toString();
                    } 
                    else if (square.isCoveredSquare()) {
                        imgName = ImageName.Covered.toString();
                    }

                } 
                else {

                    if (square.isMarkedSquare()) {
                    	imgName = ImageName.Marked.toString();
                    } 
                    else if (square.isCoveredSquare()) {
                    	imgName = ImageName.Covered.toString();
                        uncover++;
                    }
                }

                g.drawImage(images.get(imgName), (j * CELL_SIZE),
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

                    if (field[cRow][cCol].isCoveredSquare()) {

                        doRepaint = true;

                        if (!field[cRow][cCol].isMarkedSquare() 
                            && minesLeft > 0) {

                                Square square = field[cRow][cCol];
                                square.changeWhetherMarked();
                                minesLeft = minesLeft - 1;

                                if (minesLeft > 0) {
                                    String msg = Integer.toString(minesLeft);
                                    status.setText(msg);
                                } 
                                else {
                                    status.setText("No marks left");
                                }

                            step.push(cRow * N_COLS + cCol);
                        } 
                        else if (field[cRow][cCol].isMarkedSquare()){

                            field[cRow][cCol].changeWhetherMarked();
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            status.setText(msg);
                        }
                    }

                } 
                else {

                    if (field[cRow][cCol].isMarkedSquare()) {

                        return;
                    }

                    if (field[cRow][cCol].isCoveredSquare()
                            //&& (gameBoard[cRow][cCol].getCellType() == CellType.Bomb )
                            ) {
                    	
                    	field[cRow][cCol].flipUp();

                        doRepaint = true;
                        step.push(cRow * N_COLS + cCol);
                        
                        //if user clicks on mine, game is over
                        if (field[cRow][cCol].getSquareType() == SquareType.Bomb
                        		&& !field[cRow][cCol].isCoveredSquare()) {
                            inGame = false;
                        }
                        
                        //if user clicks on empty cell, call empty cell function which will handle the situation
                        if (field[cRow][cCol].getSquareType() == SquareType.Empty) {
                            find_empty_cells(cRow, cCol);
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
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    //undo features
    private void undo() {
        if (!step.empty()) {
            int i = (Integer) step.pop();

            Square square = field[i / N_COLS][i % N_ROWS];

            if (square.isCoveredSquare()) {
                square.changeWhetherMarked();
                if (square.isMarkedSquare()) {
                    minesLeft = minesLeft - 1;
                }
                else {
                    minesLeft = minesLeft + 1;
                    if (!inGame) {
                        inGame = true;
                    }
                }
            }

            else if (square.getSquareType() == SquareType.Bomb) {
                square.isCovered = true;
                inGame = true;
            }

            else if (square.getSquareType() == SquareType.BombNeighbor) {
                square.isCovered = true;
            }

            String msg = Integer.toString(minesLeft);
            this.status.setText("Flags left: " + msg);

            if (square.getSquareType() == SquareType.Empty) {
                square.isCovered = true;
                while (!step.empty()) {
                    int j = (Integer) step.pop();
                    Square squareNext = field[j / N_COLS][j % N_ROWS];
                    if (squareNext.getSquareType().equals(SquareType.BombNeighbor)) {
                        step.push(j);
                        break;
                    }
                    else {
                        squareNext.isCovered = true;
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
