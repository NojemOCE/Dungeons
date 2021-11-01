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

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Gains health by amount
     * @param health health
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HealthPoint other = (HealthPoint) obj;
        if (Double.doubleToLongBits(health) != Double.doubleToLongBits(other.health))
            return false;
        if (Double.doubleToLongBits(maxHealth) != Double.doubleToLongBits(other.maxHealth))
            return false;
        return true;
    }
    
}
