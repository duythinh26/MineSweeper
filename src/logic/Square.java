package logic;

import gui.ImageName;
import gui.SquareType;

public class Square {
    
    private boolean isOpen;
    private boolean hasMine;
    private boolean isTarget;
    private int numMineAround;
    protected boolean isCovered = true;
    protected SquareType squareType = SquareType.Empty;
    protected boolean isMarked = false;

    public Square(String isCovered, String isMarked) {

        isOpen = false;
        hasMine = false;
        isTarget = false;

        if(isCovered.equals("true")) {

            this.isCovered = true;
        }
        else {

            this.isCovered = false;
        }
        
        if(isMarked.equals("true")) {

            this.isMarked = true;
        }
        else {

            this.isMarked = false;
        }
    }

    public Square() {
    }

    public void squareCount() {
	}

    public boolean isOpen() {

        return isOpen;
    }

    public void setIsOpen(boolean isOpen){

        this.isOpen = isOpen;
    }

    public boolean isHasMine() {

        return hasMine;
    }

    public void setHasMine(boolean hasMine) {

        this.hasMine = hasMine;
    }

    public boolean isTarget() {

        return isTarget;
    }
    
    public void setTarget(boolean isTarget) {

        this.isTarget = isTarget;
    }

    public int getNumMineAround() {

        return numMineAround;
    }

    public void setNumMineAround(int numMineAround) {

        this.numMineAround = numMineAround;
    }

    public void flipUp() {

		this.isCovered = false;
	}
	
	public SquareType getSquareType() {

		return this.squareType;
	}
	
	public boolean isCoveredSquare() {

		return this.isCovered;
	}
	
	public boolean isMarkedSquare() {

		return this.isMarked;
	}
	
	public void changeWhetherMarked() {

		this.isMarked = !isMarked;
	}

    public String getImageName() {

        return ImageName.Covered.toString();
    }
}
