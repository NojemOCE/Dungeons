package dungeonmania.factory;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.Passive;
import dungeonmania.World;
import dungeonmania.collectable.*;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.movingEntity.*;
import dungeonmania.movingEntity.MovementStrategies.CircleMovement;
import dungeonmania.movingEntity.MovementStrategies.FollowPlayer;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.movingEntity.MovementStrategies.RunAway;
import dungeonmania.movingEntity.States.NormalState;
import dungeonmania.movingEntity.States.State;
import dungeonmania.movingEntity.States.SwampState;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Direction;

public class LoadGameFactory extends Factory {

    /**
     * Constructor for LoadGameFactory taking a gamemode
     * @param gamemode gamemode of the factory
     */
    public LoadGameFactory(Gamemode gamemode) {
        super(gamemode, 1);
    }

    /**
     * Creates annd returns Player from a JSON Object
     * @param obj JSON object to build player from
     * @return player
     */
    private Player createPlayerFromJSON(JSONObject obj) {
        int x = obj.getInt("x");
        int y = obj.getInt("y");

        updateBounds(x, y);

        String id = obj.getString("id");

        JSONObject healthPoint = obj.getJSONObject("health-point");

        double health = healthPoint.getDouble("health");
        double maxHealth = healthPoint.getDouble("max-health");

        HealthPoint playerHP = new HealthPoint(health, maxHealth);

        if (obj.has("active-potion")) {
            JSONObject activePotion = obj.getJSONObject("active-potion");
            String activePotionType = activePotion.getString("active-potion");
            int activePotionDuration = activePotion.getInt("duration");

            Passive e = null;
            if (activePotionType.equals("invincibility_potion")) {
                e = new InvincibilityPotion(activePotionDuration);
            }
            else if (activePotionType.equals("invisibility_potion")) {
                e = new InvisibilityPotion(activePotionDuration);
            }

            Player player = new Player(x, y, id, playerHP, e);
            return player;

        }
        else {
            Player player = new Player(x, y, id, playerHP);
            return player;
        }

    }

    /**
     * Creates and returns moving entity from a JSON Object
     * @param obj JSON Object to build moving entity from
     * @return moving entity
     */
    private MovingEntity createMovingEntityFromJSON(JSONObject obj) {
        //TODO update constructors

        int x = obj.getInt("x");
        int y = obj.getInt("y");

        updateBounds(x, y);

        String type = obj.getString("type");

        JSONObject defaultMovementJSON = obj.getJSONObject("default-strategy");
        JSONObject currentMovementJSON = obj.getJSONObject("movement-strategy");

        MovementStrategy defaultMovement = getMovementStrategyFromJSON(defaultMovementJSON);
        MovementStrategy currentMovement = getMovementStrategyFromJSON(currentMovementJSON);

        JSONObject stateJSON = obj.getJSONObject("state");

        State state = getStateFromJSON(stateJSON);

        JSONObject healthPoint = obj.getJSONObject("health-point");

        double health = healthPoint.getDouble("health");
        double maxHealth = healthPoint.getDouble("max-health");

        HealthPoint entityHP = new HealthPoint(health, maxHealth);
        

        String id = obj.getString("id");

        
        if (type.equals("spider")) {
            Spider e = new Spider(x, y, id, entityHP, defaultMovement, currentMovement, state);
            return e;
        } 
        
        else if (type.equals("zombie_toast")) {
            Zombie e = new Zombie(x, y, id, entityHP, defaultMovement, currentMovement, state);
            return e;
        } 

        else if (type.equals("mercenary")) {
            boolean isAlly = obj.getBoolean("ally");
            Mercenary e = new Mercenary(x, y, id, entityHP, defaultMovement, currentMovement, isAlly, state);
            return e;
        }

        else if (type.equals("assassin")) {
            boolean isAlly = obj.getBoolean("ally");
            MercenaryComponent m = new Mercenary(x, y, id, entityHP, defaultMovement, currentMovement, isAlly, state);
            AssassinDecorator e = new AssassinDecorator(m);
            return e;
        } 

        else if (type.equals("hydra")) {
            Hydra e = new Hydra(x,y, id, entityHP,  defaultMovement, currentMovement, state);
            return e;
        } 
        return null;

    }

