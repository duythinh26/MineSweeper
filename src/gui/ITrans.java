package gui;

import logic.Square;

public interface ITrans {
    Square[][] getListSquare();

    void play();
    void target();
    void restart();
}
