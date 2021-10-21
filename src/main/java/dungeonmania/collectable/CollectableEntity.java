package dungeonmania.collectable;

public abstract class CollectableEntity {
    private boolean collected;

    public void collect() {
        this.collected = true;
    };
    public boolean isCollected() {return collected;};
}
