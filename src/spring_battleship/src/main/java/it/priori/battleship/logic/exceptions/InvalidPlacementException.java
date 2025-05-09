package it.priori.battleship.logic.exceptions;

public class InvalidPlacementException extends Exception{
    public InvalidPlacementException(String msg) {
        super(msg);
    }
}