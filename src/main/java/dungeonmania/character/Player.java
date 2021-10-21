package dungeonmania.character;

import dungeonmania.World;
import dungeonmania.inventory.Inventory;
import dungeonmania.util.*;


public class Player extends Character {

    private Inventory inventory;

    public Player(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
        setAlly(true);
    }
  
    @Override
    public void move(World world) {
        return;
    }

    public void move(Direction direction, World world) {
        // check if the direction we are moving is valid first before setting new position
        setPosition(validMove(this.getPosition().translateBy(direction), world));
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
