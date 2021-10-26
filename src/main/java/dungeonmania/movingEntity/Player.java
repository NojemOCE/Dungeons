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

    private List<Mercenary> mercenaryObservers;


    public Player(int x, int y, String id) {
        super(new Position(x, y), id, "player", new HealthPoint(100), 10);
        setAlly(true);
    }
  
    @Override
    public void move(World world) {
        return;
    }

    public void tick(String itemUsed, Direction movementDirection, World world) {
        // check if the direction we are moving is valid first before setting new position
        if (itemUsed.isEmpty()) {
            setPosition(validMove(this.getPosition().translateBy(movementDirection), world));
        } else {
            // use item
            inventory.useItem(itemUsed);
        }
    }

    @Override
    public double attack() {
        // check inventory and mercenary in range
        double attackModifier = getAttackDamage() * inventory.attackModifier();
        
        return attackModifier;
    }

    @Override
    public void defend(double attack) {
        // check inventory and mercenary in range
        double defend = attack * inventory.defenceModifier();
        getHealthPoint().loseHealth(defend);

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
        inventory.use(this, item);
    }

    public Battle battle(MovingEntity enemy) {
        // we can pass in invincibility state for battle 
        // or invisibility battle wont be created "return null"
        if (!enemy.getAlly()) {
            return new Battle(this, enemy);
        }
        return null;
    }

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "player", getPosition(), true);
    }

    public List<ItemResponse> getInventoryResponse() {
        return inventory.getInventoryResponse();
    }

    public void registerEntity(Mercenary inRange) {
        mercenaryObservers.add(inRange);

    }
    
    public void unregisterEntity(Mercenary inRange) {
        if (inRange.getAlly()) {
            setAttackDamage(getAttackDamage() - inRange.attack());
        }
        mercenaryObservers.remove(inRange);
    }

    public void notifyObservers() {
        mercenaryObservers.forEach( mercenary -> {
            if (mercenary.getAlly()) {
                // add to attack in battle
                setAttackDamage(getAttackDamage() + mercenary.getAttackDamage());
                // mercenary.
            } else {
                // mercenaries should move 2 spaces
                mercenary.setSpeed(1);
            }
        });
    }
} 

