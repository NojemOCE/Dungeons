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
    private double allyAttack;

    public Player(int x, int y, String id, Gamemode gameMode) {
        // Attack damage set at 1 for now and layer set at  1
        super(new Position(x, y, 1), id, "player", new HealthPoint(gameMode.getStartingHP()), 1, gameMode);
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
        
        // then add on mercenary modifier
        attackModifier += allyAttack;
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
            notifyObservers(1);

            return new Battle(this, enemy);
            // notify observers of battle
        }
        return null;
    }

    public void endBattle() {
        // notify observers of ending battle
        notifyObservers(0);
    }

    public List<ItemResponse> getInventoryResponse() {
        return inventory.getInventoryResponse();
    }

    public void registerEntity(Mercenary inRange) {
        mercenaryObservers.add(inRange);
        
        if (inRange.getAlly()) {
            allyAttack += inRange.getAttackDamage();
        }
    }
    
    public void unregisterEntity(Mercenary inRange) {
        if (inRange.getAlly()) {
            allyAttack -= inRange.getAttackDamage();
        }
        mercenaryObservers.remove(inRange);
    }

    public void notifyObservers(int speed) { // notify observers for battle
        mercenaryObservers.forEach( mercenary -> {
            mercenary.setSpeed(speed);
        });
    }
} 

