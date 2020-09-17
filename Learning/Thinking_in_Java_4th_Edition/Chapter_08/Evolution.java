/**
 * @author banbao
 */

import static java.lang.System.out;

class Warrior{
    public void attack(){
        out.println("Attacking prototype!");
    }
}

class FalcoKnight extends Warrior{
    @Override
    public void attack(){
        out.println("FalcoKnight attacking!");
    }
}

class PegKnight extends Warrior{
    @Override
    public void attack(){
        out.println("PegKnight attacking!");
    }
}

class Fighter{
    private Warrior warrior = new PegKnight();
    public void evolution(){
        this.warrior = new FalcoKnight();
    }
    public void attack(){
        this.warrior.attack();
    }
}

public class Evolution{
    public static void main(String...args){
        Fighter fighter = new Fighter();
        fighter.attack();
        fighter.evolution();
        fighter.attack();
    }
}

/*
 * output:
 *
 * PegKnight attacking!
 * FalcoKnight attacking! 
 */
