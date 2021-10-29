package dungeonmania.collectable;



public class Arrows extends CollectableEntity {

    public Arrows(int x, int y, String itemId) {
        super(x, y, itemId, "arrow", null);
    }

    public void consume() {
        getInventory().removeItem(getItemId());
    };

}
