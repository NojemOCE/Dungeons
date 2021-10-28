package dungeonmania.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dungeonmania.Consumable;
import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.buildable.*;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.*;

public class Inventory {
    private Map<String, CollectableEntity> collectableItems;
    private Map<String, Buildable> buildableItems;
    private Map<String, Integer> numCollected;
    private List<String> usable;
    private Player player;

    public Inventory(Player player) {
        this.player = player;
        this.usable.add("bomb");
        this.usable.add("health_potion");
        this.usable.add("invincibility_potion");
        this.usable.add("invisibility_potion");
    }

    public void collect(CollectableEntity item) {
        String itemType = item.getItemId();

        numCollected.putIfAbsent(itemType, 0);
        numCollected.put(itemType, numCollected.get(itemType) + 1);

        collectableItems.put(item.getItemId(), item);
    }

    public void use(String itemId) {
        String itemType = collectableItems.remove(itemId).getItemId();
        numCollected.put(itemType, numCollected.get(itemType) - 1);
    }

    public void craft(String itemType) {
        if (!isBuildable(itemType)) {
            throw new InvalidActionException("Insufficient items");
        } else if (itemType.equalsIgnoreCase("bow")) {
            use("wood");
            IntStream.range(0, 3).mapToObj(i -> "arrow").forEach(this::use);
        } else if (itemType.equalsIgnoreCase("shield")) {
            IntStream.range(0, 2).mapToObj(i -> "wood").forEach(this::use);
            use(numItem("treasure") != 0 ? "treasure" : "key");
        }

    }

    public int numItem(String itemType) {
        numCollected.putIfAbsent(itemType, 0);
        return numCollected.get(itemType);
    }

    public Key keyInInventory(String keyColour) {
        List<Key> keys = collectableItems.values().stream()
                                        .filter(x -> x instanceof Key)
                                        .map(Key.class::cast)
                                        .filter(x -> x.getKeyColour().equals(keyColour))
                                        .collect(Collectors.toList());

        if (keys.isEmpty()) {
            return null;
        } else {
            return keys.get(0);
        }
    }

    public List<ItemResponse> getInventoryResponse() {
        List<ItemResponse> itemResponses = collectableItems.values().stream().map(CollectableEntity::getItemResponse).collect(Collectors.toList());
        buildableItems.values().stream().map(Buildable::getItemResponse).forEach(itemResponses::add);

        return itemResponses;
    }

    public List<String> tick(String itemUsedId) {
        if (!inInventory(itemUsedId)) {
            throw new InvalidActionException("Item not in Inventory");
        } else if (!isUsable(itemUsedId)) {
            Consumable c = (Consumable) collectableItems.get(itemUsedId);
            c.consume();
            collectableItems.forEach((id, item) -> {
                item.tick();
            });
        } else {
            throw new IllegalArgumentException("Wrong usable type");
        }

        return getBuildable();
    }

    public List<String> tick(Direction movementDirection) {
        collectableItems.forEach((id, item) -> {
            item.updatePosition(player.getPosition());
            item.tick();
        });

        return getBuildable();
    }

    public boolean inInventory(String itemUsedId) {
        return collectableItems.containsKey(itemUsedId);
    }

    public boolean isUsable(String itemUsedId) {
        
        for ( String item : usable) {
            if (itemUsedId.contains(item)) {
                return true;
            }
        }
        return false;
        
    }

    public void removeItem(String collectible) {
        collectableItems.remove(collectible);
    }

    public double attackModifier(double playerAttack) {
        for (CollectableEntity item : collectableItems.values()) {
            if (item instanceof Sword ) {
                playerAttack += ((Sword)item).attackModifier();
                ((Sword)item).consume();
            }
        }

        for (Buildable item : buildableItems.values()) {
            if (item instanceof Bow ) {
                playerAttack *= ((Bow)item).attackModifier();
                ((Bow)item).consume();
            }
        }

        return playerAttack;
    }

    public double defenceModifier(double enemyAttack) {

        for (Buildable item : buildableItems.values()) {
            if (item instanceof Shield ) {
                enemyAttack -= ((Shield)item).defenceModifier();
                ((Shield)item).consume();
            }
        }

        if (enemyAttack < 0) {
            enemyAttack = 0;
        }

        for (CollectableEntity item : collectableItems.values()) {
            if (item instanceof Armour ) {
                enemyAttack *= ((Armour)item).defenceModifier();
                ((Armour)item).consume();
            }
        }

        return enemyAttack;
    }
    public List<String> getBuildable() {
        ArrayList<String> buildable = new ArrayList<>();

        if (isBuildable("bow")) buildable.add("bow");
        if (isBuildable("shield")) buildable.add("shield");

        return buildable;
    }

    public boolean isBuildable(String buildableType) {
        if (buildableType.equalsIgnoreCase("bow")) {
            return numItem("wood") >= 1 && numItem("arrow") >= 3;
        } else if (buildableType.equalsIgnoreCase("shield")) {
            return numItem("wood") >= 2 && (numItem("treasure") >= 1 || numItem("key") >= 1);
        } else {
            throw new IllegalArgumentException("Wrong buildable type");
        }
    }
}
