package dungeonmania.character;

import dungeonmania.util.*;

public class Mercenary extends Character {
    
    public Mercenary(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
    }


    @Override
    public void move(Direction direction) {
        return;
    }
}
