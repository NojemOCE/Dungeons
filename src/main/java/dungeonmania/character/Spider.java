package dungeonmania.character;

import dungeonmania.util.*;


public class Spider extends Character {

    public Spider(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
    }

    @Override
    public void move(Direction direction) {
        return;
    }
}
