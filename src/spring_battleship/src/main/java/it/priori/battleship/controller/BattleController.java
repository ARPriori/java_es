package it.priori.battleship.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.priori.battleship.pijo.Field;

@RestController
public class BattleController {

	@GetMapping("/BattleShip")
	public Field index() {
		return new Field("A01");
	}

	@PostMapping
	public Field updateField(@RequestBody Field field) {
		field.setPlayerName("Z99");
		return field;
	}

}