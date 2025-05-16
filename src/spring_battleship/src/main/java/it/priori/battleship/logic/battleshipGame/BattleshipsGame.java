package it.priori.battleship.logic.battleshipGame;

import java.util.Random;

import it.priori.battleship.logic.exceptions.AlreadyHittenException;
import it.priori.battleship.logic.exceptions.InvalidPlacementException;
import lombok.Data;

@Data
public class BattleshipsGame {

    private Board playerBoard, aiBoard;
    private int currentPlayer;
    // private static BattleshipsGame instance;

    // public static BattleshipsGame getInstance() {
    // if (instance == null) {
    // instance = new BattleshipsGame();
    // }
    // return instance;
    // }

    public BattleshipsGame() {
        playerBoard = new Board(false);
        aiBoard = new Board(true);
        currentPlayer = 1;
        aiBoard.randomizeShips();
    }

    /**
     * Computer attacca una cella random del giocatore
     * 
     * @return 0 se mancata, 1 se colpita, 2 se affondata
     */
    public int aiTurn() {
        Random r = new Random(System.currentTimeMillis());
        while (true) {
            try {
                int result = playerBoard.tryHit(new Node(r.nextInt(10), r.nextInt(10), null));
                if (playerBoard.hasLost()) {
                    endGame();
                }
                return result;
            } catch (Exception e) {
            }
        }
    }

    /**
     * Giocatore attacca in pos computers
     * 
     * @param pos
     * @return 0 se mancata, 1 se colpita, 2 se affondata
     * @throws AlreadyHittenException
     * @throws InvalidPlacementException
     */
    public int playerTurn(Coordinates pos) throws AlreadyHittenException, InvalidPlacementException {
        int result = aiBoard.tryHit(new Node(pos.getPosx(), pos.getPosy(), null));
        if (aiBoard.hasLost()) {
            endGame();
        }
        return result;
    }

    // public void resetGame() {
    // instance = new BattleshipsGame();
    // }

    public void endGame() {
        aiBoard.setHidden(false);
    }
}
