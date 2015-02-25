package controller;

import java.util.Arrays;
import java.util.LinkedList;

public enum Combo {

	
	GSCombo1(Weapon.GS, 3, new LinkedList<Attack>(Arrays.asList(Attack.B, Attack.B, Attack.A)), true),
	GSCombo2(Weapon.GS, 4, new LinkedList<Attack>(Arrays.asList(Attack.B, Attack.B, Attack.B, Attack.C)), true),
	
	LACombo1(Weapon.LA, 3, new LinkedList<Attack>(Arrays.asList(Attack.B, Attack.B, Attack.B)), true),
	LACombo2(Weapon.LA, 2, new LinkedList<Attack>(Arrays.asList(Attack.A, Attack.A, Attack.A)), false),
	LACombo3(Weapon.LA, 4, new LinkedList<Attack>(Arrays.asList(Attack.A, Attack.A, Attack.A, Attack.C)), true),
	
	DBCombo1(Weapon.DB, 2, new LinkedList<Attack>(Arrays.asList(Attack.C, Attack.C)), true),
	DBCombo2(Weapon.DB, 3, new LinkedList<Attack>(Arrays.asList(Attack.A, Attack.B, Attack.A, Attack.B)), false),
	DBCombo3(Weapon.DB, 4, new LinkedList<Attack>(Arrays.asList(Attack.A, Attack.B, Attack.A, Attack.B, Attack.A)), false),
	DBCombo4(Weapon.DB, 5, new LinkedList<Attack>(Arrays.asList(Attack.A, Attack.B, Attack.A, Attack.B, Attack.A, Attack.C)), true),
	
	HACombo1(Weapon.HA, 2, new LinkedList<Attack>(Arrays.asList(Attack.C, Attack.C, Attack.A)), true),
	HACombo2(Weapon.HA, 2, new LinkedList<Attack>(Arrays.asList(Attack.B, Attack.B, Attack.A)), true);
	
	private Combo(Weapon weapon, int strength, LinkedList<Attack> attacks, boolean reset) {
		this.weapon = weapon;
		this.strength = strength;
		this.attacks = attacks;
		this.reset = reset;
	}
	private final Weapon weapon;
	private final int strength;
	private final LinkedList<Attack> attacks;
	private final boolean reset;
	
	public Weapon getWeapon() {
		return weapon;
	}
	public int getStrength() {
		return strength;
	}
	public LinkedList<Attack> getAttacks() {
		return attacks;
	}
	public boolean isReset() {
		return reset;
	}
	
	
}
