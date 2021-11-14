package dungeonmania.gamemode;


/**
 * Controller has a current gamemode
 */
public abstract class Gamemode {
    private int spawnRate;
    private boolean enemyAttackEnabled;
    private boolean invincibilityEnabled;
    private double startingHP;
    private String type; 

    private final static int STANDARD_SPAWN_RATE = 20;
    private final static double STANDARD_STARTING_HP = 30;
    


    /**
     * Constructor for gamemode with 2 arguments
     * @param enemyAttackEnabled whether or not enemy attack is enabled
     * @param invincibilityEnabled whether the invicibility potion has an effect
     */
    public Gamemode(boolean enemyAttackEnabled, boolean invincibilityEnabled, String type) {
        this.spawnRate = STANDARD_SPAWN_RATE;
        this.enemyAttackEnabled = enemyAttackEnabled;
        this.invincibilityEnabled = invincibilityEnabled;
        this.startingHP = STANDARD_STARTING_HP;
        this.type = type;
    }

    /**
     * Constructor for gamemode with 4 arguments
     * @param spawnRate rate at which zombie toast spawns (num ticks)
     * @param enemyAttackEnabled whether or not enemy attack is enabled
     * @param invincibilityEnabled whether the invicibility potion has an effect
     * @param startingHP starting HP of the player
     */
    public Gamemode(int spawnRate, boolean enemyAttackEnabled, boolean invincibilityEnabled, double startingHP, String type) {
        this.spawnRate = spawnRate;
        this.enemyAttackEnabled = enemyAttackEnabled;
        this.invincibilityEnabled = invincibilityEnabled;
        this.startingHP = startingHP;
        this.type = type;
    }

    /**
     * Gets the current spawn rate for zombies
     * @return spawn rate for zombies
     */
    public int getSpawnRate() {
        return spawnRate;
    }

    /**
     * Gets whether enemy attack is enabled
     * @return true if enemy attack is enabled, false otherwise
     */
    public boolean isEnemyAttackEnabled() {
        return enemyAttackEnabled;
    }

    /**
     * Gets whether invincibility is enabled
     * @return true if invincibility is enabled, false otherwise
     */
    public boolean isInvincibilityEnabled() {
        return invincibilityEnabled;
    }

    /**
     * Gets the starting HP of the player
     * @return starting HP double of player
     */
    public double getStartingHP() {
        return startingHP;
    }

    /**
     * Gets the current gamemode type
     * @return string for gamemode type
     */
    public String getGameModeType(){
        return type;

    }

}
