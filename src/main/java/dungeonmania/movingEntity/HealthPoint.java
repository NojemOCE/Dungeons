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
     * Constuctor for health point taking a double for health and a double for max health
     * @param health double to set health to
     * @param maxHealthdouble to set maxHealth to
     */
    public HealthPoint(double health, double maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
    }
    /**
     * Gives the current health
     * @return health
     */
    public double getHealth() {
        return health;
    }

    /**
     * Gets the max health
     * @return max health
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the health to a given double
     * @param health double to set health to
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Sets max health to a given double
     * @param health double to set max health to
     */
    public void setMaxHealth(double health) {
        this.maxHealth = health;
    }

    /**
     * Gains health by amount
     * @param health health amount to gain
     */
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
