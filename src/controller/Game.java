package controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import players.Felyne;
import players.Koneko;
import players.Melynx;
import players.Palico;

public class Game {
	private static Player[] players = {
			new Helper(),
			new Helper(),
			new Helper(),
			new Helper()
	};
	
	private static List<Player> helpers = new ArrayList<>(Arrays.asList(
			new Felyne(),
			new Melynx(),
			new Palico(),
			new Koneko()
	));
	
	// Game Parameters
	private static final int ROUNDS = 50;
	private static final int HUNTERS_PER_HUNT = 4;
	
	// Console
	private static final boolean DEBUG = false;
	private static final boolean GAME_MESSAGES = true;
	
	// Hunter
	private static final int HUNTER_BASE_DEF = 5;
	private static final int HUNTER_BASE_HP = 100;
	private static final int HUNTER_BASE_ENERGY = 100;
	private static final int HUNTER_BASE_AGGRO = 0;
	private static final int HUNTER_BASE_SHARPNESS = 4;
	private static final int HUNTER_STARTING_POTIONS = 5;
	private static final int HUNTER_STARTING_RATIONS = 5;
	private static final int HUNTER_STARTING_WHETSTONES = 5;
	
	// Items and actions
	private static final int ENERGY_PER_WAIT = 30;
	private static final int ENERGY_PER_TAUNT = 80;
	private static final int ENERGY_PER_RATION = 50;
	private static final int ENERGY_PER_GUARD = 15;
	private static final int ENERGY_PER_DODGE = 30;
	private static final int HEALTH_PER_POTION = 90;
	private static final int HEALTH_PER_RATION = 30;
	private static final int AGGRO_PER_TAUNT = 300;
	private static final int AGGRO_PER_TURN = 30;
	private static final int SHARPNESS_PER_HIT = 1;
	private static final int ITEM_DEF_DEBUFF = 2;
	
	// Monster moves
	private static final int MONSTER_MOVE_OBSERVE = 5;
	private static final int MONSTER_MOVE_ATTACK = 40;
	private static final int MONSTER_MOVE_ROAR = 10;
	private static final int MONSTER_MOVE_FLY = 10;
	private static final int MONSTER_MOVE_CHARGE = 15;
	private static final int MONSTER_MOVE_SPIN = 15;
	
	// Scores
	private static final int SCORE_HUNT = 1000;
	private static final int SCORE_DAMAGE = 1;
	
	private final List<Hunter> hunters = new ArrayList<Hunter>();
	private int round = 0;
	private Monster monster = new Monster();
	private List<String> monsterMoves = new ArrayList<String>();
	
	public Game() {
		for (int i = 0; i < players.length; i++) {
			players[i].setId(i);
		}
		for (int i = 0; i < MONSTER_MOVE_OBSERVE; i++) 	{ monsterMoves.add("O"); }
		for (int i = 0; i < MONSTER_MOVE_ATTACK; i++) 	{ monsterMoves.add("A"); }
		for (int i = 0; i < MONSTER_MOVE_ROAR; i++) 	{ monsterMoves.add("R"); }
		for (int i = 0; i < MONSTER_MOVE_FLY; i++) 		{ monsterMoves.add("F"); }
		for (int i = 0; i < MONSTER_MOVE_CHARGE; i++) 	{ monsterMoves.add("C"); }
		for (int i = 0; i < MONSTER_MOVE_SPIN; i++) 	{ monsterMoves.add("S"); }
	}
	
