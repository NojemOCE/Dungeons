package dungeonmania.collectable;

import dungeonmania.CraftingMaterial;

public class SunStone extends CollectableEntity implements CraftingMaterial {
    public SunStone(int x, int y, String itemId) {
        super(x, y, itemId, "sun_stone");
    }

    public SunStone(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public SunStone(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    public void craft() {
        decreaseDurability();
    }

    @Override
    public CollectableEntity consume() {return null;}
}
