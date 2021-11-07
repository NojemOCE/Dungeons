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
import dungeonmania.staticEntity.*;

public class LoadGameFactory extends Factory {

    
    public LoadGameFactory(Gamemode gamemode) {
        super(gamemode);
    }

    /**
     * Creates player from a json and remounts observers
     * @param obj json
     */
    private Player createPlayerFromJSON(JSONObject obj) {
        int x = obj.getInt("x");
        int y = obj.getInt("y");

        //updateBounds(x, y);

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
            else if (activePotionType.equals("health_potion")) {
                e = new HealthPotion(activePotionDuration);
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
     * Creates moving entity from a json
     * @param obj json
     */
    private MovingEntity createMovingEntityFromJSON(JSONObject obj) {
        //TODO update constructors

        int x = obj.getInt("x");
        int y = obj.getInt("y");

        //updateBounds(x, y);

        String type = obj.getString("type");

        JSONObject movement = obj.getJSONObject("movement");

        String defaultMovement = movement.getString("default-strategy");
        String currentMovement = movement.getString("movement-strategy");

        JSONObject healthPoint = obj.getJSONObject("health-point");

        double health = healthPoint.getDouble("health");
        double maxHealth = healthPoint.getDouble("max-health");

        HealthPoint entityHP = new HealthPoint(health, maxHealth);
        

        String id = obj.getString("id");

        
        if (type.equals("spider")) {
            String currentDir = movement.getString("current-direction");
            String nextDir = movement.getString("next-direction");
            int remMovesCurr = movement.getInt("remMovesCurr");
            int remMovesNext = movement.getInt("remMovesNext");
            boolean avoidPlayer  = movement.getBoolean("avoidPlayer");
            Spider e = new Spider(x, y, id, entityHP, defaultMovement, currentMovement, currentDir, nextDir, remMovesCurr, remMovesNext, avoidPlayer);

            return e;
        } 
        
        else if (type.equals("zombie_toast")) {
            Zombie e = new Zombie(x, y, id, entityHP, defaultMovement, currentMovement);
            return e;
        } 
        
        else if (type.equals("mercenary")) {
            boolean isAlly = obj.getBoolean("ally");
            Mercenary e = new Mercenary(x, y, id, entityHP, defaultMovement, currentMovement, isAlly);

            return e;
        } 
        return null;
    }

    /**
     * Creates static entity from a json
     * @param obj json
     */
    private StaticEntity createStaticEntityFromJSON(JSONObject obj, World world) {
        //TODO update constructors
        int x = obj.getInt("x");
        int y = obj.getInt("y");

        //updateBounds(x, y);

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
            Boulder e = new Boulder(x, y, id);
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
            SwampTile e = new SwampTile(x, y, id);
            return e;   
        } 
        
        else if (type.equals("placed_bomb")) {
            PlacedBomb e = new PlacedBomb(x, y, id);
            return e; 
        }
        return null;
    }

    /**
     * Creates collectible from a json
     * @param obj json
     */
    private CollectableEntity createCollectableEntityFromJSON(JSONObject obj) {
        // TODO update constructors
        int x = obj.getInt("x");
        int y = obj.getInt("y");

        int durability = obj.getInt("durability");

        //updateBounds(x, y);

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
        // Bow and arrow and any other buildable objs
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

    private List<String> collectablesList() {
        return Arrays.asList("arrow", "armour", "bomb", "bow", "health_potion", "invisibility_potion", "invincibility_potion", "key", "one_ring", "shield", "sword", "treasure", "wood");
    }

    private List<String> staticsList() {
        return Arrays.asList("boulder", "door", "exit", "switch", "placed_bomb", "portal", "wall", "zombie_toast_spawner");
    }

    private List<String> movingEntitiesList() {
        return Arrays.asList("zombie_toast", "mercenary", "spider");
    }
}
