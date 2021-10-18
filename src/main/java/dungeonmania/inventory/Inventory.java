public class Inventory {
    
    private List<CollectableEntities> collectableItems;
    private List<Buildables> collectableItems;

    public void collect(CollectableEntities item) {
        return;
    }

    public void use(CollectableEntities item) {
        return;
    }

    public void craft(Buildables item) {
        return;
    }

    public boolean isPresent(CollectableEntities item) {
        return true;
    }

    public boolean isPresent(Buildables item) {
        return true;
    }
}
