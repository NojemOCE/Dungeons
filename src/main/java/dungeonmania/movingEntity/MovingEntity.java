package dungeonmania.movingEntity;

import dungeonmania.util.Position;

import java.util.Objects;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.util.Direction;
import dungeonmania.staticEntity.StaticEntity;




public abstract class MovingEntity extends Entity implements Movement {
    private HealthPoint healthPoint;
    private double attackDamage;
    private Position position;

    private Gamemode gameMode;

    private boolean ally;

    public MovingEntity(Position position, String id, String type, HealthPoint healthPoint, int attackDamage) {
        super(position, id, type);
        this.healthPoint = healthPoint;
        this.attackDamage = attackDamage;
    }


    // Attack and defend will be used to calculate in the battle class
    public double attack() {
        return attackDamage;
    }

    public double defend() {
        return 0;
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
        if (!world.getBattles().isEmpty()) {
            // check if this objects position is same as players (for players if there is a battle)
            // they cannot move anyways
            if (this.getPosition().equals(world.getPlayer().getPosition())) {
                // cannot move
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

    public void setAttackDamage(int attackDamage) {
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
    
}
