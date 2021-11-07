package dungeonmania.collectable;

import dungeonmania.CraftingMaterial;

public class Wood extends CollectableEntity implements CraftingMaterial {

    public Wood(int x, int y, String itemId) {
        super(x, y, itemId, "wood");
    }

    public Wood(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public Wood(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    public void craft() {
        decreaseDurability();
    }
}
