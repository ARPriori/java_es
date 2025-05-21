package it.priori.battleship.controller;

import org.springframework.web.bind.annotation.*;
import it.priori.battleship.logic.battleshipGame.BattleshipsGame;
import it.priori.battleship.logic.battleshipGame.Board;
import it.priori.battleship.logic.battleshipGame.Coordinates;
import it.priori.battleship.logic.battleshipGame.Node;
import it.priori.battleship.logic.battleshipGame.Ship;
import it.priori.battleship.logic.exceptions.AlreadyHittenException;
import it.priori.battleship.logic.exceptions.InvalidPlacementException;

import java.util.*;

@RestController
@RequestMapping("/api")
public class FieldController {

    private BattleshipsGame game = new BattleshipsGame();

    // INIZIALIZZAZIONE
    @GetMapping("/popola-griglie")
    public Map<String, Object> popolaGriglie() {
        Map<String, Object> response = new HashMap<>();

        Board computerBoard = game.getAiBoard();

        // Prepara la risposta
        response.put("computerShips", convertShipsToIndices(computerBoard));

        return response;
    }

    //RANDOMIZZAZIONE PLAYER BOARD
    @GetMapping("/randomizza-player")
    public Map<String, Object> randomizzaPlayer() {
        Map<String, Object> response = new HashMap<>();

        // Posiziona navi random per giocatore
        Board playerBoard = game.getPlayerBoard();
        playerBoard.randomizeShips();

        response.put("playerShips", convertShipsToIndices(playerBoard));

        return response;
    }

    /**
     * Crea una lista di interi convertendo le coordinate di ogni barca (cella) in un indice che va da 0 a 99
     * @param board
     * @return lista di coordinate in versione di indici (da 0 a 99)
     */
    private List<List<Integer>> convertShipsToIndices(Board board) {

        List<List<Integer>> allShips = new ArrayList<>();
        List<Ship> ships = board.getShips();

        for (Ship ship : ships) { //per ogni barca di board
            List<Integer> singleShip = new ArrayList<>();

            for (Node node : ship.getNodes()) {
                int x = node.getPos().getPosx();
                int y = node.getPos().getPosy();
                int index = y * 10 + x;

                singleShip.add(index);
            }

            allShips.add(singleShip);
        }

        return allShips;
    }

    /**
     * Gestisce l'attaco del computer e del giocatore registrando i vari dati relativi in response (una mappa <String, Object>)
     * @param index indice della cella attaccata dal player
     * @return un mappa con: (attacco giocatore) hit e sunk, (attacco computer) computerHit, computerSunk, playerHitIndex, (game over) gameOver [true o false] e winner
     */
    @PutMapping("/attacca/{index}")
    public Map<String, Object> attacca(@PathVariable int index) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Converti indice in coordinate
            int x = index % 10;
            int y = index / 10;

            // TURNO GIOCATORE
            int result = game.playerTurn(new Coordinates(x, y));
            response.put("hit", result > 0);
            response.put("sunk", result == 2);

            // Controlla se il giocatore ha vinto
            if (game.getAiBoard().hasLost()) {
                response.put("gameOver", true);
                response.put("winner", "player");
                return response;
            }

            // TURNO COMPUTER
            Map<String, Integer> computerResponse = game.aiTurn();
            int computerResult = computerResponse.get("result");
            int attackedIndex = computerResponse.get("coordY")*10 + computerResponse.get("coordX");

            response.put("computerHit", computerResult > 0);
            response.put("computerSunk", computerResult == 2);
            response.put("playerHitIndex", attackedIndex);

            // Controlla se il computer ha vinto
            if (game.getPlayerBoard().hasLost()) {
                response.put("gameOver", true);
                response.put("winner", "computer");
            }

            return response;

        } catch (AlreadyHittenException | InvalidPlacementException e) {
            response.put("error", e.getMessage());
            return response;
        }
    }

    /**
     * Resetta il gioco creando una nuova instanza di BattleshipGame
     */
    @PostMapping("/reset")
    public void resetGame() {
        game = new BattleshipsGame();
    }
}