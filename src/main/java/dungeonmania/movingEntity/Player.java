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
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.*;

public class Player extends MovingEntity {

    static final int PLAYER_ATTACK = 3;
    private Set<MercenaryComponent> mercenariesInRange = new HashSet<>();

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
        super(new Position(x, y, Position.MOVING_LAYER), id, "player", healthPoint, PLAYER_ATTACK);
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

    /**
     * Returns the new position if posible
     * and the old position (no movement) if not
     */
    @Override
    public Position validMove(Position position, World world) {

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
        // also check if another moving entity in the position already
        MovingEntity c = world.getCharacter(position);
        if (!this.getType().equals("player") && !Objects.isNull(c)) {
            // two characters cant be in same place, dont move this object
            return getPosition();
        } 
        

        return position;
    }

    /**
     * A world tick, player will either move or interact
     * @param movementDirection direction, null if item
     * @param world item, null if direction
     */
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

     
        setPosition(validMove(this.getPosition().translateBy(movementDirection), world));
        CollectableEntity e = world.getCollectableEntity(this.getPosition());
        if (!Objects.isNull(e)) {
            e.collect();
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
    public Battle battle(MovingEntity enemy, World world, Gamemode gamemode) {
        if (!enemy.getAlly()) {
            if (Objects.isNull(activePotion) || !activePotion.getType().equals("invisibility_potion")) {
                notifyObserversForBattle(world); // mercenary speed
                return new Battle(this, enemy, gamemode.isEnemyAttackEnabled());
            }
            // notify observers of battle
        }
        return null;
    }

    public void addInRange(MercenaryComponent mercenaryComponent) {
        mercenariesInRange.add(mercenaryComponent);
    
    }
    
    public void removeInRange(MercenaryComponent mercenaryComponent) {
        mercenariesInRange.remove(mercenaryComponent);
    }

    public void subscribePassiveObserver(PlayerPassiveObserver me) {
        passiveObservers.add(me);
    
    }

    public void unsubscribePassiveObserver(PlayerPassiveObserver me) {
        passiveObservers.remove(me);
    }
    
    /**
     * get a list of allies in range
     * @return list of allies
     */
    public List<MovingEntity> alliesInRange() {
    
        List<MovingEntity> allies = new ArrayList<>();
        for (MovingEntity m : mercenariesInRange) {
            if (m.getAlly()) allies.add(m);
        }
        return allies;
    }
    
    /**
     * Notifies mercenaries of battle for battle advantage
     * @param speed movement speed
     */
    public void notifyObserversForBattle(World world) { // notify observers for battle
        mercenariesInRange.forEach( mercenary -> {
            mercenary.move(world);

        });
    }

    /**
     * Notifies passive observers of player passive
     * @param passive passive type
     */
    public void notifyPassive(String passive) {
        passiveObservers.forEach( obj -> {
            obj.updateMovement(passive);
        });
    }

    /**
     * add active potion
     * @param potion
     */
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

    @Override
    public void moveEntity(World world) {
        return;
        
    }
} 

