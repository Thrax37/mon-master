package controller;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Hunter {
	private final int id;
	private Player owner;
	
	private Weapon weapon;
	
	private int atk;
	private int def;
	private int hp;
	private int energy;
	private int guard;
	private int speed;
	private int aggro;
	private int sharpness;
	
	private LinkedList<Attack> attacks = new LinkedList<>();
	
	private boolean isDodging = false;
	private boolean isGuarding = false;
	private boolean isUsing = false;
	private boolean isAttacking = false;
	private boolean isStunned = false;
	private boolean isDead = false;
	
	private List<Item> inventory;

	public Hunter(int id, Player owner, Weapon weapon, int atk, int def,
			int hp, int energy, int guard, int speed, int aggro, int sharpness) {
		super();
		this.id = id;
		this.owner = owner;
		this.weapon = weapon;
		this.atk = atk;
		this.def = def;
		this.hp = hp;
		this.energy = energy;
		this.guard = guard;
		this.speed = speed;
		this.aggro = aggro;
		this.sharpness = sharpness;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getGuard() {
		return guard;
	}

	public void setGuard(int guard) {
		this.guard = guard;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getAggro() {
		return aggro;
	}

	public void setAggro(int aggro) {
		this.aggro = aggro;
	}

	public int getSharpness() {
		return sharpness;
	}

	public void setSharpness(int sharpness) {
		this.sharpness = sharpness;
	}

	public int getId() {
		return id;
	}
	
	public boolean isDodging() {
		return isDodging;
	}

	public void setDodging(boolean isDodging) {
		this.isDodging = isDodging;
	}

	public boolean isGuarding() {
		return isGuarding;
	}

	public void setGuarding(boolean isGuarding) {
		this.isGuarding = isGuarding;
	}

	public boolean isUsing() {
		return isUsing;
	}

	public void setUsing(boolean isUsing) {
		this.isUsing = isUsing;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

	public void setAttacking(boolean isAttacking) {
		this.isAttacking = isAttacking;
	}

	public boolean isStunned() {
		return isStunned;
	}

	public void setStunned(boolean isStunned) {
		this.isStunned = isStunned;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public List<Item> getInventory() {
		return inventory;
	}

	public void setInventory(List<Item> inventory) {
		this.inventory = inventory;
	}
	

	public LinkedList<Attack> getAttacks() {
		return attacks;
	}

	public void setAttacks(LinkedList<Attack> attacks) {
		this.attacks = attacks;
	}

	public String getCommand(String args) throws Exception {
		//neutral player
		if ("".equals(owner.getCmd())) {
			return "W";
		}
		Process proc = null;
		Scanner stdin = null;
		try {
			proc = Runtime.getRuntime().exec(owner.getCmd() + " " + args);
			stdin = new Scanner(proc.getInputStream());
			StringBuilder response = new StringBuilder();
			while (stdin.hasNext()) {
				response.append(stdin.next()).append(' ');
			}
			return response.toString();	
		} finally {
			if (stdin != null) stdin.close();
			if (proc != null) proc.destroy();
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other != null && other instanceof Hunter) {
			return getId() == ((Hunter) other).getId();
		}
		return false;
	}
	
	@Override
	public String toString() {
		//return "Id: " + id + " Owner: " + owner.getDisplayName();
		return owner.getDisplayName();
	}
	
}