	public static void main(String... args) {
		
		// Adding random helpers to make teams
		Random r = new Random();
		for (int i = 0; i < players.length; i++) { 
			if (players[i].getClass().equals(Helper.class)) {
				int p = r.nextInt(helpers.size());
				players[i] = helpers.get(p);
				helpers.remove(p);
			}
		}
		
		// Starting
		new Game().run();
	}	
	public void run() {
			
		if (GAME_MESSAGES) 
			System.out.println("Starting a new game...");
		
		this.initialize();
		
		if (GAME_MESSAGES) 
			System.out.println("Game begins.");
		
		for (Species species : Species.values()) {
			List<List<Hunter>> teams = makeTeams(hunters);
			
			for (List<Hunter> team : teams) {
				if (GAME_MESSAGES) {
					System.out.println();
					System.out.println("****** HUNT BEGINS : " + species.getName() + " ******");
					System.out.println("* HUNTERS : " + team.toString());
					System.out.println("******");
				}
				
				startHunt(team);
				monster.setSpecies(species);
							
				for (round = 1; round <= ROUNDS; round++) {
					if (GAME_MESSAGES) {
						System.out.println("-- Time left : " + (51 - round) + " mins --");
					}
					if (!makeTurns(team)) break; //break if only no player left or monster dead
				}
			}
		}
		printResults();
	}
	
	private void initialize() {		
		
		for (int i = 0; i < players.length; i++) {
			try {
				String weapon = players[i].chooseWeapon();
				
				if (GAME_MESSAGES) System.out.println(players[i].getDisplayName() + " chose to wield a " + Weapon.valueOf(weapon).getName());

				if (existsWeapon(weapon)) {
					Hunter hunter = new Hunter(i, players[i], Weapon.valueOf(weapon), Weapon.valueOf(weapon).getAtk(), HUNTER_BASE_DEF, HUNTER_BASE_HP, HUNTER_BASE_ENERGY, Weapon.valueOf(weapon).getGuard(), Weapon.valueOf(weapon).getSpeed(), HUNTER_BASE_AGGRO, HUNTER_BASE_SHARPNESS);
					hunter.setInventory(initializeInventory());
					hunters.add(hunter);
				} else {
					throw new Exception("Invalid input : " + weapon);
				}
			} catch (Exception e) {
				if (DEBUG) {
					System.out.println("Exception in initialize() by " + players[i].getDisplayName());
					e.printStackTrace();
				}
			}
		}
		
		Collections.shuffle(hunters);
	}	
	
	private boolean makeTurns(List<Hunter> hunters) {
		
		for (Hunter hunter : hunters) {
			
			// Reset Buff/Debuff, Energy...
			startTurn(hunter);
			
			if (!hunter.isDead() && !hunter.isStunned()) {
				
				Player owner = hunter.getOwner();
				try {
					String request = round + ";" + owner.getId() + generateMonsterArgs() + generateHuntersArgs();
					String response = hunter.getCommand(request);
					if (DEBUG) {
						System.out.println("Request : " + request);
						System.out.println("Response : " + response);
					}
					switch (response.charAt(0)) {
						case 'A': executeAttack(new Command(response, hunter)); break;
						case 'B': executeAttack(new Command(response, hunter)); break;
						case 'C': executeAttack(new Command(response, hunter)); break;
						case 'D': executeDodge(new Command(response, hunter)); break;
						case 'G': executeGuard(new Command(response, hunter)); break;
						case 'W': executeWait(new Command(response, hunter)); break;
						case 'T': executeTaunt(new Command(response, hunter)); break;
						case 'P': executePotion(new Command(response, hunter)); break;
						case 'R': executeRation(new Command(response, hunter)); break;
						case 'S': executeSharpen(new Command(response, hunter)); break;
						default: executeWait(new Command(response, hunter)); break;
					}
				} catch (Exception e) {
					if (DEBUG) {
						System.out.println("Exception in makeTurns() by " + owner.getDisplayName());
						e.printStackTrace();
					}
				}
			}
			
			if (gameOver(hunters))
				return false;
		}
		
		decideMonster(hunters);
		
		if (gameOver(hunters))
			return false;

		return true;	
	}
	
