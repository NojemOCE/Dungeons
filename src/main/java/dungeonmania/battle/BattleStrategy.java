package dungeonmania.battle;

import dungeonmania.inventory.Inventory;

public interface BattleStrategy {

    /**
     * Provides the attack provided to the player from the inventory
     * @param playerAttack the players attack before the modification
     * @return the attack of the player after all attack modifications of the inventory have been included
     */
    public double attackModifier(Inventory inventory, double playerAttack);

    /**
     * Provides the modified attack of the enemy when the defense modifier of the inventory has been applied
     * @param enemyAttack the enemy attack before the modification
     * @return the attack of the enemy after all defence modifications of the inventory have been included
     */
    public double defenceModifier(Inventory inventory, double enemyAttack);
}
