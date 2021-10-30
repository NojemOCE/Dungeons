package dungeonmania.movingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Set;

import org.json.JSONObject;

import dungeonmania.Passive;
import dungeonmania.World;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.util.*;
import dungeonmania.staticEntity.StaticEntity;


public class Player extends MovingEntity {

    static final int PLAYER_ATTACK = 3;

    private Set<Mercenary> mercenariesInRange = new HashSet<>();

    private Set<PlayerPassiveObserver> passiveObservers = new HashSet<>();
    // observer here for passive
    private Map<String, Passive> activePotions = new HashMap<>();

    /**
     * Constructor for player taking an x coordinate, a y coordinate, an id and a HealthPoint
     * @param x x coordinate of the player
     * @param y y coordinate of the player
     * @param id unique entity id of the player
     * @param healthPoint healthpoint of the player
     */
    public Player(int x, int y, String id, HealthPoint healthPoint) {
        super(new Position(x, y, 2), id, "player", healthPoint, PLAYER_ATTACK);
        setAlly(true);
    }
    
  
    @Override
    public void move(World world) {
        return;
    
    }

     /**
     * Returns the new position if posible
     * and the old position (no movement) if not
     */
    @Override
    public Position validMove(Position position, World world) {
        
        // Check for boundaries of the map her

        // check if there is a static entity in the way
        StaticEntity se = world.getStaticEntity(position);
        if (!Objects.isNull(se)) {
            // interact with static entitity
            return se.interact(world, this); 
        } 
        if (!Objects.isNull(world.getBattle())) {
            // check if this objects position is same as players (for players if there is a battle)
            // they cannot move anyways
            if (getPosition().equals(world.getPlayer().getPosition())) {
                // cannot move into battle, wait outside
                return getPosition();
            }
        }


        return position;
    }

    // TODO shouldn't this be done in move?
    // TODO implement using item?
    public void tick(Direction movementDirection, World world) {
        // check if the direction we are moving is valid first before setting new position
        // Tick passive
        for (Passive potion: activePotions.values()) {
            potion.decreaseDuration();
            potion.applyPassive(this);
            if (potion.getDuration() == 0){
                activePotions.remove(potion.getType());
            } 
        }

        if (!Objects.isNull(movementDirection)){
            setPosition(validMove(this.getPosition().translateBy(movementDirection), world));
            CollectableEntity e = world.getCollectableEntity(this.getPosition());
            if (!Objects.isNull(e)) {
                e.collect();
            }
        } 
    }

    @Override
    public void defend(double attack) {
        // check inventory and mercenary in range
        if (Objects.isNull(activePotions.get("invincibility_potion"))) {
            getHealthPoint().loseHealth(attack);
        } // doesnt lose health if invincible

    }

    /**
     * Creates a new battle between a player and an enemy
     * @param enemy enemy that the player fights in the battle
     * @return new Battle
     */
    public Battle battle(MovingEntity enemy, Gamemode gamemode) {
        // we can pass in invincibility state for battle 
        // or invisibility battle wont be created "return null"
        if (!enemy.getAlly() && Objects.isNull(activePotions.get("invisibility_potion"))) {
            notifyObserversForBattle(1); // mercenary speed

            return new Battle(this, enemy, gamemode.isEnemyAttackEnabled());
            // notify observers of battle
        }
        return null;
    }

    /**
     * Notifies observers of a battle ending
     */
    public void endBattle() {
        // notify observers of ending battle
        notifyObserversForBattle(0);
    }


    //TODO add javadoc comment idk what this method does
    /**
     * 
     * @param inRange
     */
    public void addInRange(Mercenary inRange) {
        mercenariesInRange.add(inRange);
    
    }
    
    //TODO add javadoc comment idk what this method does
    /**
     * 
     * @param inRange
     */
    public void removeInRange(Mercenary inRange) {
        mercenariesInRange.remove(inRange);
    }

    public void subscribePassiveObserver(PlayerPassiveObserver me) {
        passiveObservers.add(me);
    
    }
    
    //TODO add javadoc comment idk what this method does
    /**
     * 
     * @param inRange
     */
    public void unsubscribePassiveObserver(PlayerPassiveObserver me) {
        passiveObservers.remove(me);
    }
    
    public List<MovingEntity> alliesInRange() {
        
        List<MovingEntity> allies = new ArrayList<>();
        for (MovingEntity m : mercenariesInRange) {
            if (m.getAlly()) allies.add(m);
        }
        return allies;
    }
    
    //TODO add javadoc comment idk what this method does
    /**
     * 
     * @param speed
     */
    public void notifyObserversForBattle(int speed) { // notify observers for battle
        mercenariesInRange.forEach( mercenary -> {
            mercenary.setSpeed(speed);
        });
    }

    public void notifyPassive(String passive) {
        passiveObservers.forEach( obj -> {
            obj.updateMovement(passive);
        });
    }

    public void addPotion(CollectableEntity potion) {
        activePotions.put(potion.getType(), (Passive) potion);
    }


    @Override
    public JSONObject saveGameJson() {
        JSONObject playerJSON = super.saveGameJson();


        List<String> mercList = new ArrayList<>();
        mercList = mercenariesInRange.stream().map(MovingEntity::getId).collect(Collectors.toList());

        playerJSON.put("mercenaries", mercList);
        //TODO STILL NEED TO REMOUNT ALL OBSERVERS AFTER LOADING A SAVED GAME
        return playerJSON;
    }
} 

