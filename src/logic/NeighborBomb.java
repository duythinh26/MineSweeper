package logic;

import gui.SquareType;

public class NeighborBomb extends Square{

    private int bombSquareCount;
	
	public NeighborBomb() {
		super();
        this.squareType = SquareType.BombNeighbor;
	}
	
	
	public NeighborBomb(String isCovered, String isMarked, int bombSquareCount) {
		super(isCovered, isMarked);
		this.squareType = SquareType.BombNeighbor;
		this.bombSquareCount = bombSquareCount;
	}
	
	//want to call this function when initializing board, which automatically  
	@Override
    public void squareCount() {
		this.bombSquareCount ++;
	}
        
    @Override
    public SquareType getSquareType() {
        return this.squareType;
    }

	@Override
	public String getImageName(){
		return Integer.toString(bombSquareCount);
	}
}
