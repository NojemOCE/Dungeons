package dungeonmania.collectable;

import org.json.JSONObject;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;

public class Key extends CollectableEntity {
    private String keyColour;

    public Key(int x, int y, String itemId, String keyColour) {
        super(x, y, itemId, "key");
        this.keyColour = keyColour;
    }

    public String getKeyColour() {
        return keyColour;
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject keyJSON  = super.saveGameJson();
        keyJSON.put("key", keyColour);
        return keyJSON;
    }

}
