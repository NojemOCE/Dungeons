package dungeonmania.character;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;



public abstract class Character implements Movement {
    private HealthPoint healthPoint;
    private double attackDamage;
    private Position position;

    private boolean ally;

    public Character(HealthPoint healthPoint, double attackDamage, Position position) {
        this.healthPoint = healthPoint;
        this.attackDamage = attackDamage;
        this.position = position;
    }

    public double attack() {
        return attackDamage;
    }

    public double defend() {
        return 0;
    }

    @Override
    public void move(Position position) {
        return;
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
