package it.priori.battleship.logic.battleshipGame;

import it.priori.battleship.logic.enums.nodeStatus;
import it.priori.battleship.logic.exceptions.InvalidPlacementException;
import lombok.Data;

@Data
public class Node{
    private Coordinates pos;
    private nodeStatus status;

    /**
     * Singolo nodo di una nave
     * @param posx
     * @param posy
     * @throws InvalidPlacementException 
     */
    public Node(int posx, int posy, nodeStatus s) throws InvalidPlacementException {
        this.pos = new Coordinates(posx, posy);
        this.status = s;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (pos == null) {
            if (other.pos != null)
                return false;
        } else if (!pos.equals(other.pos))
            return false;
        return true;
    }
}