    /**
     * Creates and returns a movement State object from a given JSON Object
     * @param stateJSON JSON object to build state from
     * @return movement state 
     */
    private State getStateFromJSON(JSONObject stateJSON) {
        String type = stateJSON.getString("state");
        if (type.equals("normalState")) {
            return new NormalState();
        }
        else if (type.equals("swampState")) {
            int remTicks = stateJSON.getInt("remTicks");
            return new SwampState(remTicks);
        }
        return null;
    }

    /**
     * Creates and returns a movement strategy from a given JSON Object
     * @param movementJSON JSON object to build movement strategy from
     * @return movement strategy
     */
    private MovementStrategy getMovementStrategyFromJSON(JSONObject movementJSON) {
        String movement = movementJSON.getString("movement");

        if (movement.equals("circleMovement")) {
            String currentDirString = movementJSON.getString("current-direction");
            String nextDirString = movementJSON.getString("next-direction");
            int remMovesCurr = movementJSON.getInt("remMovesCurr");
            int remMovesNext = movementJSON.getInt("remMovesNext");
            boolean avoidPlayer = movementJSON.getBoolean("avoidPlayer");

            Direction currentDirection = getDirectionFromString(currentDirString);
            Direction nextDirection = getDirectionFromString(nextDirString);

            return new CircleMovement(currentDirection, nextDirection, remMovesCurr, remMovesNext, avoidPlayer);
        }
        else if (movement.equals("followPlayer")) {
            return new FollowPlayer();
        }
        else if (movement.equals("randomMovement")) {
            return new RandomMovement();
        }
        else if (movement.equals("runAway")) {
            return new RunAway();
        }

        return null;
    }

    /**
     * Creates and returns a direction from a string
     * @param d string of direction
     * @return direction
     */
    private Direction getDirectionFromString(String d) {
        switch(d)  {
            case "UP":
                return Direction.UP;
            case "DOWN":
                return Direction.DOWN;
            case "LEFT":
                return Direction.LEFT;
            case "RIGHT":
                return Direction.RIGHT;
            default:
                return Direction.NONE;
        }
    }

    /**
     * Creates and returns a static entity from a JSON Object
     * @param obj JSON Object to build static entity from
     * @return static entity
     */
    private StaticEntity createStaticEntityFromJSON(JSONObject obj, World world) {
        int x = obj.getInt("x");
        int y = obj.getInt("y");

        updateBounds(x, y);

        String type = obj.getString("type");

        String id = obj.getString("id");

        if (type.equals("wall")) {
            Wall e = new Wall(x, y, id);
            return e;

        } else if (type.equals("exit")) {
            Exit e = new Exit(x,y,id);
            return e;
        } 
        
        else if (type.equals("switch")) {
            FloorSwitch e = new FloorSwitch(x, y, id);
            return e;
        } 
        
        else if (type.equals("boulder")) {
            JSONObject stateJSON = obj.getJSONObject("state");
            Boulder e = new Boulder(x, y, id, getStateFromJSON(stateJSON));
            return e;
        } 
        
        else if (type.equals("door")) {
            int key = obj.getInt("key");
            boolean opened = obj.getBoolean("open");
            Door e = new Door(x, y, id, key, opened);
            return e;
        } 
        
        else if (type.equals("portal")) {
            Portal e;

            String colour = obj.getString("colour");

            if (world.getStaticEntities().containsKey(colour)) {
                e = new Portal(x, y, colour + "2", colour, (Portal) world.getStaticEntities().get(colour));
            } else {
                e = new Portal(x, y, colour, colour);
            }
            return e;
        } 
        
        else if (type.equals("zombie_toast_spawner")) {
            ZombieToastSpawn e = new ZombieToastSpawn(x, y, id);
            return e;   
        } 

        else if (type.equals("swamp_tile")) {
            int movement_factor = obj.getInt("movement_factor");
            SwampTile e = new SwampTile(x, y, id, movement_factor);
            return e;   
        } 
        
        else if (type.equals("bomb_placed")) {
            PlacedBomb e = new PlacedBomb(x, y, id);
            return e; 
        }
        return null;
    }

