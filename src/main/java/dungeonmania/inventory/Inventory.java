package dungeonmania.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;

import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;

public class Inventory {
    private Map<String, CollectableEntity> collectableItems = new HashMap<>();
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
     * @return false when there is already a key within the inventory and the item being collected is a key
     * @return true when the item has been collected
     */
    public boolean collect(CollectableEntity item) {
        if (item instanceof Key && numItem("key") != 0) {
            return false;
        }

        item.collect();
        String itemType = item.getType();

        numCollected.putIfAbsent(itemType, 0);
        numCollected.put(itemType, numCollected.get(itemType) + 1);

        collectableItems.put(item.getId(), item);
        return true;
    }

    /**
     * Consumes the item which corresponds with the itemId provided if it exists within the inventory
     * Removes the item if its durability becomes zero after being consumed
     * @param itemId 
     */
    public void use(String itemId) {
        if (collectableItems.containsKey(itemId)) {
            collectableItems.get(itemId).consume();
            if (collectableItems.get(itemId).getDurability() == 0) {
                removeItem(collectableItems.get(itemId));
            }
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
        //consumableItems.remove(idToRemove);
    }
    
    /**
     * Checks if the provided item type is able to be crafted with the items currently within the inventory.
     * If the item is able to be crafted, the crafting material are consumed and the crafted item is added to the inventory
     * @param itemType the type of the item to be built
     * @param itemNum the number following the item's id if it is built successfully
     * @throws InvalidActionException when there are insufficient items within the inventory to build the provided item type
     */
    public void craft(String itemType, String itemNum) {
        if (!isBuildable(itemType)) {
            throw new InvalidActionException("Insufficient items");
        } else if (itemType.equalsIgnoreCase("bow")) {
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

    /**
     * Provides the amount of the specified item type within the inventory
     * @return the amount of the specified item type within the inventory
     */
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

    /**
     * Causes a tick to occur for the item id provided
     * @param itemUsedId the id of the item that is trying to be used
     * @return the collectable that has been used, null otherwise
     * @throws InvalidActionException when the item id provided doesn't correspond to an item within the inventory
     * @throws IllegalArgumentException when the item id provided doesn't correspond to a useable item
     */
    public CollectableEntity tick(String itemUsedId) {
        CollectableEntity collectable = null;
        if (!inInventory(itemUsedId)) {
            throw new InvalidActionException("Item not in Inventory");
        } else if (isUsable(itemUsedId)) {
            collectable = collectableItems.get(itemUsedId).consume();
            if (collectable.getDurability() == 0) {
                removeItem(collectable);
            }
            return collectable;
        } else {
            throw new IllegalArgumentException("Wrong usable type");
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

    /**
     * Checks if the provided item id exists within inventory
     * @param itemUsedId the id of the item to be checked
     * @return true if the item is in the inventory, false otherwise
     */
    public boolean inInventory(String itemUsedId) {
        return collectableItems.containsKey(itemUsedId);
    }

    /**
     * Checks if the provided item id is able to be used
     * @param itemUsedId the id of the item to be checked
     * @return true if the item is useable, false otherwise
     */
    private boolean isUsable(String itemUsedId) {
        
        for ( String item : usable) {
            if (itemUsedId.contains(item)) {
                return true;
            }
        }
        return false;
        
    }

    /**
     * Removes the provided item from the inventory
     * @param item the item to be removed
     */
    private void removeItem(CollectableEntity item) {
        collectableItems.remove(item.getId());
        numCollected.put(item.getType(), numCollected.get(item.getType()) - 1);
    }

    /**
     * Provides the attack provided to the player from the inventory
     * @param playerAttack the players attack before the modification
     * @return the attack of the player after all attack modifications of the inventory have been included
     */
    public double attackModifier(double playerAttack) {
        for (CollectableEntity item : collectableItems.values()) {
            if (item instanceof Sword ) {
                playerAttack += ((Sword)item).attackModifier();
                ((Sword)item).consume();
                if (item.getDurability() == 0) {
                    removeItem(item);
                }
            }
        }

        for (CollectableEntity item : collectableItems.values()) {
            if (item instanceof Bow ) {
                playerAttack *= ((Bow)item).attackModifier();
                ((Bow)item).consume();
                if (item.getDurability() == 0) {
                    removeItem(item);
                }
            }
        }

        return playerAttack;
    }

    /**
     * Provides the modified attack of the enemy when the defense modifier of the inventory has been applied
     * @param enemyAttack the enemy attack before the modification
     * @return the attack of the enemy after all defence modifications of the inventory have been included
     */
    public double defenceModifier(double enemyAttack) {

        for (CollectableEntity item : collectableItems.values()) {
            if (item instanceof Shield ) {
                enemyAttack -= ((Shield)item).defenceModifier();
                ((Shield)item).consume();
                if (item.getDurability() == 0) {
                    removeItem(item);
                }
            }
        }

        if (enemyAttack < 0) {
            enemyAttack = 0;
        }

        for (CollectableEntity item : collectableItems.values()) {
            if (item instanceof Armour ) {
                enemyAttack *= ((Armour)item).defenceModifier();
                ((Armour)item).consume();
                if (item.getDurability() == 0) {
                    removeItem(item);
                }
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

    /**
     * Checks whether the item type provided is buildable
     * @param buildableType the type of the item to be checked
     * @return true if the item is able to be built, false otherwise
     * @throws IllegalArgumentExeption if the type provided isn't able to be built
     */
    public boolean isBuildable(String buildableType) {
        if (buildableType.equalsIgnoreCase("bow")) {
            return numItem("wood") >= 1 && numItem("arrow") >= 3;
        } else if (buildableType.equalsIgnoreCase("shield")) {
            return numItem("wood") >= 2 && (numItem("treasure") >= 1 || numItem("key") == 1);
        } else {
            throw new IllegalArgumentException("Wrong buildable type");
        }
    }

    /**
     * Provides the entities within the inventory in a JSON for game saving
     * @return a JSONArray of the entities within the inventory
     */
    public JSONArray saveGameJson() {
        JSONArray entitiesInInventory = new JSONArray();
        for (CollectableEntity e : collectableItems.values()) {
            entitiesInInventory.put(e.saveGameJson());
        }
        return entitiesInInventory;
    }

    /**
     * Checks if there is a item that has the type which corresponds with provided item type
     * @param itemType the type of the item to be checked
     * @return true if there is a weapon, otherwise false
     */
    public boolean hasItem(String type) {
        for (CollectableEntity e : collectableItems.values()) {
            if (e.getType().equals("type")) {
                return true;
            }
        }
        return false;
    }
}
