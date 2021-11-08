package dungeonmania.factory;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.collectable.*;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.movingEntity.*;
import dungeonmania.staticEntity.*;

public class NewGameFactory extends Factory {

    public NewGameFactory(Gamemode gamemode) {
        super(gamemode);
    }


    public Entity createEntity(JSONObject obj, World world) {
        int x = (int)obj.get("x");
        int y = (int)obj.get("y");


        //world.updateBounds(x, y);

        String type = obj.getString("type");

        String id = type + String.valueOf(incrementEntityCount());

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
            int key = (int)obj.get("key");
            Door e = new Door(x, y, id, key);
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
        
        else if (type.equals("player")) {
            Player e = new Player(x, y, id, new HealthPoint(gamemode.getStartingHP()));
           
            return e;
        } 
        
        else if (type.equals("spider")) {
            Spider e = new Spider(x, y, id);
            return e;
        } 
        
        else if (type.equals("zombie_toast")) {
            Zombie e = new Zombie(x, y, id);
            return e;
        } 
        
        else if (type.equals("mercenary")) {
            Mercenary e = new Mercenary(x, y, id);

            return e;
        } 
        
        else if (type.equals("treasure")) {
            Treasure e = new Treasure(x, y, id);
            return e;
        } 
        
        else if (type.equals("key")) {
            int key = (int)obj.get("key");
            Key e = new Key(x, y, id, key);
            return e;
        } 
        
        else if (type.equals("health_potion")) {
            HealthPotion e = new HealthPotion(x, y, id);
            return e;
        } 
        
        else if (type.equals("invincibility_potion")) {
            InvincibilityPotion e = new InvincibilityPotion(x, y, id, gamemode.isInvincibilityEnabled());
            return e;
        } 
        
        else if (type.equals("invisibility_potion")) {
            InvisibilityPotion e = new InvisibilityPotion(x, y, id);
            return e;
        } 
        
        else if (type.equals("wood")) {
            Wood e = new Wood(x, y, id);
            return e;
        } 
        
        else if (type.equals("arrow")) {
            Arrows e = new Arrows(x, y, id);
            return e;
        } 
        
        else if (type.equals("bomb")) {
            Bomb e = new Bomb(x, y, id);
            return e;
        }
        else if (type.equals("placed_bomb")) {
            PlacedBomb e = new PlacedBomb(x, y, id);
            return e;
        }
        else if (type.equals("sword")) {
            Sword e = new Sword(x, y, id);
            return e;
        }
        else if (type.equals("one_ring")) {
            OneRing e = new OneRing(x, y, id);
            return e;
        }

        return null;
    }
    
}
