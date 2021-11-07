package dungeonmania.battle;

import dungeonmania.inventory.Inventory;

public interface BattleStrategy {
    public double attackModifier(Inventory inventory);
    public double defenceModifier(Inventory inventory);
}
