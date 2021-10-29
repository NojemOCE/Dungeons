package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.buildable.Buildable;
import dungeonmania.response.models.ItemResponse;

public class Bow extends CollectableEntity implements Consumable {

    private String type = "bow";
    private String itemId;
    private int durability;
    private boolean built = false;
    private final int DURABILITY = 10;
    private final int ATTACK_MULTIPLIER = 2;

    public Bow(String itemId) {
        this.itemId = itemId;
    }

    public void build() {
        this.durability = DURABILITY;
        this.built = true;
    }

    public void consume() {
        this.durability--;
        if (this.durability == 0) {
            this.built = false;
        }
    }

    public int attackModifier() {
        if (built) {
            return ATTACK_MULTIPLIER;
        }
        return 1;
    }

    public String getType() {
        return type;
    }

    public String getItemId() {
        return itemId;
    }

    public boolean isBuilt() {
        return this.built;
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(itemId, type);
    };

}
