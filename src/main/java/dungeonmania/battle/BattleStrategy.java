package dungeonmania.battle;

import dungeonmania.inventory.Inventory;

public interface BattleStrategy {
    public double attackModifier(Inventory inventory, double playerAttack);
    public double defenceModifier(Inventory inventory, double enemyAttack);
}
