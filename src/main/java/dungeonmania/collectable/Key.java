package dungeonmania.collectable;

import org.json.JSONObject;

import dungeonmania.response.models.EntityResponse;

public class Key extends CollectableEntity {
    private int keyColour;

    /**
     * Constructor for key taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of key
     * @param y y coordinate of key
     * @param itemId unique item id of key
     * @param keyColour key colour of key
     */
    public Key(int x, int y, String itemId, int keyColour) {
        super(x, y, itemId, "key");
        this.keyColour = keyColour;
    }

    /**
     * Constructor for key taking its unique itemID and durability
     * @param itemId unique item id of key
     * @param keyColour key colour of key
     * @param durability integer value of durability
     */
    public Key(String itemId, int keyColour, int durability) {
        this(0, 0, itemId, keyColour, durability);
    }

    /**
     * Constructor for key taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of key
     * @param y y coordinate of key
     * @param itemId unique item id of key
     * @param keyColour key colour of key
     * @param durability integer value of durability
     */
    public Key(int x, int y, String itemId, int keyColour, int durability) {
        this(x, y, itemId, keyColour);
        setDurability(durability);
    }

    /**
     * Gets the key colour
     * @return int indicating key colour
     */
    public int getKeyColour() {
        return keyColour;
    }

    @Override
    public EntityResponse getEntityResponse() {
        if (keyColour % 2 == 0) {
            return new EntityResponse(getId(), "key2", getPosition(), false);
        } else {
            return super.getEntityResponse();
        }
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject keyJSON  = super.saveGameJson();
        keyJSON.put("key", keyColour);
        return keyJSON;
    }

}
