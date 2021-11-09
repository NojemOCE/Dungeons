package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.exceptions.InvalidActionException;

public class AssassinDecorator extends MercenaryComponent {

    static final int ASSASSIN_HEALTH = 20;
    protected static final int ONE_RING = 1;

    private MercenaryComponent decorated;

    public AssassinDecorator(MercenaryComponent merc) {
        super(merc.getX(), merc.getY(), merc.getId(), "assassin", new HealthPoint(ASSASSIN_HEALTH), merc.getAttackDamage() * 1.5);
        this.decorated = merc;
    }

    @Override
    public void interact(World world) throws InvalidActionException {
        if (world.numItemInInventory("one_ring") < ONE_RING) {
            decorated.interact(world);
            world.useByType("one_ring");
        } else {
            throw new InvalidActionException("Need the one ring to bribe Assassin!");

        }
    }

    @Override
    public void moveEntity(World world) {
        // TODO Auto-generated method stub
        
    }
}
