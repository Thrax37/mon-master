import java.util.ArrayList;
import java.util.List;


public class RoranStronghammer {

    int round;
    int playerID;
    Hunter thisHunter;
    List<Hunter> hunters = new ArrayList<>();
    List<Hunter> otherHunters = new ArrayList<>();
    Monster monster;
    boolean canTauntSafely;

    public static void main(String[] args){
        if (args.length == 0) {
            System.out.println("HA");//ha!
        } else {
            System.out.println(new RoranStronghammer(args).hunt());
        }
    }

    private String hunt() {

        int requiredEnergy = 100 / (thisHunter.getSpeed() + 2);

        if (requiredEnergy > thisHunter.getEnergy()){
            if (canTauntSafely){
                return "T";
            }
            if (thisHunter.getRations() > 0){
                return "R";
            }
            return "T";
        }


        int damage = thisHunter.getAtk() * (10 + thisHunter.getSharpness() - monster.getDef());
        int monsterDamage = monster.getAtk() * (10 - thisHunter.getDef());

        String currentCombo = thisHunter.getAttacks();

        if (damage > monster.getHp()){//Kill monster if possible.
            return "B";
        }

        if (currentCombo.length() >= 2 && damage * 2 > monster.getHp()){//Kill monster if possible.
            return "A";
        }

        if (monsterDamage > thisHunter.getHp()){
            if (thisHunter.getPotions() > 0 && thisHunter.getHp() <= 10){
                return "P";
            }
            if (thisHunter.getEnergy() <= 50 && thisHunter.getRations() > 0){
                return "R";
            }
            if (thisHunter.getPotions() > 0){
                return "P";
            }
        }

        if (currentCombo.length() >= 2){
            return "A";                    //Do critical hit
        }

        if (currentCombo.length() == 0){
            if (thisHunter.getEnergy() < requiredEnergy * 3) {
                if (canTauntSafely){
                    return "T";
                }
                if (thisHunter.getRations() > 0) {
                    return "R";
                }
                return "T";
            }
            if (thisHunter.getSharpness() < 4){
                return "S";
            }
        }
        if (currentCombo.length() == 1){
            if (thisHunter.getEnergy() < requiredEnergy * 2) {
                if (canTauntSafely){
                    return "T";
                }
                if (thisHunter.getRations() > 0) {
                    return "R";
                }
                return "T";
            }
            if (thisHunter.getSharpness() < 3){
                return "S";
            }
        }
        return "B";                   //Attack constantly.

    }

    public RoranStronghammer(String[] args) {

        args = args[0].split(";");

        round = Integer.parseInt(args[0]);
        playerID = Integer.parseInt(args[1]);
        monster = new Monster(args[2]+";"+args[3]+";"+args[4]+";"+args[5]+";"+args[6]);


        for (int i = 7; i < args.length; i++){
            hunters.add(new Hunter(args[i]));
        }

        int mostAggressiveness = 0;
        int myAggressiveness = 0;

        for (Hunter hunter : hunters) {
            if (hunter.isMe()){
                thisHunter = hunter;
                myAggressiveness = hunter.getAggro();
            } else {
                otherHunters.add(hunter);
                mostAggressiveness = Math.max(mostAggressiveness, hunter.getAggro());
            }
        }

        canTauntSafely = myAggressiveness + 300 < mostAggressiveness;
    }

    private class Monster {

        int atk;
        int def;
        int hp;
        int targetId;
        String nextMove;

        public Monster(String string) {
            String[] args = string.split(";");
            atk = Integer.parseInt(args[0]);
            def = Integer.parseInt(args[1]);
            hp = Integer.parseInt(args[2]);
            targetId = Integer.parseInt(args[3]);
            nextMove = args[4];
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

        public int getTargetId() {
            return targetId;
        }

        public void setTargetId(int targetId) {
            this.targetId = targetId;
        }

        public String getNextMove() {
            return nextMove;
        }

        public void setNextMove(String nextMove) {
            this.nextMove = nextMove;
        }
    }

    private class Hunter {
        int hunterID;
        String weapon;
        int atk;
        int def;
        int hp;
        int energy;
        int guard;
        int speed;
        int sharpness;
        int aggro;
        int potions;
        int rations;
        int whetstones;
        String attacks;

        public Hunter(String string) {
            String[] args = string.split("_");
            hunterID = Integer.parseInt(args[0]);
            weapon = args[1];
            atk = Integer.parseInt(args[2]);
            def = Integer.parseInt(args[3]);
            hp = Integer.parseInt(args[4]);
            energy = Integer.parseInt(args[5]);
            guard = Integer.parseInt(args[6]);
            speed = Integer.parseInt(args[7]);
            sharpness = Integer.parseInt(args[8]);
            aggro = Integer.parseInt(args[9]);
            potions = Integer.parseInt(args[10]);
            rations = Integer.parseInt(args[11]);
            whetstones = Integer.parseInt(args[12]);
            attacks = args[13];
        }

        public int getHunterID() {
            return hunterID;
        }

        public void setHunterID(int hunterID) {
            this.hunterID = hunterID;
        }

        public String getWeapon() {
            return weapon;
        }

        public void setWeapon(String weapon) {
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

        public int getSharpness() {
            return sharpness;
        }

        public void setSharpness(int sharpness) {
            this.sharpness = sharpness;
        }

        public int getAggro() {
            return aggro;
        }

        public void setAggro(int aggro) {
            this.aggro = aggro;
        }

        public int getPotions() {
            return potions;
        }

        public void setPotions(int potions) {
            this.potions = potions;
        }

        public int getRations() {
            return rations;
        }

        public void setRations(int rations) {
            this.rations = rations;
        }

        public int getWhetstones() {
            return whetstones;
        }

        public void setWhetstones(int whetstones) {
            this.whetstones = whetstones;
        }

        public String getAttacks() {
            return attacks;
        }

        public void setAttacks(String attacks) {
            this.attacks = attacks;
        }

        public boolean isMe() {
            return getHunterID() == playerID;
        }
    }
}