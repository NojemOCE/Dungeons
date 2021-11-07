package dungeonmania.battle;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Weapon;
import dungeonmania.collectable.Bow;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.inventory.Inventory;

public class BossBattle implements BattleStrategy {
    public double attackModifier(Inventory inventory, double playerAttack) {
        List<String> idToRemove = new ArrayList<>();
        for (CollectableEntity item : inventory.getCollectableItems().values()) {
            if (item instanceof Weapon) {
                playerAttack += ((Weapon)item).bossAttackModifier();
                ((CollectableEntity)item).consume();
                if (item.getDurability() == 0) {
                    idToRemove.add(item.getId());
                }
            }
        }

        for (CollectableEntity item : inventory.getCollectableItems().values()) {
            if (item instanceof Bow ) {
                playerAttack *= ((Bow)item).attackModifier();
                ((Bow)item).consume();
                if (item.getDurability() == 0) {
                    idToRemove.add(item.getId());
                }
            }
        }
        
        inventory.removeItem(idToRemove);
        
        return playerAttack;
    }
}
