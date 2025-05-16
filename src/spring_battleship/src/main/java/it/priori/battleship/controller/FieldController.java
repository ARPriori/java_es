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
import java.util.stream.Collectors;

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

    @GetMapping("/randomizza-player")
    public Map<String, Object> randomizzaPlayer() {
        Map<String, Object> response = new HashMap<>();

        // Posiziona navi random per giocatore
        Board playerBoard = game.getPlayerBoard();
        playerBoard.randomizeShips();

        response.put("playerShips", convertShipsToIndices(playerBoard));

        return response;
    }

    private List<List<Integer>> convertShipsToIndices(Board board) {

        List<List<Integer>> allShips = new ArrayList<>();
        List<Ship> ships = board.getShips();

        for (Ship ship : ships) {
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

    // Endpoint per l'attacco
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
            int computerResult = game.aiTurn();
            response.put("computerHit", computerResult > 0);
            response.put("computerSunk", computerResult == 2);

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

    @PostMapping("/reset")
    public Map<String, Object> resetGame() {
        game = new BattleshipsGame();
        return Collections.singletonMap("success", true);
    }

    @GetMapping("/stato-gioco")
    public Map<String, Object> getGameState() {
        Map<String, Object> state = new HashMap<>();

        // Stato griglia giocatore
        state.put("playerBoard", convertBoardToMap(game.getPlayerBoard()));

        // Stato griglia computer
        state.put("computerBoard", convertBoardToMap(game.getAiBoard()));

        // Stato del gioco
        state.put("gameOver", game.getPlayerBoard().hasLost() || game.getAiBoard().hasLost());
        if (game.getAiBoard().hasLost()) {
            state.put("winner", "player");
        } else if (game.getPlayerBoard().hasLost()) {
            state.put("winner", "computer");
        }

        return state;
    }

    private Map<String, Object> convertBoardToMap(Board board) {
        Map<String, Object> boardMap = new HashMap<>();

        // Celle con navi
        List<Integer> shipCells = board.getShips().stream()
                .flatMap(ship -> ship.getNodes().stream())
                .map(node -> node.getPos().getPosy() * 10 + node.getPos().getPosx())
                .collect(Collectors.toList());

        // Celle colpite
        List<Integer> hitCells = board.getHittenNodes().stream()
                .map(node -> node.getPos().getPosy() * 10 + node.getPos().getPosx())
                .collect(Collectors.toList());

        boardMap.put("shipCells", shipCells);
        boardMap.put("hitCells", hitCells);
        boardMap.put("hidden", board.isHidden());
        boardMap.put("lost", board.hasLost());

        return boardMap;
    }
}