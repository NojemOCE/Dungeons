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

public class Inventory {
    private Map<String, CollectableEntity> collectableItems = new HashMap<>();
    private Map<String, Consumable> consumableItems = new HashMap<>();
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
    public boolean collect(CollectableEntity item) {
        if (item instanceof Key && numItem("key") != 0) {
            //System.out.println("hi");
            return false;
        }

        //System.out.println(numItem("key"));
        item.collect();
        String itemType = item.getType();

        numCollected.putIfAbsent(itemType, 0);
        numCollected.put(itemType, numCollected.get(itemType) + 1);

        collectableItems.put(item.getId(), item);
        consumableItems.put(item.getId(), item);
        return true;
    }

    public void use(String itemId) {
        if (consumableItems.containsKey(itemId)) {
            consumableItems.get(itemId).consume();
        }
    }

    /**
     * Uses an item of the given type (if it exists)
     * @precondition must check there is at least 1 of this type (by calling numItem)
     * @param type type of the item we want to use
     */
    public void useByType(String type) {
        // if it doesn't exist we can't use it
        if (numCollected.get(type).equals(null)) {
            return;
        }
        numCollected.put(type, numCollected.get(type) - 1);
        // remove the first instance in collectable and consumable
        List<CollectableEntity> collectable = collectableItems.values()
                                                              .stream()
                                                              .filter(e -> e.getType().equals(type))
                                                              .collect(Collectors.toList());
        String idToRemove =collectable.get(0).getId();
        collectableItems.remove(idToRemove);
        consumableItems.remove(idToRemove);
    }
    
    public void craft(String itemType, String itemNum) {
        if (!isBuildable(itemType)) {
            throw new InvalidActionException("Insufficient items");
        } else if (itemType.equalsIgnoreCase("bow")) {
            System.out.println("x");
            useByType("wood");
            IntStream.range(0, 3).mapToObj(i -> "arrow").forEach(this::useByType);
            collectableItems.put(itemType + itemNum, new Bow(0, 0, itemType + itemNum));
        } else if (itemType.equalsIgnoreCase("shield")) {
            IntStream.range(0, 2).mapToObj(i -> "wood").forEach(this::useByType);
            useByType(numItem("treasure") != 0 ? "treasure" : "key");
            collectableItems.put(itemType + itemNum, new Shield(0, 0, itemType + itemNum));
        }
        numCollected.putIfAbsent(itemType, 0);
        numCollected.put(itemType, numCollected.get(itemType) + 1);
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
    public Key keyInInventory(int keyColour) {
        List<Key> keys = collectableItems.values().stream()
                                        .filter(x -> x instanceof Key)
                                        .map(Key.class::cast)
                                        .filter(x -> x.getKeyColour() == keyColour)
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
        return collectableItems.values().stream().anyMatch(e -> e instanceof Sword);
    }

    public List<ItemResponse> getInventoryResponse() {
        List<ItemResponse> itemResponses = collectableItems.values().stream().map(CollectableEntity::getItemResponse).collect(Collectors.toList());
        return itemResponses;
    }

    public CollectableEntity tick(String itemUsedId) {
        CollectableEntity collectable = null;
        if (!inInventory(itemUsedId)) {
            throw new InvalidActionException("Item not in Inventory");
        } else if (isUsable(itemUsedId)) {
            collectable = collectableItems.get(itemUsedId).consume();
            tick();
        } else {
            throw new IllegalArgumentException("Wrong usable type");
        }

        return collectable;
    }

    public void tick() {
        for (CollectableEntity item : collectableItems.values()) {
            item.tick();
            if (item.getDurability() == 0) {
                removeItem(item);
            }
        }
    }

    /**
     * Get the type of the given item
     * @param itemStringId the id of the item
     * @return the type of the item, otherwise null
     */
    public String getType(String itemStringId) {
        if (collectableItems.containsKey(itemStringId)) {
            return collectableItems.get(itemStringId).getType();
        }
        return null;
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

    public void removeItem(CollectableEntity item) {
        consumableItems.remove(item.getId());
        collectableItems.remove(item.getId());
        numCollected.put(item.getType(), numCollected.get(item.getType()) - 1);
    }

    public double attackModifier(double playerAttack) {
        for (CollectableEntity item : collectableItems.values()) {
            if (item instanceof Sword ) {
                playerAttack += ((Sword)item).attackModifier();
                ((Sword)item).consume();
            }
        }

        for (CollectableEntity item : collectableItems.values()) {
            if (item instanceof Bow ) {
                playerAttack *= ((Bow)item).attackModifier();
                ((Bow)item).consume();
            }
        }

        return playerAttack;
    }

    public double defenceModifier(double enemyAttack) {

        for (CollectableEntity item : collectableItems.values()) {
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
            return numItem("wood") >= 2 && (numItem("treasure") >= 1 || numItem("key") == 1);
        } else {
            throw new IllegalArgumentException("Wrong buildable type");
        }
    }

    public JSONArray saveGameJson() {
        JSONArray entitiesInInventory = new JSONArray();
        for (CollectableEntity e : collectableItems.values()) {
            entitiesInInventory.put(e.saveGameJson());
        }
        return entitiesInInventory;
    }
}
