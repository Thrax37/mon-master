package controller;

public class Monster extends Player {

	private Species species;
	private int atk;
	private int def;
	private int hp;
	private int target;
	
	private String nextMove;

	private boolean isFlying = false;
	private boolean isDodging = false;
	private boolean isDead = false;
	
	public Monster() {
		super();
		setId(-1);
	}
	
	@Override
	public String getCmd() {
		return "";
	}

	public Species getSpecies() {
		return species;
	}

	public void setSpecies(Species species) {
		this.species = species;
		
		setAtk(species.getAtk());
		setDef(species.getDef());
		setHp(species.getHp());
		
		setNextMove("O"); //Observe hunters
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

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public boolean isDodging() {
		return isDodging;
	}

	public void setDodging(boolean isDodging) {
		this.isDodging = isDodging;
	}

	public boolean isFlying() {
		return isFlying;
	}

	public void setFlying(boolean isFlying) {
		this.isFlying = isFlying;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public String getNextMove() {
		return nextMove;
	}

	public void setNextMove(String nextMove) {
		this.nextMove = nextMove;
	}
	
	
}
