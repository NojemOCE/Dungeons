package dungeonmania.inventory;

import java.util.List;
import dungeonmania.collectable.*;
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
}
