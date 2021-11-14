package dungeonmania.battle;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.collectable.Armour;
import dungeonmania.collectable.Bow;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.collectable.Protection;
import dungeonmania.collectable.Shield;
import dungeonmania.collectable.Weapon;
import dungeonmania.inventory.Inventory;

public class NormalBattle implements BattleStrategy {
    
    @Override
    public double attackModifier(Inventory inventory, double playerAttack) {
        List<String> idToRemove = new ArrayList<>();
        for (CollectableEntity item : inventory.getCollectableItems().values()) {
            if (item instanceof Weapon) {
                playerAttack += ((Weapon)item).attackModifier();
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

    @Override
    public double defenceModifier(Inventory inventory, double enemyAttack) {
        List<String> idToRemove = new ArrayList<>();
        for (CollectableEntity item : inventory.getCollectableItems().values()) {
            if (item instanceof Protection ) {
                enemyAttack -= ((Protection)item).defenceModifier();
                ((CollectableEntity)item).consume();
                if (item.getDurability() == 0) {
                    idToRemove.add(item.getId());
                }
            }
        }

        if (enemyAttack < 0) {
            enemyAttack = 0;
        }

        for (CollectableEntity item : inventory.getCollectableItems().values()) {
            if (item instanceof Armour ) {
                enemyAttack *= ((Armour)item).defenceModifier();
                ((Armour)item).consume();
                if (item.getDurability() == 0) {
                    idToRemove.add(item.getId());
                }
            }
        }

        inventory.removeItem(idToRemove);
        
        return enemyAttack;
    }
}