	private boolean gameOver(List<Hunter> hunters) {

		if (allHuntersDead(hunters))  {
			if (GAME_MESSAGES) { System.out.println("All hunters were killed by the " + monster.getSpecies().getName() + ". The hunt is over."); }
			
			for (Hunter hunter : hunters) {
				hunter.getOwner().setScore(hunter.getOwner().getScore() - SCORE_HUNT);
			}
			
			return true;
		} else if (monsterDead()) {
			if (GAME_MESSAGES) { System.out.println("The hunt is over."); }
			
			for (Hunter hunter : hunters) {
				if (!hunter.isDead()) 
					hunter.getOwner().setScore(hunter.getOwner().getScore() + SCORE_HUNT);
				else
					hunter.getOwner().setScore(hunter.getOwner().getScore() - SCORE_HUNT);
			}
			
			return true;
		}
		return false;
	}
	
	private boolean allHuntersDead(List<Hunter> hunters) {
		return hunters.stream().allMatch(x -> x.isDead());
	}

	private boolean monsterDead() {
		return monster.isDead();
	}
	
	private int acquireTarget(List<Hunter> hunters) {
		return hunters.stream().filter(x -> !x.isDead()).max((x, y) -> x.getAggro() - y.getAggro()).get().getId(); 
	}
	
	private void decideMonster(List<Hunter> hunters) {
		int playerId = acquireTarget(hunters);
		monster.setTarget(playerId);
		
		if (monster.isFlying()) {
			monster.setFlying(false);
			if (GAME_MESSAGES) { System.out.println("The " + monster.getSpecies().getName() + " landed."); }
		}
		if (monster.isDodging()) {
			monster.setDodging(false);
		}
		for (Hunter hunter : hunters) {
			hunter.setStunned(false);
		}
		
		switch (monster.getNextMove()) {
			case "O" : monsterObserve(hunters); break; 		// Wait
			case "A" : monsterAttack(hunters); break; 		// Attack 1 hunter
			case "R" : monsterRoar(hunters); break;			// Roar stun
			case "F" : monsterFlyAway(hunters); break;		// Dodge next
			case "C" : monsterCharge(hunters); break;		// Attack 1 hunter + dodge next
			case "S" : monsterSpin(hunters); break;			// Attack all hunters
		}
		
		monster.setNextMove(monsterMoves.get(new Random().nextInt(monsterMoves.size())));
	}
	
	private void monsterObserve(List<Hunter> hunters) {
		
		if (GAME_MESSAGES) { System.out.println("The " + monster.getSpecies().getName() + " spotted the hunters."); }
	}

	private void monsterAttack(List<Hunter> hunters) {
		
		Hunter target = hunters.stream().filter(x -> x.getId() == monster.getTarget()).findFirst().get();
		
		if (!target.isDead()) {
			
			if (!target.isDodging()) {
		
				int damage = damageToHunter(target);
				
				target.setHp(target.getHp() - damage);
				
				if (GAME_MESSAGES) { System.out.println("The " + monster.getSpecies().getName() + " attacked " + target.getOwner().getDisplayName() + " for " + damage + "HP."); }
				
				if (target.getHp() <= 0) {
					target.setDead(true);
					
					if (GAME_MESSAGES) { System.out.println(target.getOwner().getDisplayName() + " was killed by the " + monster.getSpecies().getName() + "."); }
				} 
			} else {
				if (GAME_MESSAGES) { System.out.println(target.getOwner().getDisplayName() + " dodged."); }
			}
		}
	}
	
	private void monsterFlyAway(List<Hunter> hunters) {
		
		monster.setFlying(true);
		
		if (GAME_MESSAGES) { System.out.println("The " + monster.getSpecies().getName() + " flew away."); }
	}
	
	private void monsterRoar(List<Hunter> hunters) {
		
		for (Hunter hunter : hunters) {
			hunter.setStunned(true);
		}
		
		if (GAME_MESSAGES) { System.out.println("The " + monster.getSpecies().getName() + " roared and stunned everyone."); }
	}
	
