package it.priori.battleship.logic.battleshipGame;

import java.util.ArrayList;
import java.util.List;

import it.priori.battleship.logic.enums.nodeStatus;
import it.priori.battleship.logic.exceptions.InvalidPlacementException;
import lombok.Data;

@Data
public class Ship {
    private List<Node> nodes = new ArrayList<>();
    private boolean sunk;

    /**
     * 
     * @param length     lunghezza della nave
     * @param startPosx  posizione iniziale ascissa
     * @param startPosy  posizione iniziale ordinata
     * @param horizontal direzione della nave (orizzontale o verticale)
     * @throws InvalidPlacementException
     */
    public Ship(int length, int startPosx, int startPosy, boolean horizontal) throws InvalidPlacementException {
        for (int i = 0; i < length; i++) {
            if (horizontal) {
                nodes.add(new Node(startPosx + i, startPosy, nodeStatus.SHIP));
            } else {
                nodes.add(new Node(startPosx, startPosy + i, nodeStatus.SHIP));
            }
        }
    }

    public int getAttacked(Node n) {
        int index = nodes.indexOf(n);
        if (index >= 0) {
            Node shipNode = nodes.get(index);
            shipNode.setStatus(nodeStatus.SHIP_HITTEN);
            return checkStatus();
        }
        return 0;
    }

    /**
     * Controlla lo stato della nave (se affondata, colpita o intatta)
     * 
     * @return 2 se è affondata, 1 se anche solo un nodo è integro
     */
    public int checkStatus() {
        for (Node n : nodes) {
            if (!n.getStatus().hitten)
                return 1;
        }
        sunk = true;
        return 2;
    }
}
