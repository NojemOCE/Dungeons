package dungeonmania.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.buildable.*;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.*;

public class Inventory {
    // Change to hashmap<string, collectableentity> with id as key
    private Map<String, CollectableEntity> collectableItems;
    private Map<String, Buildable> buildableItems;
    private Map<String, Integer> collected;
    private List<String> useables;
    private Player player;

    public Inventory(Player player) {
        this.player = player;
        this.useables.add("bomb");
        this.useables.add("health_potion");
        this.useables.add("invincibility_potion");
        this.useables.add("invisibility_potion");
    }

    public void collect(CollectableEntity item) {
        String itemType = item.getClass().getSimpleName();
        collected.putIfAbsent(itemType, 0);
        collected.put(itemType, collected.get(itemType) + 1);

        collectableItems.add(item);
    }

    public void use(CollectableEntity item) {
        collectableItems.remove(item);
    }

    public void use(String itemType) {
        collectableItems.removeIf(c -> itemType.equalsIgnoreCase(c.getClass().getSimpleName()));

        collected.put(itemType, collected.get(itemType) - 1);
    }


    public void craft(String itemType) {
        if (itemType.equalsIgnoreCase("bow")) {
            if (numItem("wood") < 1 || numItem("arrow") < 3) {
                throw new InvalidActionException("Insufficient items");
            }
            IntStream.range(0, 1).mapToObj(i -> "wood").forEach(this::use);
            IntStream.range(0, 3).mapToObj(i -> "arrow").forEach(this::use);
        } else if (itemType.equalsIgnoreCase("shield")) {
            if (numItem("wood") < 2 || numItem("treasure") < 1 || numItem("key") < 1) {
                throw new InvalidActionException("Insufficient items");
            }
            IntStream.range(0, 2).mapToObj(i -> "wood").forEach(this::use);
            use(numItem("treasure") != 0 ? "treasure" : "key");

        } else {
            throw new IllegalArgumentException("Wrong buildable type");
        }

        buildableItems.add(BuildableFactory.getBuildable(itemType));
    }

    public int numItem(String itemType) {
        collected.putIfAbsent(itemType, 0);
        return collected.get(itemType);
    }

    // Can we combine these two? Lose type safety if we do Object
    // Can get rid of it all together
    public boolean isPresent(CollectableEntity item) {
        return collected.get(item.getClass().getSimpleName()) != null;
    }

    public boolean isPresent(Buildable item) {
        return collected.get(item.getClass().getSimpleName()) != null;
    }

    public Key keyInInventory(String keyColour) {
        List<Key> keys = collectableItems.stream()
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
        List<ItemResponse> itemResponses = new ArrayList<>();
        
        itemResponses.addAll(collectableItems.stream().map(CollectableEntity::getItemResponse).collect(Collectors.toList()));
        itemResponses.addAll(buildableItems.stream().map(Buildable::getItemResponse).collect(Collectors.toList()));
        return itemResponses;
    }

    public Buildable tick(String itemUsedId) {
        if (!inInventory(itemUsedId)) {
            throw new InvalidActionException("Item not in Inventory");
        } else if (!isUseable(itemUsedId)) {
        
        }

        collectableItems.forEach((id, item) -> {
            item.tick();
        });
    }

    public Buildable tick(Direction movementDirection) {
        collectableItems.forEach((id, item) -> {
            item.updatePosition(player.getPosition());
            item.tick();
        });
    }

    public boolean inInventory(String itemUsedId) {
        return collectableItems.containsKey(itemUsedId);
    }

    public boolean isUseable(String itemUsedId) {
        
        for ( String item : useables) {
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
}
