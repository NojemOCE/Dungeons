package dungeonmania.movingEntity;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.movingEntity.MovementStrategies.FollowPlayer;
import dungeonmania.movingEntity.States.State;

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

    /**
     * Constructor for mercenary taking x and y coords, and unique ID
     * @param x x coordinate
     * @param y y coordinate
     * @param id unique ID of mercenary
     * @param defaultMovement default movement
     * @param currentMovement current movement
     * @param isAlly boolean for whether the mercenary is an ally
     * @param state movement state
     */
    public Mercenary(int x, int y, String id, HealthPoint hp, MovementStrategy defaultMovement, MovementStrategy currentMovement, Boolean isAlly, State state) {
        super(x,y,id,"mercenary", hp, MERC_ATTACK, defaultMovement,currentMovement,isAlly,state);

    }

    /**
     * The character can bribe a mercenary if they are within 2 cardinal tiles
     * to the mercenary. Player requires minimum amount of gold to bribe.
     * @param world current world that the mercenary is in
     * @throws InvalidActionException if the player does not have enough gold, or if the player is not within 2 cardinal tiles of the mercenary
     */
    public void interact(World world) throws InvalidActionException {
        if (getInteractable()) {
            if (world.inInventory("sceptre") && world.useableSceptre()) {
                world.useSceptre(this);
            } else if (canBribe(world)) {
                bribe(world);
                setAlly(true);
            } else {
                throw new InvalidActionException("Not enough gold to bribe Mercenary!");
            }
        } else {
            throw new InvalidActionException("Must be within 2 cardinal tiles to bribe Mercenary!");
        }
        setInteractable(world.getPlayer());
    }
    
    /**
     * Checks whether there are resources to bribe the mercenary
     * @param world The current world
     * @return true if tthere are enough resources, else false
     */
    private boolean canBribe(World world) {
        int numTreasure = world.numItemInInventory("treasure");
        int numSunstone = world.numItemInInventory("sun_stone");
        return ((numTreasure + numSunstone) >= GOLD_TO_BRIBE);
    }
    
    /**
     * Carry out required changes to make a bribe.
     * Sun stones are used before treasure.
     * @param world
     */
    private void bribe(World world) {
        for (int i = 0; i < GOLD_TO_BRIBE; i++) {
            if (world.numItemInInventory("sun_stone") >= 1) {
                world.useByType("sun_stone");
            } else {
                world.useByType("treasure");
            }
        }
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject mercJSON = super.saveGameJson();

        mercJSON.put("default-strategy", defaultMovementStrategy.getMovementJson());
        mercJSON.put("movement-strategy", movementStrategy.getMovementJson());
        mercJSON.put("state", getState().getStateJson());
        mercJSON.put("ally", getAlly());

        return mercJSON;
    }
}
