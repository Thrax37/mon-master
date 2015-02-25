package controller;
public enum Species{
	
	KUTKU("Yian Kut-ku", 5, 5, 1500),
	RATHIAN("Rathian", 7, 4, 1800),
	RATAHALOS("Rathalos", 4, 7, 1800),
	TIGREX("Tigrex", 8, 6, 2000),
	FATALIS("Fatalis", 11, 7, 2500);
	
	private Species(String name, int atk, int def, int hp) {
		this.name = name;
		this.atk = atk;
		this.def = def;
		this.hp = hp;
	}
	private final String name;
	private final int atk;
	private final int def;
	private final int hp;
	
	public String getName() {
		return name;
	}
	public int getAtk() {
		return atk;
	}
	public int getDef() {
		return def;
	}
	public int getHp() {
		return hp;
	}
	
	
}
