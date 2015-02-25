package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Weapon{
	
	GS("GS", "Great Sword", 7, 2, 2, new ArrayList<Combo>(Arrays.asList(Combo.GSCombo1, Combo.GSCombo2))),
	LA("LA", "Lance", 5, 4, 3, new ArrayList<Combo>(Arrays.asList(Combo.LACombo1, Combo.LACombo2, Combo.LACombo3))),
	DB("DB", "Dual Blade", 3, 0, 7, new ArrayList<Combo>(Arrays.asList(Combo.DBCombo1, Combo.DBCombo2, Combo.DBCombo3, Combo.DBCombo4))),
	HA("HA", "Hammer", 9, 0, 1, new ArrayList<Combo>(Arrays.asList(Combo.HACombo1, Combo.HACombo2)));
	
	Weapon(final String id, final String name, final int atk, final int guard, final int speed, final List<Combo> combos) {
		this.id = id;
		this.name = name;
		this.atk = atk;
		this.guard = guard;
		this.speed = speed;
		this.combos = combos;
	}
	
	private final String id;
	private final String name;
	private final int atk;
	private final int guard;
	private final int speed;
	private final List<Combo> combos;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getAtk() {
		return atk;
	}
	public int getGuard() {
		return guard;
	}
	public int getSpeed() {
		return speed;
	}
	public List<Combo> getCombos() {
		return combos;
	}
	
	
	
}
