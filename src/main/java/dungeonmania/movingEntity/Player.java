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

    private List<Mercenary> mercenaryObservers;
    private double allyAttack;

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
        }
    }

    @Override
    public double attack(double attack) {
        // check inventory and mercenary in range
        
        // then add on mercenary modifier
        attack += allyAttack;
        return attack;
    }

    @Override
    public void defend(double attack) {
        // check inventory and mercenary in range
        getHealthPoint().loseHealth(attack);

    }


    public Battle battle(MovingEntity enemy, Inventory inventory) {
        // we can pass in invincibility state for battle 
        // or invisibility battle wont be created "return null"
        if (!enemy.getAlly()) {
            notifyObservers(1);

            return new Battle(this, enemy, inventory);
            // notify observers of battle
        }
        return null;
    }

    public void endBattle() {
        // notify observers of ending battle
        notifyObservers(0);
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), true);
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

