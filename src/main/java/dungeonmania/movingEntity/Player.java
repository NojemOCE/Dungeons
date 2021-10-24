package dungeonmania.movingEntity;

import java.util.List;

import dungeonmania.World;
import dungeonmania.buildable.Buildable;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.util.*;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;


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

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "player", getPosition(), true);
    }

    public List<ItemResponse> getInventoryResponse() {
        return inventory.getInventoryResponse();
    }
}  