	private void monsterCharge(List<Hunter> hunters) {
		
		monster.setDodging(true);
		
		if (GAME_MESSAGES) { System.out.println("The " + monster.getSpecies().getName() + " started to charge."); }
		
		monsterAttack(hunters);
	}
	
	private void monsterSpin(List<Hunter> hunters) {
		
		if (GAME_MESSAGES) { System.out.println("The " + monster.getSpecies().getName() + " started spinning."); }
		
		for (Hunter target : hunters) {
			
			if (!target.isDead()) {
				
				if (!target.isDodging()) {
					
					int damage = damageToHunter(target);
					
					target.setHp(target.getHp() - damage);
					
					if (GAME_MESSAGES) { System.out.println(target.getOwner().getDisplayName() + " was wounded by the " + monster.getSpecies().getName() + " for " + damage + "HP."); }
					
					if (target.getHp() <= 0) {
						target.setDead(true);
						
						if (GAME_MESSAGES) { System.out.println(target.getOwner().getDisplayName() + " was killed by the " + monster.getSpecies().getName() + "."); }
					} 
				} else {
					if (GAME_MESSAGES) { System.out.println(target.getOwner().getDisplayName() + " dodged."); }
				}
			}
		}
	}
	
	private int damageToHunter(Hunter hunter) {
		
		if (hunter.isDodging()) {
			return 0;
		} else {
			int damageMultiplicator = 10 - hunter.getDef();
			if (hunter.isGuarding()) { damageMultiplicator -= hunter.getGuard(); }
			if (hunter.isUsing()) { damageMultiplicator += ITEM_DEF_DEBUFF; }
			
			int damage = monster.getAtk() * damageMultiplicator;
			return damage;
		}
	}
	
	private int damageToMonster(Hunter hunter) {
		int comboMultiplicator = (hunter.getWeapon().getCombos().stream().anyMatch(x -> x.getAttacks().equals(hunter.getAttacks())) ? hunter.getWeapon().getCombos().stream().filter(x -> x.getAttacks().equals(hunter.getAttacks())).findFirst().get().getStrength() : 1);		
		int damageMultiplicator = 10 + hunter.getSharpness() - monster.getDef();
		int damage = hunter.getAtk() * damageMultiplicator * comboMultiplicator;
		
		
		return damage;
	}
	
	private void startHunt(List<Hunter> hunters) {
		for (Hunter hunter : hunters) {
			hunter.setDead(false);
			hunter.setGuarding(false);
			hunter.setDodging(false);
			hunter.setUsing(false);
			hunter.setAttacking(false);
			hunter.setStunned(false);
			hunter.setInventory(initializeInventory());
			hunter.setAggro(HUNTER_BASE_AGGRO);
			hunter.setHp(HUNTER_BASE_HP);
			hunter.setEnergy(HUNTER_BASE_ENERGY);
			hunter.setSharpness(HUNTER_BASE_SHARPNESS);
		}
		monster.setDead(false);
	}
	
	private void startTurn(Hunter hunter) {
		hunter.setAggro(Math.max(0, hunter.getAggro() - AGGRO_PER_TURN));
		hunter.setDodging(false);
		hunter.setGuarding(false);
		hunter.setUsing(false);
		
		if (!hunter.isAttacking()) {
			hunter.getAttacks().clear();
		}
	}

