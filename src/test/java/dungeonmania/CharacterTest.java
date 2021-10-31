package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import dungeonmania.movingEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CharacterTest {

    @Test
    public void constructTest() {
        // Testing construction + basic getters

        HealthPoint playerHealth = new HealthPoint(100);
        Position start = new Position(0, 0);

        Player player = new Player(0, 0, "1", playerHealth);

        // Ensure attributes are correct
        assertEquals(player.getHealthPoint(), new HealthPoint(100));
        assertEquals(player.getId(), 10);
        assertEquals(player.getPosition(), new Position(0, 0));

    }


    @Test
    public void testPlayerMovement() {
        
        HealthPoint playerHealth = new HealthPoint(100);
        Position start = new Position(0, 0);

        Player player = new Player(0, 0, "1", playerHealth);

        //player.move(player.getPosition().translateBy(Direction.UP));
        assertEquals(player.getPosition(), new Position(0, 1));

        //player.move(player.getPosition().translateBy(Direction.RIGHT));
        //player.move(player.getPosition().translateBy(Direction.DOWN));

        assertEquals(player.getPosition(), new Position(1, 0));
        // moving outside the game will not work
        //player.move(player.getPosition().translateBy(Direction.DOWN));

        assertEquals(player.getPosition(), new Position(1, 0));

    }

    @Test
    public void testCharacterMovement() {

        HealthPoint health = new HealthPoint(100);
        Position start = new Position(0, 0);

        //Spider spider = new Spider(health, 10, start);
        
        //spider.move(new Position(0,0)); // argument should not matter
        /*assertEquals(spider.getPosition(), new Position(0, 1));
        //spider.move(new Position(0,0)); // argument should not matter
        assertEquals(spider.getPosition(), new Position(1, 1));*/

    }

    @Test
    public void testBattle() {
        
        HealthPoint health = new HealthPoint(100);
        Position start = new Position(0, 0);

        //Spider spider = new Spider(health, 10, start);
        //Player player = new Player(health, 10, start);

        // on same cell, battle should be created

        /*Battle battle = new Battle(player, spider);
        battle.battleTick();
        assertEquals(spider.getHealthPoint().getHealth(), health.getHealth() - player.attack());
        assertEquals(player.getHealthPoint().getHealth(), health.getHealth() - spider.attack());

        assertEquals(battle.getPosition(), start);*/
        
    }
}