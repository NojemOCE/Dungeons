package dungeonmania.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.collectable.*;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.buildable.*;

public class Inventory {
    
    private List<CollectableEntity> collectableItems;
    private List<Buildable> buildableItems;

    public void collect(CollectableEntity item) {
        return;
    }

    public void use(CollectableEntity item) {
        return;
    }

    public void craft(Buildable item) {
        return;
    }

    public boolean isPresent(CollectableEntity item) {
        return true;
    }

    public boolean isPresent(Buildable item) {
        return true;
    }

    public List<ItemResponse> getInventoryResponse() {
        List<ItemResponse> itemResponses = new ArrayList<>();
        
        itemResponses.addAll(collectableItems.stream().map(CollectableEntity::getItemResponse).collect(Collectors.toList()));
        itemResponses.addAll(buildableItems.stream().map(Buildable::getItemResponse).collect(Collectors.toList()));
        return itemResponses;
    }
}
