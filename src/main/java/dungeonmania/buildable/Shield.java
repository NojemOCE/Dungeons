package dungeonmania.buildable;

import dungeonmania.response.models.ItemResponse;

public class Shield implements Buildable {
    public Shield() {};
    public void build() {};
    public void defend() {};

    @Override
    public ItemResponse getItemResponse() {
        // TODO Update for valid ID
        return new ItemResponse("not a valid ID", "shield");
    };
}
