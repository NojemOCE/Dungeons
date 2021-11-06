package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {

    public Assassin(int x, int y, String id) {
        super(x, y, id);
        setAttackDamage(12);
    }

    public Assassin(int x, int y, String id, HealthPoint hp, String defaultMovement, String currentMovement,
            Boolean isAlly) {
        super(x, y, id, hp, defaultMovement, currentMovement, isAlly);
    }
    
    /**
     *  
     * The character can bribe a mercenary if they are within 2 cardinal tiles
     * to the mercenary. Player requires minimum amount of gold to bribe.
     * @param world
     */
    @Override
    public void interact(World world) throws InvalidActionException {
        if (getInteractable()) {
            if (world.numItemInInventory("treasure") >= GOLD_TO_BRIBE) {
                for (int i = 0; i < GOLD_TO_BRIBE; i++) {
                    world.useByType("treasure");
                }
                setAlly(true);
                world.setBattle(null);
                setSpeed(0);
            } else {
                throw new InvalidActionException("Not enough gold to bribe Assassin!");
            }
        } else {
            throw new InvalidActionException("Must be within 2 cardinal tiles to bribe Assassin!");
        }
    }
}
