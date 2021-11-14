package dungeonmania.movingEntity;

import dungeonmania.battle.BattleStrategy;
import dungeonmania.inventory.Inventory;

public class Battle {
    private Player player;
    private MovingEntity character;
    private BattleStrategy battleStrat;
    
    
    private boolean enemyAttackEnabled;
    private boolean playerWins;
    private boolean activeBattle;

    
    /**
     * Constructor for Battle taking a player and a character that are engaged in battle, enemy attack and battle strategy
     * @param player Player that is engaged in battle
     * @param character enemy that the player is fighting in battle
     * @param enemyAttack boolean for whether enemy attack is enabled
     * @param bs battle strategy
     */
    public Battle(Player player, MovingEntity character, Boolean enemyAttack, BattleStrategy bs) {
        this.player = player;
        this.character = character;
        this.activeBattle = true;
        this.enemyAttackEnabled = enemyAttack;
        this.battleStrat = bs;
    }

    /**
     * Simulates 1 tick worth of battle for the player and the enemy
     * @param inventory takes in the inventory to determine the attack and defence modifiers of the player
     */
    public void battleTick(Inventory inventory) {
        if (character.getHealthPoint().getHealth() == 0) {
            endBattle(true);
            return;
        } 
        if (player.getHealthPoint().getHealth() == 0) {
            if (inventory.hasItem("one_ring")) {
                inventory.useByType("one_ring");
                player.getHealthPoint().gainHealth(999);  
            } else {
                endBattle(false);
                return;
            }
        }


        // Base attack modified by inventory weapons
        // Character attack modified by players defence weapons
        double playerAttack = battleStrat.attackModifier(inventory, player.getAttackDamage());
        // Character attack modified by players defence weapons
        double characterAttack = battleStrat.defenceModifier(inventory, player.getAttackDamage());

        double currentPlayerHealth = player.getHealthPoint().getHealth();
        double currentEnemyHealth = character.getHealthPoint().getHealth();

        character.defend((currentPlayerHealth * playerAttack)/10);
        if (enemyAttackEnabled) {
            player.defend((currentEnemyHealth * characterAttack)/10);
        }
        // ally help
        player.alliesInRange().forEach(ally -> {
            character.defend((ally.getHealthPoint().getHealth() * ally.getAttackDamage()) / 10);
        });

        battleTick(inventory);
    }

    /**
     * Gives the MovingEntity object that is currently opposing the player in battle
     * @return MovingEntity currently in battle with the player
     */
    public MovingEntity getCharacter() {
        return character;
    }

    /**
     * Returns a boolean to indicate if the battle is active
     * @return true if the battle is active, false otherwise
     */
    public boolean isActiveBattle() {
        return activeBattle;
    }

    /**
     * Sets the status of the battle to a given boolean (true = active battle, false = inactive battle)
     * @param activeBattle status to set the battle too
     */
    public void setActiveBattle(boolean activeBattle) {
        this.activeBattle = activeBattle;
    }


    /**
     * Ends the battle and updates the playerWins field to whether the player has won or not
     * @param playerWin whether the player has won. true if the player won the battle, false otherwise
     */
    private void endBattle(boolean playerWin) {
        setActiveBattle(false);
        this.playerWins = playerWin;
    }

    /**
     * Returns whether the player has won the battle
     * @return true if the player won the battle, false otherwise
     */
    public boolean getPlayerWins() {
        return this.playerWins;
    }

}
