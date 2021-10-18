package dungeonmania.gamemode;


/**
 * Controller has a current gamemode
 */
public abstract class Gamemode {
    private int spawnRate;
    private boolean enemyAttackEnabled;
    private boolean invincibilityEnabled;
    private double startingHP; 

    private final static int STANDARD_SPAWN_RATE = 20;
    private final static double STANDARD_STARTING_HP = 20;
    


    /**
     * Constructor with 2 arguments
     * @param enemyAttackEnabled whether or not enemy attack is enabled
     * @param invincibilityEnabled whether the invicibility potion has an effect
     */
    public Gamemode(boolean enemyAttackEnabled, boolean invincibilityEnabled) {
        this.spawnRate = STANDARD_SPAWN_RATE;
        this.enemyAttackEnabled = enemyAttackEnabled;
        this.invincibilityEnabled = invincibilityEnabled;
        this.startingHP = STANDARD_STARTING_HP;
    }

    /**
     * Constructor with 4 arguments
     * @param spawnRate rate at which zombie toast spawns (num ticks)
     * @param enemyAttackEnabled whether or not enemy attack is enabled
     * @param invincibilityEnabled whether the invicibility potion has an effect
     * @param startingHP starting HP of the player
     */
    public Gamemode(int spawnRate, boolean enemyAttackEnabled, boolean invincibilityEnabled, double startingHP) {
        this.spawnRate = spawnRate;
        this.enemyAttackEnabled = enemyAttackEnabled;
        this.invincibilityEnabled = invincibilityEnabled;
        this.startingHP = startingHP;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public boolean isEnemyAttackEnabled() {
        return enemyAttackEnabled;
    }

    public boolean isInvincibilityEnabled() {
        return invincibilityEnabled;
    }

    public double getStartingHP() {
        return startingHP;
    }

}
