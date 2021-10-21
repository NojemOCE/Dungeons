package dungeonmania.character;

import dungeonmania.inventory.Inventory;
import dungeonmania.util.*;


public class Player extends Character {

    private Inventory inventory;

    public Player(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
    }


    @Override
    public void move(Direction direction) {
        return;
    }

    @Override
    public double attack() {
        // check inventory and mercenary in range

        return getAttackDamage();
    }

    @Override
    public double defend() {
        // check inventory and mercenary in range

        return 0;
    }

}  
