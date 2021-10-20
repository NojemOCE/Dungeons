package dungeonmania.character;

import dungeonmania.util.*;

public class Mercenary extends Character {

    
    public Mercenary(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
        setAlly(false);
    }


    @Override
    public void move(Position position) {
        return;
    }

    private void followPlayer(){
        
    }
}
