package logic;

import gui.SquareType;

public class BombSquare extends Square{

    public BombSquare() {
		super();
        this.squareType = SquareType.Bomb;
	}
	
	public BombSquare(String isCovered, String isMarked) {
		super(isCovered, isMarked);
		this.squareType = SquareType.Bomb;
	}
        
    @Override
    public SquareType getSquareType() {
        return this.squareType;
    }
}
