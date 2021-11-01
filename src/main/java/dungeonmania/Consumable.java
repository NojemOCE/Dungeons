package dungeonmania;

import dungeonmania.collectable.CollectableEntity;

public interface Consumable {
    /**
     * consumes the consumable
     * @return the collectable entity that was consumed
     */ 
    public CollectableEntity consume();
}
