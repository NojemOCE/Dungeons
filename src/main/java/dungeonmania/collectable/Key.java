package dungeonmania.collectable;

import org.json.JSONObject;

public class Key extends CollectableEntity {
    private int keyColour;

    public Key(int x, int y, String itemId, int keyColour) {
        super(x, y, itemId, "key");
        this.keyColour = keyColour;
    }

    public Key(String itemId, int keyColour, int durability) {
        this(0, 0, itemId, keyColour, durability);
    }

    public Key(int x, int y, String itemId, int keyColour, int durability) {
        this(x, y, itemId, keyColour);
        setDurability(durability);
    }

    public int getKeyColour() {
        return keyColour;
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject keyJSON  = super.saveGameJson();
        keyJSON.put("key", keyColour);
        return keyJSON;
    }

}
