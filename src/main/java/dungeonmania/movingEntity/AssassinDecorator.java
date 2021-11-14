package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.movingEntity.States.State;

public class AssassinDecorator extends MercenaryComponent {

    static final int ASSASSIN_HEALTH = 20;
    static final int ASSASSIN_ATTACK = 6;
    protected static final int ONE_RING = 1;

    private MercenaryComponent decorated;

    public AssassinDecorator(MercenaryComponent merc) {
        super(merc.getX(), merc.getY(), merc.getId(), "assassin", new HealthPoint(ASSASSIN_HEALTH), ASSASSIN_ATTACK);
        this.decorated = merc;
    }

    public AssassinDecorator(int x, int y, String id, HealthPoint hp, MovementStrategy defaultMovement, MovementStrategy currentMovement, Boolean isAlly, State state) {
        super(x,y,id,"assassin", hp, ASSASSIN_ATTACK, defaultMovement,currentMovement,isAlly,state);

    }
    
    @Override
    public void interact(World world) throws InvalidActionException {
        if (world.numItemInInventory("one_ring") < ONE_RING) {
            decorated.interact(world);
            world.useByType("one_ring");
        } else {
            throw new InvalidActionException("Need the one ring to bribe Assassin!");
        }
        setInteractable(world.getPlayer());
    }

    @Override 
    public void defend(double attack) {
        if (attack < 0) attack = Math.abs(attack);
        getHealthPoint().loseHealth(attack);
    }
}
