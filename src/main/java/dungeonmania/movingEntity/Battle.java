package dungeonmania.movingEntity;

import dungeonmania.inventory.Inventory;
import dungeonmania.util.Position;

public class Battle {
    private Player player;
    private MovingEntity character;
    private Position position;
    
    private boolean enemyAttackEnabled;
    private boolean playerWins;
    private boolean activeBattle;

    /**
     * Constructor for Battle taking a player and a character that are engaged in battle
     * @param player Player that is engaged in battle
     * @param character enemy that the player is fighting in battle
     */
    public Battle(Player player, MovingEntity character, Boolean enemyAttack) {
        this.player = player;
        this.character = character;
        this.activeBattle = true;
        this.enemyAttackEnabled = enemyAttack;
    }

    /**
     * Simulates 1 tick worth of battle for the player and the enemy
     * @param inventory takes in the inventory to determine the attack and defence modifiers of the player
     */
    public void battleTick(Inventory inventory) {

        // Base attack modified by inventory weapons
        double playerAttack = inventory.attackModifier(player.getAttackDamage());
        // Character attack modified by players defence weapons
        double characterAttack = inventory.defenceModifier(character.getAttackDamage());

        double currentPlayerHealth = player.getHealthPoint().getHealth();
        double currentEnemyHealth = character.getHealthPoint().getHealth();

        character.defend((currentPlayerHealth * playerAttack)/10);
        //System.out.println("Player attacks with " + (player.getHealthPoint().getHealth() * playerAttack)/10);
        if (enemyAttackEnabled) {
            player.defend((currentEnemyHealth * characterAttack)/10);

        }
        //System.out.println("Enemy attacks with " + (character.getHealthPoint().getHealth() * characterAttack)/10);

        // ally help
        player.alliesInRange().forEach(ally -> {

            character.defend((ally.getHealthPoint().getHealth() * ally.getAttackDamage()) / 10);
        });


        if (character.getHealthPoint().getHealth() == 0) {
            endBattle(true);
        }

        if (player.getHealthPoint().getHealth() == 0) {
            if (inventory.hasItem("one_ring")) {
                inventory.useByType("one_ring");
                player.getHealthPoint().gainHealth(999);  
                return;
            }
            endBattle(false);
        }
        // game over
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
        player.endBattle();
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
