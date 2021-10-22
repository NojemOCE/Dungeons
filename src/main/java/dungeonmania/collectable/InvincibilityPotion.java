package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity implements Consumable {
    public InvincibilityPotion(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    private int duration;

    public void consume() {};

    public void tick() {};

    public void invincibility() {};
}
