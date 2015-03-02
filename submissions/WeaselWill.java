public class WeaselWill {
    int id, round, atk, def, hp,
        energy, guard, speed,
        sharp, aggro, potions,
        rations, whets;
    String combo;
    Monster monster;
    int[] aggros;

    void hunt(){
        if(willKillMe(false))
            dontDie();
        if(energy < 11){
            if(aggroDiff() > 300)
                output("T");
            output("W");
        }
        if(willHitMe()&&energy>29)
            output("D");
        int potDamage = attack(false);
        if(potDamage>monster.hp/2)
            attack(true);
        if(aggroDiff()*2>potDamage || round==1)
            attack(true);
        if(sharp==1&&whets>0&&!willKillMe(true))
            output("S");
        output("W");
    }

    int aggroDiff(){
        int highest = 0;
        for(int agg : aggros)
            highest = agg>highest?agg:highest;
        return highest - aggro;
    }

    int attack(boolean output){
        if(output)
            output("C");
        return willHitFor(combo.length()+1);
    }

    void dontDie(){
        if(energy>=30)
            output("D");
        int dmg = hitsMeFor(true);
        if(hp-dmg>-30 && rations>0)
            output("R");
        if(potions>0)
            output("P");
        if(energy>10)
            attack(true);
        output("W");
    }

    boolean willKillMe(boolean item){return willHitMe()&&(hitsMeFor(item)>=hp);}
    boolean willHitMe(){
        if(monster.move.equals("S"))
            return true;
        else if((monster.move.equals("A")||monster.move.equals("C"))&&monster.target==id)
            return true;
        return false;
    }

    int hitsMeFor(boolean item){
        return monster.atk*(10-def+(item?2:0));
    }

    int willHitFor(int combo){
        return atk*(10+sharp-monster.def)*combo;
    }

    static int atoi(String in){
        return Integer.parseInt(in);
    }

    void output(String out){
        System.out.println(out);
        System.exit(0);
    }

    public static void main(String[] args){
        if(args.length==0){
            System.out.println("DB");
        } else {
            new WeaselWill(args[0]).hunt();
        }
    }

    WeaselWill(String input){
        String[] tokens = input.split(";");
        round = atoi(tokens[0]);
        id = atoi(tokens[1]);
        monster = new Monster(new String[]{tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]});        
        aggros = new int[3];
        for(int i=7,j=0;i<tokens.length;i++){
            String[] in = tokens[i].split("_");
            if(atoi(in[0])==id){
                atk = atoi(in[2]);
                def = atoi(in[3]);
                hp = atoi(in[4]);
                energy = atoi(in[5]);
                guard = atoi(in[6]);
                speed = atoi(in[7]);
                sharp = atoi(in[8]);
                aggro = atoi(in[9]);
                potions = atoi(in[10]);
                rations = atoi(in[11]);
                whets = atoi(in[12]);
                combo = in[13];                
            } else {
                aggros[j++] = atoi(in[9]);
            }
        }
    }

    class Monster{
        int atk, def, hp, target;
        String move;

        Monster(String[] in){
            atk = atoi(in[0]);
            def = atoi(in[1]);
            hp = atoi(in[2]);
            target = atoi(in[3]);
            move = in[4];
        }
    }
}