package dungeonmania.buildable;

import dungeonmania.Consumable;
import dungeonmania.response.models.ItemResponse;

public class Bow implements Buildable, Consumable {
    public Bow() {};
    public void build() {};
    public void attack() {};
    public void consume() {}

    @Override
    public ItemResponse getItemResponse() {
        // TODO Update for valid ID
        return new ItemResponse("not a valid ID", "bow");
    };

}
