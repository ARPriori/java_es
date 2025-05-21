package it.priori.battleship.logic.battleshipGame;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import it.priori.battleship.logic.exceptions.AlreadyHittenException;
import it.priori.battleship.logic.exceptions.InvalidPlacementException;
import lombok.Data;

@Data
public class BattleshipsGame {

    private Board playerBoard, aiBoard;
    private int currentPlayer;

    public BattleshipsGame() {
        playerBoard = new Board(false);
        aiBoard = new Board(true);
        currentPlayer = 1;
        aiBoard.randomizeShips();
    }

    /**
     * Computer attacca una cella random del giocatore
     * 
     * @return una mappa con coordinata x (coordX), coordinata y (coordY), e risultato uguale a 0 se mancata, 1 se colpita, 2 se affondata (result)
     */
    public Map<String, Integer> aiTurn() {
        int x, y;
        Map<String, Integer> response = new HashMap<>();
        Random r = new Random(System.currentTimeMillis());
        while (true) {
            try {
                x = r.nextInt(10);
                y = r.nextInt(10);
                int result = playerBoard.tryHit(new Node(x, y, null));
                if (playerBoard.hasLost()) {
                    endGame();
                }
                response.put("coordX", x);
                response.put("coordY", y);
                response.put("result", result);
                return response;
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

    public void endGame() {
        aiBoard.setHidden(false);
    }
}
