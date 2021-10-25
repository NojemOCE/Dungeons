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

public class Inventory {
    private List<CollectableEntity> collectableItems;
    private List<Buildable> buildableItems;
    private Map<String, Integer> collected;

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
        collectableItems.removeIf(c -> itemType.equals(c.getClass().getSimpleName()));

        collected.put(itemType, collected.get(itemType) - 1);
    }

    // This signature doesn't make sense - making bow before we know its possible
    public void craft(Buildable item) {
        if (item instanceof Bow) {
            if (numItem("wood") < 1 || numItem("arrow") < 3) {
                throw new InvalidActionException("Insufficient items");
            }
        } else if (item instanceof Shield) {
            if (numItem("wood") < 2 || numItem("treasure") < 1 || numItem("key") < 1) {
                throw new InvalidActionException("Insufficient items");
            }
        } else {
            throw new IllegalArgumentException("Wrong buildable type");
        }
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
}
