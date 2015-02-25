package controller;
public enum Item{
	
	POTION("P", "Potion"),
	RATION("R", "Ration"),
	WHETSTONE("W", "Whetstone");
	
	Item(final String id, final String name) {
		this.id = id;
		this.name = name;
	}
	
	private final String id;
	private final String name;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
}
