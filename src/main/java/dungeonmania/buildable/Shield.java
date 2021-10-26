package dungeonmania.buildable;

import dungeonmania.Consumable;
import dungeonmania.response.models.ItemResponse;

public class Shield implements Buildable, Consumable {

    private String type = "shield";
    private String itemId;
    private int durability;
    private boolean built = false;
    private final int DURABILITY = 10;
    private final int DEFENCE = 10;

    public Shield(String itemId) {
        this.itemId = itemId;
    };

    public void build() {
        this.durability = DURABILITY;
        this.built = true;
    };

    public int defenceModifer() {
        return DEFENCE;
    };

    public void consume() {
        this.durability--;
        if (this.durability == 0) {
            this.built = false;
        }
    };

    public boolean isBuilt() {
        return this.built;
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(itemId, type);
    }
}
