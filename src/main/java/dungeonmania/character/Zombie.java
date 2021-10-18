package dungeonmania.character;

import dungeonmania.util.*;

public class Zombie extends Character{

    public Zombie(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
    }
    
    @Override
    public void move(Direction direction) {
        return;
    }
}
