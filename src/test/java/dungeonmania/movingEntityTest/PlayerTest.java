package dungeonmania.movingEntityTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.InputValueSwitch;

import dungeonmania.World;
import dungeonmania.Passive;
import dungeonmania.exceptions.*;
import dungeonmania.gamemode.Standard;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.collectable.InvincibilityPotion;
import dungeonmania.collectable.InvisibilityPotion;
import dungeonmania.inventory.Inventory;
import dungeonmania.movingEntity.*;
import dungeonmania.movingEntity.MovementStrategies.CircleMovement;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

   

public class PlayerTest {
    @Test
    public void constructPlayer() {
        Passive invincibilityPassive  = new InvincibilityPotion(5);

        Player player = new Player(1, 0, "player2", new HealthPoint(10), invincibilityPassive);

        JSONObject jsonObj = player.saveGameJson();

        assertEquals(new Position(1,0), new Position(jsonObj.getInt("x"), jsonObj.getInt("y")));
        JSONObject activePotion = jsonObj.getJSONObject("active-potion");

        assertEquals("invincibility_potion", activePotion.getString("active-potion"));
        assertEquals(5, activePotion.getInt("duration"));
    }

    @Test
    public void mercRunAwayTest() {
        Passive invincibilityPassive  = new InvincibilityPotion(5);

        Mercenary merc = new Mercenary(2, 1, "mercenary1", new HealthPoint(10), "followPlayer", "followPlayer", false);
        

        merc.updateMovement("invincibility_potion");
        assertEquals("runAway", merc.getMovement().getMovementType());
    
    }

    @Test
    public void mercRunAwayAllyTest() {
        Passive invincibilityPassive  = new InvincibilityPotion(5);

        Mercenary merc = new Mercenary(2, 1, "mercenary1", new HealthPoint(10), "followPlayer", "followPlayer", true);
        

        merc.updateMovement("invincibility_potion");
        assertEquals("followPlayer", merc.getMovement().getMovementType());
    
    }

    @Test
    public void spiderConstructors() {
        Spider s1 = new Spider(1, 1, "spider1", new HealthPoint(10), "runAway", "followPlayer", "UP", "DOWN", 2, 1, false);
        
        Spider s2 = new Spider(1, 1, "spider1", new HealthPoint(10), "randomMovement", "runAway", "UP", "DOWN", 2, 1, false);
        
        Spider s3 = new Spider(1, 1, "spider1", new HealthPoint(10), "followPlayer", "randomMovement", "UP", "DOWN", 2, 1, false);


        Spider s4 = new Spider(1, 1, "spider1", new HealthPoint(10), "circle", "circle", "RIGHT", "LEFT", 2, 1, false);
        Spider s5 = new Spider(1, 1, "spider1", new HealthPoint(10), "circle", "circle", "DOWN", "RIGHT", 2, 1, false);
        Spider s6 = new Spider(1, 1, "spider1", new HealthPoint(10), "circle", "circle", "LEFT", "UP", 2, 1, false);
        Spider s7 = new Spider(1, 1, "spider1", new HealthPoint(10), "circle", "circle", "NONE", "LEFT", 2, 1, false);

        assertEquals("followPlayer", s1.getMovement().getMovementType());
        assertEquals("runAway", s2.getMovement().getMovementType());
        assertEquals("randomMovement", s3.getMovement().getMovementType());

        JSONObject spider5JSON = s5.saveGameJson();
        JSONObject spider6JSON = s6.saveGameJson();

        assertDoesNotThrow(()->spider5JSON.getJSONObject("movement"));
        assertDoesNotThrow(()->spider5JSON.getJSONObject("movement"));
    }


}
