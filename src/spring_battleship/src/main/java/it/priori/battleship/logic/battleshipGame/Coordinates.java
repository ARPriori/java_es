package it.priori.battleship.logic.battleshipGame;

import it.priori.battleship.logic.exceptions.InvalidPlacementException;
import lombok.Data;

@Data
public class Coordinates {
    private int posx,posy;

    public Coordinates(int posx, int posy) throws InvalidPlacementException {
        if(posx < 0 || posy < 0 || posx > 9 || posy > 9) throw new InvalidPlacementException("Specified coordinates are out of bounds");
        this.posx = posx;
        this.posy = posy;
    }
}
