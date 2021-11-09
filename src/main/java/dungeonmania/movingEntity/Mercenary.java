package dungeonmania.movingEntity;

import dungeonmania.util.*;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.movingEntity.MovementStrategies.FollowPlayer;

public class Mercenary extends MercenaryComponent {

    static final int MERC_ATTACK = 3;
    static final int MERC_HEALTH = 15;
    protected static final int GOLD_TO_BRIBE = 1;

    /**
     * Constructor for Mercenary taking an x coordinate, and y coordinate and an id
     * @param x x coordinate of the mercenary
     * @param y y coordinate of the mercenary
     * @param id unique entity id of the mercenary
     */
    public Mercenary(int x, int y, String id) {
        super(x, y, id, "mercenary", new HealthPoint(MERC_HEALTH), MERC_ATTACK);
        setMovement(new FollowPlayer());
        setDefaultMovementStrategy(new FollowPlayer());
        setAlly(false);
    }

    public Mercenary(int x, int y, String id, HealthPoint hp, String defaultMovement, String currentMovement, Boolean isAlly) {
        super(x, y, id, "mercenary", hp, MERC_ATTACK);
        setMovement(getMovementFromString(currentMovement));
        setDefaultMovementStrategy(getMovementFromString(defaultMovement));
        setAlly(isAlly);
    }

    /**
     *  
     * The character can bribe a mercenary if they are within 2 cardinal tiles
     * to the mercenary. Player requires minimum amount of gold to bribe.
     * @param world
     */
    public void interact(World world) throws InvalidActionException {
        if (getInteractable()) {
            if (world.numItemInInventory("treasure") >= GOLD_TO_BRIBE) {
                for (int i = 0; i < GOLD_TO_BRIBE; i++) {
                    world.useByType("treasure");
                }
                setAlly(true);
                world.setBattle(null);
            } else {
                throw new InvalidActionException("Not enough gold to bribe Mercenary!");
            }
        } else {
            throw new InvalidActionException("Must be within 2 cardinal tiles to bribe Mercenary!");
        }
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject mercJSON = super.saveGameJson();
        JSONObject movement = new JSONObject();

        movement.put("default-strategy", defaultMovementStrategy.getMovementType());
        movement.put("movement-strategy", movementStrategy.getMovementType());
        
        mercJSON.put("movement", movement);
        mercJSON.put("ally", getAlly());

        return mercJSON;
    }

}
