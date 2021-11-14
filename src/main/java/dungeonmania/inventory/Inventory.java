package dungeonmania.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dungeonmania.movingEntity.MercenaryComponent;
import org.json.JSONArray;

import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;

public class Inventory {
    private Map<String, CollectableEntity> collectableItems = new HashMap<>();
    private Map<String, Integer> numCollected = new HashMap<>();
    private List<String> usable;
    private List<String> buildables;

    /**
     * Constructor for inventory
     */
    public Inventory() {
        this.usable = Arrays.asList("bomb", "health_potion", "invincibility_potion", "invisibility_potion");
        this.buildables = Arrays.asList("bow", "shield", "sceptre", "midnight_armour");
    }

    /**
     * Add given item to the inventory
     * @param item item that is collected and to be added to inventory
     * @return false when there is already a key within the inventory and the item being collected is a key, true when the item has been collected
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
        collectableItems.get(itemId).consume();
        if (collectableItems.get(itemId).getDurability() == 0) {
            removeItem(collectableItems.get(itemId));
        }
    }

    /**
     * Uses an item of the given type (if it exists)
     * @precondition must check there is at least 1 of this type (by calling numItem)
     * @param type type of the item we want to use
     */
    public void useByType(String type) {

        numCollected.put(type, numCollected.get(type) - 1);
        // remove the first instance in collectable
        List<CollectableEntity> collectable = collectableItems.values()
                .stream()
                .filter(e -> e.getType().equals(type))
                .collect(Collectors.toList());
        String idToRemove =collectable.get(0).getId();
        collectableItems.remove(idToRemove);
    }
    
    /**
     * Use mind control on a MercenaryComponent
     * @param m given MercenaryComponent to be mind controlled
     */
    public void useSceptre(MercenaryComponent m) {
        getSceptre().useMindControl(m);
    }

    /**
     * Overloaded method for when loading from a file
     * @param m given MercenaryComponent that has been mind controlled
     * @param duration remaining duration of the effect
     */
    public void useSceptre(MercenaryComponent m, int duration) {
        getSceptre().useMindControl(m, duration);
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
            IntStream.range(0, 1).mapToObj(i -> "wood").forEach(this::useByType);
            IntStream.range(0, 3).mapToObj(i -> "arrow").forEach(this::useByType);
            collectableItems.put(itemType + itemNum, new Bow(0, 0, itemType + itemNum));
        } else if (itemType.equalsIgnoreCase("shield")) {
            IntStream.range(0, 2).mapToObj(i -> "wood").forEach(this::useByType);
            if (numItem("sun_stone") != 0) {
                useByType("sun_stone");
            } else if (numItem("treasure") != 0) {
                useByType("treasure");
            } else {
                useByType("key");
            }
            collectableItems.put(itemType + itemNum, new Shield(0, 0, itemType + itemNum));
        } else if (itemType.equalsIgnoreCase("midnight_armour")) {
            useByType("armour");
            useByType("sun_stone");
            collectableItems.put(itemType + itemNum, new MidnightArmour(0, 0, itemType + itemNum));
        } else if (itemType.equalsIgnoreCase("sceptre")) {
            if (numItem("wood") != 0) {
                useByType("wood");
            } else {
                IntStream.range(0, 2).mapToObj(i -> "arrow").forEach(this::useByType);
            }

            if (numItem("sun_stone") >= 2) {
                useByType("sun_stone");
            } else if (numItem("treasure") != 0) {
                useByType("treasure");
            } else {
                useByType("key");
            }

            useByType("sun_stone");
            collectableItems.put(itemType + itemNum, new Sceptre(0, 0, itemType + itemNum));
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
        return collectableItems.values().stream().anyMatch(e -> e instanceof Weapon);
    }

    public Sceptre getSceptre() {
        for (CollectableEntity c : collectableItems.values()) {
            if (c instanceof Sceptre) return (Sceptre) c;
        }
        return null;
    }

    /**
     * Tick the duration of the sceptre effects and cool down
     */
    public void tickSceptre() {
        getSceptre().consume();
    }

    public Bomb getBomb(String id) {
        for (CollectableEntity c : collectableItems.values()) {
            if (c.getId().equals(id)) {
                return (Bomb) c;
            }
        }
        return null;
    }

    public List<ItemResponse> getInventoryResponse() {
        return collectableItems.values().stream().map(CollectableEntity::getItemResponse).collect(Collectors.toList());
    }

    /**
     * Causes a tick to occur for the item id provided
     * @param itemUsedId the id of the item that is trying to be used
     * @return the collectable that has been used, null otherwise
     * @throws InvalidActionException when the item id provided doesn't correspond to an item within the inventory
     * @throws IllegalArgumentException when the item id provided doesn't correspond to a useable item
     */
    public CollectableEntity tick(String itemUsedId) {
        CollectableEntity collectable;
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

    public Map<String, CollectableEntity> getCollectableItems() {
        return this.collectableItems;
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
        return usable.stream().anyMatch(itemUsedId::contains);
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
     * Removes items with the id corresponding to the list provided, from the inventory
     * @param itemIdToRemove the list of item ids to be removed
     */
    public void removeItem(List<String> itemIdToRemove) {
        for (String itemId : itemIdToRemove) {
            String type = collectableItems.get(itemId).getType();
            numCollected.put(type, numCollected.get(type) - 1);
            collectableItems.remove(itemId);
        }
    }

    public List<String> getBuildable() {
        ArrayList<String> buildable = new ArrayList<>();
        for (String item : buildables) {
            if (isBuildable(item)) buildable.add(item);
        }
        return buildable;
    }

    /**
     * Checks whether the item type provided is buildable
     * @param buildableType the type of the item to be checked
     * @return true if the item is able to be built, false otherwise
     * @throws IllegalArgumentExeption if the type provided isn't able to be built
     */
    public boolean isBuildable(String buildableType) {

        if (hasItem(buildableType)) {
            return false;
        } else if (buildableType.equalsIgnoreCase("bow")) {
            return numItem("wood") >= 1 && numItem("arrow") >= 3;
        } else if (buildableType.equalsIgnoreCase("shield")) {
            if (numItem("wood") < 2) return false;
            return numItem("key") >= 1 || (numItem("treasure") >= 1 || numItem("sun_stone") >= 1);
        } else if (buildableType.equalsIgnoreCase("sceptre")) {
            if (numItem("wood") < 1 && numItem("arrow") < 2) return false;
            if (numItem("key") < 1 && numItem("treasure") < 1) return (numItem("sun_stone") >= 2);
            return (numItem("sun_stone") >= 1);
        } else if (buildableType.equalsIgnoreCase("midnight_armour")) {;
            return (numItem("armour") >= 1 && numItem("sun_stone") >= 1);
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
    public boolean hasItem(String itemType) {
        return collectableItems.values().stream().anyMatch(e -> e.getType().equals(itemType));
    }

    /**
     * Check if the sceptre is ready to be used (off cool down)
     * @return true if sceptre is ready to be used
     */
    public boolean usableSceptre() {
        return getSceptre().ready();
    }
}
