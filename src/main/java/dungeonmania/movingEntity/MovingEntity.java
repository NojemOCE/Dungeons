package dungeonmania.movingEntity;

import dungeonmania.util.Position;

import java.util.Objects;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.CircleMovement;
import dungeonmania.movingEntity.MovementStrategies.FollowPlayer;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.movingEntity.MovementStrategies.RunAway;
import dungeonmania.staticEntity.StaticEntity;




public abstract class MovingEntity extends Entity {
    private HealthPoint healthPoint;
    private double attackDamage;

    private int speed;

    protected Movement movementStrategy;
    protected Movement defaultMovementStrategy;

    private boolean ally;

    public MovingEntity(Position position, String id, String type, HealthPoint healthPoint, double attackDamage) {
        super(position, id, type);
        this.healthPoint = healthPoint;
        this.attackDamage = attackDamage;
        this.speed = 0;
    }


    public void defend(double attack) {
        this.healthPoint.loseHealth(attack);
    }

    public abstract void move(World world);

    /**
     * Returns the new position if posible
     * and the old position (no movement) if not
     */
    public Position validMove(Position position, World world) {
        
        // Check for boundaries of the map here
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

    public HealthPoint getHealthPoint() {
        return healthPoint;
    }

    public void addHealth(double health) {
        this.healthPoint.gainHealth(health);
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public boolean getAlly() {
        return this.ally;
    }

    public void setAlly(boolean ally) {
        this.ally = ally;
    }

    public void setMovement(Movement strategy) {
        this.movementStrategy = strategy;
    }

    public Movement getMovement() {
        return this.movementStrategy;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Movement getDefaultMovementStrategy() {
        return defaultMovementStrategy;
    }

    public void setDefaultMovementStrategy(Movement defaultMovementStrategy) {
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

    // @Override
    // public String toString() {
    //     return "MovingEntity [ally=" + ally + ", attackDamage=" + attackDamage + ", defaultMovementStrategy="
    //             + defaultMovementStrategy + ", healthPoint=" + healthPoint + ", movementStrategy=" + movementStrategy
    //             + ", position=" + getPosition()+ ", speed=" + speed + "]";
    // }

    //might be better to move to movement
    protected Movement getMovementFromString(String movement, String currDir, String nextDir, int remMovesCurr, int remMovesNext, boolean avoidPlayer) {
        switch(movement)  {
            case "circle":
                return new CircleMovement(currDir, nextDir, remMovesCurr, remMovesNext, avoidPlayer);
            case "followPlayer":
                return new FollowPlayer();
            case "randomMovement":
                return new RandomMovement();
            case "runAway":
                return new RunAway();
        }
        return null;
    }

    protected Movement getMovementFromString(String movement) {
        switch(movement)  {
            case "circle":
                return new CircleMovement();
            case "followPlayer":
                return new FollowPlayer();
            case "randomMovement":
                return new RandomMovement();
            case "runAway":
                return new RunAway();
        }
        return null;
    }
    
    
    
}