	private void executeAttack(Command support) {
		Hunter hunter = support.getSource();
		
		hunter.setAttacking(true);
		Attack attack = Attack.valueOf(String.valueOf(support.getCommand().charAt(0)));
		int energyNeeded = Math.floorDiv(100, hunter.getSpeed() + 2);
		
		if (hunter.getEnergy() >= energyNeeded) {
			
			hunter.getAttacks().add(attack);
			hunter.setEnergy(hunter.getEnergy() - energyNeeded);
			
			if (monster.isDodging() || monster.isFlying()) {
				if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " missed his attack"); }
			} else {
				int damage = damageToMonster(hunter);
				
				hunter.setSharpness(Math.max(1, hunter.getSharpness() - SHARPNESS_PER_HIT));
				hunter.setAggro(hunter.getAggro() + damage);
				monster.setHp(monster.getHp() - damage);
				hunter.getOwner().setScore(hunter.getOwner().getScore() + (damage * SCORE_DAMAGE));
				
				if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " wounded the " + monster.getSpecies().getName() + " for " + damage + "HP."); }
				
				if (monster.getHp() <= 0) {
					monster.setDead(true);
					
					if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " killed the " + monster.getSpecies().getName() + "."); }
				} 
			}
		} else {
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " couldn't muster up the energy to attack."); } 
			
		} 
	}

	private void executeDodge(Command support) {
		Hunter hunter = support.getSource();
		
		hunter.setAttacking(false);
		
		if (hunter.getEnergy() >= ENERGY_PER_DODGE) {
			hunter.setDodging(true);
			hunter.setEnergy(hunter.getEnergy() - ENERGY_PER_DODGE);
			
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " rolled out of reach."); } 
		} else {
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " was too tired to move."); } 
			
		} 
	}

	private void executeGuard(Command support) {
		Hunter hunter = support.getSource();
		
		hunter.setAttacking(false);
		
		if (hunter.getEnergy() >= ENERGY_PER_GUARD) {
			hunter.setGuarding(true);
			hunter.setEnergy(hunter.getEnergy() - ENERGY_PER_GUARD);
			
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " put up his guard."); } 
		} else {
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " hadn't enough strength to lift his weapon."); } 
			
		}
	}

	private void executeTaunt(Command support) {
		Hunter hunter = support.getSource();
		
		hunter.setAttacking(false);
		
		hunter.setAggro(hunter.getAggro() + AGGRO_PER_TAUNT);
		hunter.setEnergy(Math.max(0, Math.min(HUNTER_BASE_ENERGY, hunter.getEnergy() + ENERGY_PER_TAUNT)));
		
		if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " started dancing and throwing a rock at the " + monster.getSpecies().getName() + " to get its attention."); } 
	}
	
	private void executeWait(Command support) {
		Hunter hunter = support.getSource();
		
		hunter.setAttacking(false);
		
		hunter.setEnergy(Math.max(0, Math.min(HUNTER_BASE_ENERGY, hunter.getEnergy() + ENERGY_PER_WAIT)));
		
		if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " waited for a better opportunity."); } 
	}

	private void executePotion(Command support) {
		Hunter hunter = support.getSource();
		
		hunter.setAttacking(false);
		
		if (hunter.getInventory().contains(Item.POTION)) {
			
			hunter.setUsing(true);
			hunter.getInventory().remove(Item.POTION);
			hunter.setHp(Math.max(0, Math.min(HUNTER_BASE_HP, hunter.getHp() + HEALTH_PER_POTION)));
			
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " drank a potion."); }
		} else {
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " remained thirsty.. and hurt."); }
		}
	}

	private void executeRation(Command support) {
		Hunter hunter = support.getSource();
		
		hunter.setAttacking(false);
		
		if (hunter.getInventory().contains(Item.RATION)) {
			
			hunter.setUsing(true);
			hunter.getInventory().remove(Item.RATION);
			hunter.setHp(Math.max(0, Math.min(HUNTER_BASE_HP, hunter.getHp() + HEALTH_PER_RATION)));
			hunter.setEnergy(Math.max(0, Math.min(HUNTER_BASE_ENERGY, hunter.getEnergy() + ENERGY_PER_RATION)));
			
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " ate a ration. Yummy!"); }
		} else {
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " was hungry but had nothing to eat."); }
		}
	}

	private void executeSharpen(Command support) {
		Hunter hunter = support.getSource();
		
		hunter.setAttacking(false);
		
		if (hunter.getInventory().contains(Item.WHETSTONE)) {
			
			hunter.setUsing(true);
			hunter.getInventory().remove(Item.WHETSTONE);
			hunter.setSharpness(HUNTER_BASE_SHARPNESS);
			
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " sharpened his weapon."); }
		} else {
			if (GAME_MESSAGES) { System.out.println(hunter.getOwner().getDisplayName() + " found out his weapon is condemned to stay blunt."); }
		}
	}
	
	private void printResults() {
		
		List<Score> scores = new ArrayList<Score>();
		
		System.out.println("********** FINISH **********");
		
		
		for (Player player : players) {
			scores.add(new Score(player, player.getScore()));
		}
		
		//sort descending
		Collections.sort(scores, Collections.reverseOrder());
		
		for (int i = 0; i < scores.size(); i++) {
			Score score = scores.get(i);
			System.out.println(i+1 + ". " + score.print());
		}
		
	}
	
	private String generateMonsterArgs() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(';');
		builder.append(monster.getAtk()).append(';');
		builder.append(monster.getDef()).append(';');
		builder.append(monster.getHp()).append(';');
		builder.append(monster.getTarget()).append(';');
		builder.append(monster.getNextMove());
	
		return builder.toString();
	}

	private String generateHuntersArgs() {
		
		StringBuilder builder = new StringBuilder();
		//PlayerId WeaponId Atk Def HP Energy Guard Speed Sharpness Aggro Potions Rations Whetstones
		for (Hunter hunter : hunters) {
			if (!hunter.isDead()) {
				builder.append(';');
				builder.append(hunter.getOwner().getId()).append('_');
				builder.append(hunter.getWeapon().getId()).append('_');
				builder.append(hunter.getAtk()).append('_');
				builder.append(hunter.getDef()).append('_');
				builder.append(hunter.getHp()).append('_');
				builder.append(hunter.getEnergy()).append('_');
				builder.append(hunter.getGuard()).append('_');
				builder.append(hunter.getSpeed()).append('_');
				builder.append(hunter.getSharpness()).append('_');
				builder.append(hunter.getAggro()).append('_');
				builder.append(this.generateInventoryArgs(hunter)).append('_');
				builder.append(this.generateComboArgs(hunter));
			}
		}
		return builder.toString();
	}
	
	private String generateInventoryArgs(Hunter hunter) {
		
		StringBuilder builder = new StringBuilder();
		int potions = 0;
		int rations = 0;
		int whetstones = 0;
		for (Item item : hunter.getInventory()) {
			switch (item) {
				case POTION: potions++; break;
				case RATION: rations++; break;
				case WHETSTONE: whetstones++; break;
				default: break;
			}
		}
		builder.append(potions).append("_");
		builder.append(rations).append("_");
		builder.append(whetstones);
		return builder.toString();
	}
	
	private String generateComboArgs(Hunter hunter) {
		StringBuilder builder = new StringBuilder();
		if (hunter.getAttacks().isEmpty()) {
			builder.append("N");
		} else {
			for (Attack attack : hunter.getAttacks()) {
				builder.append(attack.name());
			}
		}
		return builder.toString();
	}
	
	public List<Item> initializeInventory() {
		List<Item> inventory = new ArrayList<Item>();
		for (int i = 0; i < HUNTER_STARTING_POTIONS; i++) { inventory.add(Item.POTION); }
		for (int i = 0; i < HUNTER_STARTING_RATIONS; i++) { inventory.add(Item.RATION); }
		for (int i = 0; i < HUNTER_STARTING_WHETSTONES; i++) { inventory.add(Item.WHETSTONE); }
		return inventory;
	}
	
	public static boolean existsWeapon(String value) {
	    for (Weapon w : Weapon.values()) {
	        if (w.name().equals(value)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public List<List<Hunter>> makeTeams(List<Hunter> hunters) {
		return new Utils<Hunter>().distribute(new ArrayList<Hunter>(hunters), HUNTERS_PER_HUNT);
	}
}

