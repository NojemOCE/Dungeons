package dungeonmania.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;

import dungeonmania.Consumable;
import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.buildable.*;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.*;

public class Inventory {
    private Map<String, CollectableEntity> collectableItems = new HashMap<>();
    private Map<String, Buildable> buildableItems = new HashMap<>();
    private Map<String, Integer> numCollected = new HashMap<>();
    private List<String> usable = new ArrayList<>();

    public Inventory() {
        this.usable.add("bomb");
        this.usable.add("health_potion");
        this.usable.add("invincibility_potion");
        this.usable.add("invisibility_potion");
    }

    /**
     * Add given item to the inventory
     * @param item item that is collected and to be added to inventory
     */
    public void collect(CollectableEntity item) {
        String itemType = item.getId();

        numCollected.putIfAbsent(itemType, 0);
        numCollected.put(itemType, numCollected.get(itemType) + 1);

        collectableItems.put(item.getId(), item);
    }

    public void use(String itemId) {
        String itemType = collectableItems.remove(itemId).getId();
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

    /**
     * Checks for a key with a specified key colour in the inventory
     * @param keyColour key colour to search for
     * @return the Key if found, else null
     */
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

    /**
     * Checks if there is a weapon in the inventory
     * @return true if there is a weapon, otherwise false
     */
    public boolean hasWeapon() {
        for (CollectableEntity e : collectableItems.values()) {
            if (e instanceof Sword) {
                return true;
            }
        }
        return false;
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
            // item.updatePosition(player.getPosition());
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

    public JSONArray saveGameJson() {
        JSONArray entitiesInInventory = new JSONArray();
        for (CollectableEntity e : collectableItems.values()) {
            entitiesInInventory.put(e.saveGameJson());
        }
        for (Buildable b : buildableItems.values()) {
            entitiesInInventory.put(b.saveGameJson());
        }
        return entitiesInInventory;
    }
}
