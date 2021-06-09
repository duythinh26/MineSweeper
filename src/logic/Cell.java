package logic;

import gui.CellType;

public class Cell {
    public boolean isCovered = true;
    public CellType cellType = CellType.Empty;
    public boolean isMarked = false;

    public Cell(String isCovered, String isMarked) {
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

    public void flipUp() {
		this.isCovered = false;
	}
	
	public CellType getCellType() {
		return this.cellType;
	}
	
	public boolean isCoveredCell() {
		return this.isCovered;
	}
	
	public boolean isMarkedCell() {
		return this.isMarked;
	}
	
	public void changeWhetherMarked() {
		this.isMarked = !isMarked;
	}
}
