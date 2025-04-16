package it.priori.battleship.pijo;

public class Field {
    private String playerName;

    public Field() {
    }

    public Field(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
