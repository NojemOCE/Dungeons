package dungeonmania.collectable;

import dungeonmania.Passive;
import dungeonmania.movingEntity.Player;

public class InvisibilityPotion extends CollectableEntity implements Passive {

    private final int DURATION = 10;
    private int duration = DURATION;

    /**
     * Constructor for invisibility potion taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of invisibility potion
     * @param y y coordinate of invisibility potion
     * @param itemId unique item id of invisibility potion
     */
    public InvisibilityPotion(int x, int y, String itemId) {
        super(x, y, itemId, "invisibility_potion");
    }

    /**
     * Constructor for invisibility potion taking its unique itemID and durability
     * @param itemId unique item id of invisibility potion
     * @param durability integer value of durability
     */
    public InvisibilityPotion(String itemId, int durability) {
        this(0, 0, itemId);
        setDurability(durability);
    }

    /**
     * Constructor for invisibility potion taking an x and a y coordinate, its unique itemID, durability and duration
     * @param x x coordinate of invisibility potion
     * @param y y coordinate of invisibility potion
     * @param itemId unique item id of invisibility potion
     * @param durability integer value of durability
     * @param duration integer value of duration
     */
    public InvisibilityPotion(int x, int y, String itemId, int durability, int duration) {
        this(x, y, itemId);
        setDurability(durability);
        this.duration = duration;
    }

    /**
     * Constructor for invisibility potion taking in a duration
     * @param duration duration of invisibility potion
     */
    public InvisibilityPotion(int duration) {
        this(0,0, null);
        this.duration = duration;
    }
    
    @Override
    public void applyPassive(Player player) {
        if (this.duration == 0) {
            player.notifyPassive("N/A");
        } else {
            player.notifyPassive(getType());
        }
    }

    @Override
    public void decreaseDuration() {
        duration--;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public CollectableEntity consume() {
        decreaseDurability();
        return this;
    }

    

}
