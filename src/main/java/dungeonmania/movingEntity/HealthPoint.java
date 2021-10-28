package dungeonmania.movingEntity;

public class HealthPoint {
    
    private double health;
    private double maxHealth;

    public HealthPoint(double health) {
        this.health = health;
        this.maxHealth = health;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
    
    public void loseHealth(double attack) {
        this.health -= attack;
        if (this.health <= 0 ) setHealth(0);
    }
    
}
