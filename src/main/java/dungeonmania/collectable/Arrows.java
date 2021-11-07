package dungeonmania.collectable;

import dungeonmania.CraftingMaterial;

public class Arrows extends CollectableEntity implements CraftingMaterial {

    public Arrows(int x, int y, String itemId) {
        super(x, y, itemId, "arrow");

    }

    public Arrows(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }
    
    public Arrows(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    public void craft() {
        decreaseDurability();
    }
}
