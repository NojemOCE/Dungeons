package dungeonmania.buildable;

import dungeonmania.Consumable;
import dungeonmania.response.models.ItemResponse;

public class Bow implements Buildable, Consumable {

    private String itemId;
    private int durability;
    private boolean built = false;
    private final int DURABILITY = 10;
    private final int ATTACK_MULTIPLIER = 2;

    public Bow(String itemId) {

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

    public int attack() {
        if (built) {
            consume();
            return ATTACK_MULTIPLIER;
        }
        return 1;
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(itemId, "bow");
    };

}
