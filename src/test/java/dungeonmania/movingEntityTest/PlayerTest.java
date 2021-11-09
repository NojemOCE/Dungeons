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
import dungeonmania.movingEntity.MovementStrategies.FollowPlayer;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.movingEntity.MovementStrategies.RunAway;
import dungeonmania.movingEntity.States.NormalState;
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

        Mercenary merc = new Mercenary(2, 1, "mercenary1", new HealthPoint(10), new FollowPlayer(), new FollowPlayer(), false, new NormalState());
        

        merc.updateMovement("invincibility_potion");
        assertEquals("runAway", merc.getMovement().getMovementType());
    
    }

    @Test
    public void mercRunAwayAllyTest() {
        Passive invincibilityPassive  = new InvincibilityPotion(5);

        Mercenary merc = new Mercenary(2, 1, "mercenary1", new HealthPoint(10), new FollowPlayer(), new FollowPlayer(), true, new NormalState());
        

        merc.updateMovement("invincibility_potion");
        assertEquals("followPlayer", merc.getMovement().getMovementType());
    
    }

    @Test
    public void spiderConstructors() {

        Spider s1 = new Spider(1, 1, "spider1", new HealthPoint(10), new RunAway(), new FollowPlayer(), new NormalState());
        
        Spider s2 = new Spider(1, 1, "spider1", new HealthPoint(10), new RandomMovement(), new RunAway(), new NormalState());
        
        Spider s3 = new Spider(1, 1, "spider1", new HealthPoint(10), new FollowPlayer(), new RandomMovement(), new NormalState());


        Spider s4 = new Spider(1, 1, "spider1", new HealthPoint(10), new CircleMovement(), new CircleMovement(Direction.RIGHT, Direction.LEFT, 2, 1, false), new NormalState());
        Spider s5 = new Spider(1, 1, "spider1", new HealthPoint(10), new CircleMovement(), new CircleMovement(Direction.DOWN, Direction.RIGHT, 2, 1, false), new NormalState());
        Spider s6 = new Spider(1, 1, "spider1", new HealthPoint(10), new CircleMovement(), new CircleMovement(Direction.LEFT, Direction.UP, 2, 1, false), new NormalState());
        Spider s7 = new Spider(1, 1, "spider1", new HealthPoint(10), new CircleMovement(), new CircleMovement(Direction.NONE, Direction.LEFT, 2, 1, false), new NormalState());

        assertEquals("followPlayer", s1.getMovement().getMovementType());
        assertEquals("runAway", s2.getMovement().getMovementType());
        assertEquals("randomMovement", s3.getMovement().getMovementType());

        JSONObject spider5JSON = s5.saveGameJson();
        JSONObject spider6JSON = s6.saveGameJson();

        assertDoesNotThrow(()->spider5JSON.getJSONObject("movement-strategy"));
        assertDoesNotThrow(()->spider5JSON.getJSONObject("default-strategy"));
        assertDoesNotThrow(()->spider6JSON.getJSONObject("movement-strategy"));
        assertDoesNotThrow(()->spider6JSON.getJSONObject("default-strategy"));

        JSONObject s5CurrMovement = spider5JSON.getJSONObject("movement-strategy");
        JSONObject s6CurrMovement = spider6JSON.getJSONObject("movement-strategy");

        assertEquals("DOWN", s5CurrMovement.getString("current-direction"));
        assertEquals("RIGHT", s5CurrMovement.getString("next-direction"));
        assertEquals(2, s5CurrMovement.getInt("remMovesCurr"));
        assertEquals(1, s5CurrMovement.getInt("remMovesNext"));

        assertEquals("LEFT", s6CurrMovement.getString("current-direction"));
        assertEquals("UP", s6CurrMovement.getString("next-direction"));
        assertEquals(2, s6CurrMovement.getInt("remMovesCurr"));
        assertEquals(1, s6CurrMovement.getInt("remMovesNext"));

        JSONObject s5State = spider5JSON.getJSONObject("state");
        assertEquals("normalState", s5State.getString("state"));

    }


}
