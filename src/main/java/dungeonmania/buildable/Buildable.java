package dungeonmania.buildable;

import dungeonmania.response.models.ItemResponse;

public interface Buildable {
    public void build();

    public ItemResponse getItemResponse();
}