    /**
     * Creates and returns a Collectable Entity from a JSON Object
     * @param obj JSON Object to create collectable entity from
     * @return collectable entity
     */
    private CollectableEntity createCollectableEntityFromJSON(JSONObject obj) {
        // TODO update constructors
        int x = obj.getInt("x");
        int y = obj.getInt("y");

        int durability = obj.getInt("durability");

        updateBounds(x, y);

        String type = obj.getString("type");

        String id = obj.getString("id");

        
        if (type.equals("treasure")) {
            Treasure e = new Treasure(x, y, id, durability);
            return e;
        } 
        
        else if (type.equals("key")) {
            int key = (int)obj.get("key");
            Key e = new Key(x, y, id, key, durability);
            return e;
        } 
        
        else if (type.equals("health_potion")) {
            HealthPotion e = new HealthPotion(x, y, id, durability);
            return e;
        } 
        
        else if (type.equals("invincibility_potion")) {
            InvincibilityPotion e = new InvincibilityPotion(x, y, id, gamemode.isInvincibilityEnabled());
            return e;
        } 
        
        else if (type.equals("invisibility_potion")) {
            //int duration = obj.getInt("duration");
            //InvisibilityPotion e = new InvisibilityPotion(x, y, id, durability, duration);
            InvisibilityPotion e = new InvisibilityPotion(x, y, id);
            return e;
        } 
        
        else if (type.equals("wood")) {
            Wood e = new Wood(x, y, id, durability);
            return e;
        } 
        
        else if (type.equals("arrow")) {
            Arrows e = new Arrows(x, y, id, durability);
            return e;
        } 
        
        else if (type.equals("bomb")) {
            Bomb e = new Bomb(x, y, id, durability);
            return e;
        } 
        
        else if (type.equals("sword")) {
            Sword e = new Sword(x, y, id, durability);
            return e;
        }
        else if (type.equals("one_ring")) {
            OneRing e = new OneRing(x, y, id, durability);
            return e;
        }
        else if (type.equals("armour")) {
            Armour e = new Armour(id, durability);
            return e;
        }

        else if (type.equals("bow")) {
            Bow e = new Bow(id, durability);
            return e;
        }

        else if (type.equals("shield")) {
            Shield e = new Shield(id, durability);
            return e;
        }

        else if (type.equals("sceptre")) {

            int cooldown = Integer.parseInt((String) obj.get("cooldown"));
            Sceptre e = new Sceptre(id, cooldown, durability);

            return e;
        }

        else if (type.equals("anduril")) {
            Anduril e = new Anduril(id, durability);
            return e;
        }

        else if (type.equals("midnight_armour")) {
            MidnightArmour e = new MidnightArmour(id, durability);
            return e;
        }

        else if (type.equals("sun_stone")) {
            SunStone e = new SunStone(id, durability);
            return e;
        }

        return null;
    }

    @Override
    public Entity createEntity(JSONObject jsonObject, World world) {
        String type = jsonObject.getString("type");

        if (collectablesList().contains(type)) {
            return createCollectableEntityFromJSON(jsonObject);
        }

        else if (type.equals("player")) {
            return createPlayerFromJSON(jsonObject);
        }

        else if (staticsList().contains(type)) {
            return createStaticEntityFromJSON(jsonObject, world);
        }

        //add moving entities list
        else if (movingEntitiesList().contains(type)) {
            return createMovingEntityFromJSON(jsonObject);
        }

        return null;
    }

    /**
     * Returns a list of all the different string types of collectable entities
     * @return String list of collectable entity types
     */
    private List<String> collectablesList() {
        // TODO add additional entities
        return Arrays.asList("arrow", "armour", "bomb", "bow", "health_potion", "invisibility_potion", "invincibility_potion", "key", "one_ring", "shield", "sword", "treasure", "wood",  "anduril", "sceptre", "midnight_armour", "sun_stone");
    }

    /**
     * Returns a list of all the different string types of static entities
     * @return String list of static entity types
     */
    private List<String> staticsList() {
        // TODO add additional entities
        return Arrays.asList("boulder", "door", "exit", "switch", "bomb_placed", "portal", "wall", "zombie_toast_spawner", "swamp_tile");
    }

    /**
     * Returns a list of all the different string types of moving entities
     * @return String list of moving entity types
     */
    private List<String> movingEntitiesList() {
        // TODO add additional entities
        return Arrays.asList("zombie_toast", "mercenary", "spider", "assassin", "hydra");
    }
}
