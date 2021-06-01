package logic;

public class Square {
    private boolean isOpen;
    private boolean hasMine;
    private boolean isTarget;
    private int numMineAround;

    public Square() {
        isOpen = false;
        hasMine = false;
        isTarget = false;
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

    public void setMine(boolean hasMine) {
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
}
