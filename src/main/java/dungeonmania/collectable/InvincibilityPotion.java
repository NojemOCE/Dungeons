package dungeonmania.collectable;

public class InvincibilityPotion extends CollectableEntity {

    private final int DURATION = 10;
    private int duration = DURATION;

    public InvincibilityPotion(int x, int y, String itemId) {
        super(x, y, itemId, "invincibility_potion");
    }

    public void decreaseDuration() {
        duration--;
    }

    public int getDuration() {
        return this.duration;
    }

}
