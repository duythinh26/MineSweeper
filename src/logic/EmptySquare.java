package logic;

import gui.ImageName;
import gui.SquareType;

public class EmptySquare extends Square{
    
    public EmptySquare() {
        super();
        this.squareType = squareType;
    }

    public EmptySquare(String isCovered, String isMarked) {
		super(isCovered, isMarked);
		this.squareType = SquareType.Empty;
	}

    @Override
    public SquareType getSquareType() {
        return this.squareType;
    }

    @Override
    public String getImageName() {
        return ImageName.Empty.toString();
    }
}
