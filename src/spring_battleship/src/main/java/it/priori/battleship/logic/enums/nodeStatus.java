package it.priori.battleship.logic.enums;

public enum nodeStatus {
	WATER(false),
	WATER_HITTEN(true),
	SHIP_HITTEN(true),
	SHIP(false);

	public boolean hitten;

	private nodeStatus(boolean hit) {
		this.hitten = hit;
	}
}
