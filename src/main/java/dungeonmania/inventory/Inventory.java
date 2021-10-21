package dungeonmania.inventory;

import java.util.List;
import dungeonmania.collectable.*;
import dungeonmania.buildable.*;

public class Inventory {
    
    private List<CollectableEntities> collectableItems;
    private List<Buildable> buildableItems;

    public void collect(CollectableEntities item) {
        return;
    }

    public void use(CollectableEntities item) {
        return;
    }

    public void craft(Buildable item) {
        return;
    }

    public boolean isPresent(CollectableEntities item) {
        return true;
    }

    public boolean isPresent(Buildable item) {
        return true;
    }
}
