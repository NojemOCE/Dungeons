package dungeonmania.movingEntity;

import dungeonmania.util.Position;

import java.util.Objects;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.staticEntity.StaticEntity;




public abstract class MovingEntity extends Entity {
    private HealthPoint healthPoint;
    private double attackDamage;
    private Position position;

    private int speed;

    private Gamemode gameMode;
    protected Movement movementStrategy;
    

    private boolean ally;

    public MovingEntity(Position position, String id, String type, HealthPoint healthPoint, int attackDamage) {
        super(position, id, type);
        this.healthPoint = healthPoint;
        this.attackDamage = attackDamage;
        this.speed = 0;
    }


    // Attack and defend will be used to calculate in the battle class
    public double attack(double attack) {

        // need to go through caclulators (player may have weapons)
        return getAttackDamage();
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
        if (!Objects.isNull(c)) {
            // two characters cant be in same place, dont move this object
            return getPosition();
        } 
        

        return position;
    }

    public HealthPoint getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(HealthPoint healthPoint) {
        this.healthPoint = healthPoint;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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


    abstract public EntityResponse getEntityResponse();
    
}
