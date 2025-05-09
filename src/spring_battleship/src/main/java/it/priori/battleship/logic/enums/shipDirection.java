package it.priori.battleship.logic.enums;

public enum shipDirection {
    HORIZONTAL(true),
    VERTICAL(false);

    public boolean isHorizontal;

    private shipDirection(boolean b){
        this.isHorizontal = b;
    }
}
