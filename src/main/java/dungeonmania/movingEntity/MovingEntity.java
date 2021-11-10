package dungeonmania.movingEntity;

import dungeonmania.util.Position;

import java.util.List;
import java.util.Objects;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.movingEntity.States.NormalState;
import dungeonmania.movingEntity.States.State;
import dungeonmania.movingEntity.States.SwampState;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.SwampTile;




public abstract class MovingEntity extends Entity {
    private HealthPoint healthPoint;
    private double attackDamage;

    protected MovementStrategy movementStrategy;
    protected MovementStrategy defaultMovementStrategy;

    private boolean ally;
    private State state;

    /**
     * Connstructor for moving entity taking position, id, type, healthpoint and attack damage
     * @param position position of the moving entity
     * @param id unique id of the entity
     * @param type string type of the entity
     * @param healthPoint healthpoint object of the entity
     * @param attackDamage attack damage double of the entity
     */
    public MovingEntity(Position position, String id, String type, HealthPoint healthPoint, double attackDamage) {
        super(position, id, type);
        this.healthPoint = healthPoint;
        this.attackDamage = attackDamage;
<<<<<<< HEAD
=======
        this.state = new NormalState();
>>>>>>> master
    }

    /**
     * Defends against an attack
     * @param attack attack damage
     */
    public void defend(double attack) {
        this.healthPoint.loseHealth(attack);
    }

    /**
     * Moves an entity in accordance with its movement state
     * @param world current world that the entity resides in. Allows the entity to observe its surroundings
     */
    public abstract void move(World world);

    /**
     * Returns the new position if posible
     * and the old position (no movement) if not
     */
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
     * Gets the healthpoint object of the entity
     * @return healthpoint object of moving entity
     */
    public HealthPoint getHealthPoint() {
        return healthPoint;
    }

    /**
     * Adds a given amount to the entity's health
     * @param health health to add
     */
    public void addHealth(double health) {
        this.healthPoint.gainHealth(health);
    }

    /**
     * Gets the attack damage of the moving entity
     * @return attack damage of entity
     */
    public double getAttackDamage() {
        return attackDamage;
    }

    /**
     * Sets the attack damage of the moving entity
     * @param attackDamage double to set attack damage to
     */
    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Gets whether the moving entity is an ally of the player
     * @return true if ally, false otherwise
     */
    public boolean getAlly() {
        return this.ally;
    }

    /**
     * Sets the moving entity as an ally or enemy
     * @param ally if true, sets player as ally. Otherwise sets player as enemy.
     */
    public void setAlly(boolean ally) {
        this.ally = ally;
    }

    /**
     * Sets the moving entity's current movement strategy
     * @param strategy movement strategy to set
     */
    public void setMovement(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    /**
     * Gets the moving entity's movement strategy
     * @return current movement strategy of moving entity
     */
    public MovementStrategy getMovement() {
        return this.movementStrategy;
    }

    public MovementStrategy getDefaultMovementStrategy() {
        return defaultMovementStrategy;
    }

    /**
     * Sets the moving entity's default movement strategy
     * @param defaultMovementStrategy default movement strategy to set
     */
    public void setDefaultMovementStrategy(MovementStrategy defaultMovementStrategy) {
        this.defaultMovementStrategy = defaultMovementStrategy;
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject saveObj = new JSONObject();
        saveObj.put("type", getType());
        saveObj.put("x",  getPosition().getX());
        saveObj.put("y",  getPosition().getY());
        saveObj.put("id",  getId());
        
        JSONObject healthObj = new JSONObject();
        healthObj.put("health", getHealthPoint().getHealth());
        healthObj.put("max-health", getHealthPoint().getMaxHealth());

        saveObj.put("health-point", healthObj);

        return saveObj;
    }

    
    
    /**
     * Moves an entity in accordance with its movement strategy
     * @param world current world that the entity resides in. Allows the entity to observe its surroundings
     */
    abstract public void moveEntity(World world);

    /**
     * Gets the movement state of the moving entity
     * @return movement state of the entity (normal or swamp)
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the movement state of the moving entity
     * @param state state to set to (normal or swamp)
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Checks if the moving entity is on a swamp tile, and updates their movement state if they are
     * @param world world in which the moving entity resides
     */
    public void checkSwampTile(World world) {
        List<StaticEntity> statics = world.getStaticEntitiesAtPosition(getPosition());
            for (StaticEntity s: statics) {
                if (s instanceof SwampTile) {
                    setState(new SwampState(((SwampTile) s).getMovementFactor()));
                    return;
                }
            }
    }

    // @Override
    // public String toString() {
    //     return "MovingEntity [ally=" + ally + ", attackDamage=" + attackDamage + ", defaultMovementStrategy="
    //             + defaultMovementStrategy + ", healthPoint=" + healthPoint + ", movementStrategy=" + movementStrategy
    //             + ", position=" + getPosition()+ ", speed=" + speed + "]";
    // }

}
