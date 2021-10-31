package dungeonmania.movingEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Set;

import org.json.JSONObject;

import dungeonmania.Passive;
import dungeonmania.World;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.util.*;

public class Player extends MovingEntity {

    static final int PLAYER_ATTACK = 3;
    private Set<Mercenary> mercenariesInRange = new HashSet<>();

    private Set<PlayerPassiveObserver> passiveObservers = new HashSet<>();
    // observer here for passive
    private Passive activePotion;

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
        this.activePotion = null;
    }

    public Player(int x, int y, String id, HealthPoint healthPoint, Passive activePotion) {
        this(x, y, id, healthPoint);
        setAlly(true);
        this.activePotion = activePotion;
    }

  
    @Override
    public void move(World world) {
        return;
    
    }
    public void tick(Direction movementDirection, World world) {
        // check if the direction we are moving is valid first before setting new position
        // Tick passive
        if (!Objects.isNull(activePotion)) {
            activePotion.decreaseDuration();
            activePotion.applyPassive(this);
            if (activePotion.getDuration() == 0){
                activePotion = null;
            } 

        } else {
            notifyPassive("N/A");
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
        if (Objects.isNull(activePotion) || !activePotion.getType().equals("invincibility_potion")) {
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
        if (!enemy.getAlly()) {

            if (Objects.isNull(activePotion) || !activePotion.getType().equals("invisibility_potion")) {
                notifyObserversForBattle(1); // mercenary speed

                return new Battle(this, enemy, gamemode.isEnemyAttackEnabled());
            }

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
        activePotion = (Passive) potion;
    }


    @Override
    public JSONObject saveGameJson() {
        JSONObject playerJSON = super.saveGameJson();

        JSONObject activePotionJSON = new JSONObject();

        if (!Objects.isNull(activePotion)) {
            activePotionJSON.put("active-potion", activePotion.getType());
            activePotionJSON.put("duration", activePotion.getDuration());
            playerJSON.put("active-potion", activePotionJSON);
        }


        return playerJSON;
    }
} 

