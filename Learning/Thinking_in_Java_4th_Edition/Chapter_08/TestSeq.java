/**
 * @author banbao
 */

import static java.lang.System.out;

class Warrior{
    Warrior(){
        out.println("Warrior!");
    }
}

class Knight extends Warrior{
    Knight(){
        out.println("Knight!");
    }
}

class PegKnight extends Knight{
    PegKnight(){
        out.println("PegKnight!");
    }
}

class Dancer{
    Dancer(){
        out.println("Dancer!");
    }
}

class Troop{
    public Dancer dance1 = new Dancer();
    public PegKnight pegKnight1 = new PegKnight();
    Troop(){
        out.println("Troop!");
    }
}

public class TestSeq{
    public static void main(String...args){
        new Troop();
    }
}

/*
 * output:
 *
 * Dancer!
 * Warrior!
 * Knight!
 * PegKnight!
 * Troop!
 * 
 */
