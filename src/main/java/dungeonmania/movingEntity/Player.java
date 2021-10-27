package dungeonmania.movingEntity;

import java.util.List;

import dungeonmania.World;
import dungeonmania.buildable.Buildable;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.collectable.Key;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.util.*;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;


public class Player extends MovingEntity {

    private Inventory inventory;

    public Player(int x, int y, String id, Gamemode gameMode) {

        // Attack damage set at 1 for now and layer set at  1
        super(new Position(x, y, 1), id, "player", new HealthPoint(gameMode.getStartingHP()), 1, gameMode);
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

    public Key keyInInventory(String keyColour) {
        return inventory.keyInInventory(keyColour);
    }

    public void use(CollectableEntity item) {
        inventory.use(item);
    }

    public List<ItemResponse> getInventoryResponse() {
        return inventory.getInventoryResponse();
    }
}  
