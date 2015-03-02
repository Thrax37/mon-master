import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RedViper {
    private static final int ENERGY_PER_GUARD = 15;
    private static final int ENERGY_PER_HIT = 20;
    private static final String FILE_PATH = "RedViper.txt";

    int round;
    int playerID;
    Hunter me;
    List<Hunter> otherHunters = new ArrayList<>();;
    Monster monster;

    public static void main(String[] args){
        if (args.length == 0) {
            System.out.println("LA");
        } else {
            RedViper redViper = new RedViper(args);
            String output = redViper.hunt();
            redViper.updateDataFile();
            System.out.println(output);
        }
    }

    private void updateDataFile() {
        File f = new File(FILE_PATH);
        if (f.exists())
            f.delete();
        if(monster.nextMove.equals("F") || monster.nextMove.equals("C")) {
            try {
                f.createNewFile();
            } catch (IOException e) {}
        }
    }

    private String hunt() {
        //low hp
        if (me.hp <= monster.dmg(me, false, false)) {
            if (!monster.attacksMe()) {
                if (me.potions > 0)
                    return "P";
                else if (me.rations > 0)
                    return "R";
            } else {
                if (me.hp > monster.dmg(me, true, false) && me.energy > ENERGY_PER_GUARD)
                    return "G";
                else if (me.potions > 0)
                    return "P";
                else if (me.rations > 0 && me.hp + 30 > monster.dmg(me, false, true))
                    return "R";
                else
                    return "B";
            }
        }
        if (monster.attacksMe() && me.energy > ENERGY_PER_GUARD)
            return "G";
        if (isSafeToTaunt() && me.energy < 60)
            return "T";
        if (!monster.attacksMe() 
                && !(me.attacks.equals("BB") && me.energy >= ENERGY_PER_HIT && !monster.dodges()) 
                && !(me.attacks.equals("B") && me.energy >= ENERGY_PER_HIT*2) 
                && round < 48) {
            if (me.sharpness < 2 && me.whetstones > 0)
                return "S";
            if (me.energy <= 50 && me.hp <= 70 && me.rations > 0)
                return "R";
        }
        if ((monster.dodges() && !(me.attacks.equals("B") && me.energy >= ENERGY_PER_HIT*2) && me.energy < 90) || me.energy < ENERGY_PER_HIT)
            return "W";

        return "B";
    }

    private boolean isSafeToTaunt() {
        int highestAggro = Integer.MIN_VALUE;
        for (Hunter hunter : otherHunters) {
            if (hunter.aggro > highestAggro)
                highestAggro = hunter.aggro;
        }
        return highestAggro > me.aggro + 300;
    }

    public RedViper(String[] args) {
        List<Hunter> allHunters = new ArrayList<>();
        args = args[0].split(";");

        round = Integer.parseInt(args[0]);
        playerID = Integer.parseInt(args[1]);
        monster = new Monster(args[2]+";"+args[3]+";"+args[4]+";"+args[5]+";"+args[6]);

        for (int i = 7; i < args.length; i++){
            allHunters.add(new Hunter(args[i]));
        }

        for (Hunter hunter : allHunters) {
            if (hunter.isMe()){
                me = hunter;
            } else {
                otherHunters.add(hunter);
            }
        }
    }

    private class Monster {

        int atk;
        int targetId;
        String nextMove;

        public Monster(String string) {
            String[] args = string.split(";");
            atk = Integer.parseInt(args[0]);
            targetId = Integer.parseInt(args[3]);
            nextMove = args[4];
        }

        public boolean attacksMe() {
            return nextMove.equals("S") 
                    || (nextMove.equals("C") && targetId == playerID)
                    || (nextMove.equals("A") && targetId == playerID);
        }

        public int dmg(Hunter hunter, boolean guard, boolean using) {
            int damageMultiplicator = 10 - hunter.def;
            if (guard) { damageMultiplicator -= hunter.guard; }
            if (using) { damageMultiplicator += 2; }

            int damage = monster.atk * damageMultiplicator;
            return damage;
        }

        public boolean dodges() {
            File f = new File(FILE_PATH);
            return f.exists();
        }
    }

    private class Hunter {
        int id;
        int def;
        int hp;
        int energy;
        int guard;
        int sharpness;
        int aggro;
        int potions;
        int rations;
        int whetstones;
        String attacks;

        public Hunter(String string) {
            String[] args = string.split("_");
            id = Integer.parseInt(args[0]);
            def = Integer.parseInt(args[3]);
            hp = Integer.parseInt(args[4]);
            energy = Integer.parseInt(args[5]);
            guard = Integer.parseInt(args[6]);
            sharpness = Integer.parseInt(args[8]);
            aggro = Integer.parseInt(args[9]);
            potions = Integer.parseInt(args[10]);
            rations = Integer.parseInt(args[11]);
            whetstones = Integer.parseInt(args[12]);
            attacks = args[13];
        }

        public boolean isMe() {
            return id == playerID;
        }
    }
}