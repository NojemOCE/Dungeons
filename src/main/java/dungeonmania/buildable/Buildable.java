package dungeonmania.buildable;

import org.json.JSONObject;

import dungeonmania.response.models.ItemResponse;

public interface Buildable {
    public void build();

    public ItemResponse getItemResponse();
    public String getItemId();

    /**
     * Creates a JSON containing entity state required to save/load game
     * @return JSON object containing important variables
     */
    public abstract JSONObject saveGameJson();
}
