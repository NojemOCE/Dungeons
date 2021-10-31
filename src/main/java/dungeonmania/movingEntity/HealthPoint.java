package dungeonmania.movingEntity;

public class HealthPoint {
    
    private double health;
    private double maxHealth;

    /**
     * Constructor for health point which takes a double for health
     * @param health double to set health and maxHealth to
     */
    public HealthPoint(double health) {
        this.health = health;
        this.maxHealth = health;
    }

    /**
     * Gives the current health
     * @return health
     */
    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }


    // TODO can we remove this I don't think we need this. maybe an increase health method
    public void setHealth(double health) {
        this.health = health;
    }

    public void gainHealth(double health) {
        this.health += health;
        if (this.health >= this.maxHealth) setHealth(maxHealth);
    }
    
    /**
     * Decreases the health by a given double
     * @param attack double to reduce health by 
     */
    public void loseHealth(double attack) {
        this.health -= attack;
        if (this.health <= 0 ) setHealth(0);
    }
    
}
