package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.buildable.Buildable;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.util.*;
import dungeonmania.inventory.Inventory;


public class Player extends MovingEntity {

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

    public boolean inInventory(CollectableEntity item) {
        return inventory.isPresent(item);
    }

    public boolean inInventory(Buildable item) {
        return inventory.isPresent(item);
    }
}  
