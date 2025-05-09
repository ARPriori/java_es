package it.priori.battleship.logic.battleshipGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.priori.battleship.logic.enums.nodeStatus;
import it.priori.battleship.logic.exceptions.AlreadyHittenException;
import it.priori.battleship.logic.exceptions.InvalidPlacementException;
import lombok.Data;

@Data
public class Board {
    private List<Node> hittenNodes = new ArrayList<>();
    private List<Ship> ships = new ArrayList<>();
    private boolean hidden;

    public Board(boolean hidden) {
        this.hidden = hidden;
    }

    public void clear() {
        ships.clear();
        hittenNodes.clear();
    }

    /**
     * Posiziona tutte le navi (una da 4, tre da 3, tre da 2, due da 1)
     * randomizzandone posizione e direzione
     */
    public void randomizeShips() {
        ships.clear();
        int[] shipSizes = { 4, 3, 3, 3, 2, 2, 2, 1, 1 };
        Random r = new Random(System.currentTimeMillis());
        for (int s : shipSizes) {
            boolean placed = false;
            while (!placed) {
                try {
                    placeShip(s, r.nextInt(10), r.nextInt(10), r.nextBoolean());
                    placed = true;
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Posiziona una barca. Se esce dai bordi rende la posizione iniziale quella
     * finale fino a quando non rientra nei limiti della griglia.
     * 
     * @param length     lunghezza della barca
     * @param posx       posizione iniziale sulle ascisse
     * @param posy       posizione iniziale sulle ordinate
     * @param horizontal direzione della barca
     * @throws InvalidPlacementException se la barca si sovrappone a un'altra
     */
    public void placeShip(int length, int posx, int posy, boolean horizontal) throws InvalidPlacementException {

        // Adjust position if the ship goes out of bounds
        while ((posx + length - 1 > 9 && horizontal) || (posy + length - 1 > 9 && !horizontal)) {
            if (horizontal) {
                posx -= length - 1;
            } else {
                posy -= length - 1;
            }
        }

        // Check for overlaps with existing ships
        for (Ship s : ships) {
            for (Node n : s.getNodes()) {
                for (int i = 0; i < length; i++) {
                    int checkX = horizontal ? posx + i : posx;
                    int checkY = horizontal ? posy : posy + i;
                    if (n.getPos().getPosx() == checkX && n.getPos().getPosy() == checkY) {
                        throw new InvalidPlacementException("Ship placement overlaps with another ship");
                    }
                }
            }
        }

        ships.add(new Ship(length, posx, posy, horizontal));
    }

    /**
     * Mossa: colpisci un nodo e scopri se hai colpito qualcosa
     * 
     * @param node nodo da attaccare
     * @return 0 se mancata, 1 se colpita, 2 se affondata
     * @throws AlreadyHittenException
     */
    public int tryHit(Node node) throws AlreadyHittenException {
        if (hittenNodes.contains(node))
            throw new AlreadyHittenException("The given coordinates have already been hit");
        hittenNodes.add(node);
        for (Ship s : ships) {
            for (Node n : s.getNodes()) {
                if (n.equals(node)) {
                    n.setStatus(nodeStatus.SHIP_HITTEN);
                    node.setStatus(nodeStatus.SHIP_HITTEN);
                    return s.checkStatus(); // Return 1 if hit, 2 if sunk
                }
            }
        }
        node.setStatus(nodeStatus.WATER_HITTEN);
        return 0;
    }

    public boolean hasLost() {
        for (Ship s : ships) {
            if (!s.isSunk())
                return false;
        }
        return true;
    }
}
