package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import static dungeonmania.TestHelpers.assertListAreEqualIgnoringOrder;

public class GoalsTest {

    @Test
    public void simpleOrTest1(){
        DungeonManiaController c = new DungeonManiaController();

        DungeonResponse d = c.newGame("simple-or-goal", "Standard");
        assertEquals("(:enemies OR :exit)", d.getGoals());

        d = c.tick(null, Direction.RIGHT);
        assertEquals("", d.getGoals());
    }

    @Test
    public void simpleOrTest2(){
        DungeonManiaController c = new DungeonManiaController();

        DungeonResponse d = c.newGame("simple-or-goal", "Standard");
        assertEquals("(:enemies OR :exit)", d.getGoals());

        d = c.tick(null, Direction.DOWN);
        assertEquals("(:enemies OR :exit)", d.getGoals());

        //Mercenary and player are currently in battle
        d = c.tick(null, Direction.DOWN);
        assertEquals("(:enemies OR :exit)", d.getGoals());

        //Battle ended, goal achieved
        d = c.tick(null, Direction.DOWN);
        assertEquals("", d.getGoals());
    }
    @Test
    public void simpleAndTest1(){
        DungeonManiaController c = new DungeonManiaController();

        DungeonResponse d = c.newGame("simple-and-goal", "Standard");
        assertEquals("(:enemies AND :exit)", d.getGoals());
        
        
        d = c.tick(null, Direction.UP);
        assertEquals("(:enemies AND :exit)", d.getGoals());

        //Mercenary and player meet on the same square (battle)
        d = c.tick(null, Direction.RIGHT);
        assertEquals("(:enemies AND :exit)", d.getGoals());

        //Battle ended, enemies goal achieved
        d = c.tick(null, Direction.DOWN);
        assertEquals(":exit", d.getGoals());

        
        d = c.tick(null, Direction.DOWN);
        assertEquals(":exit", d.getGoals());

        
        d = c.tick(null, Direction.RIGHT);
        assertEquals("", d.getGoals());
    }

    @Test
    public void simpleAndTest2(){
        DungeonManiaController c = new DungeonManiaController();
        // Check if the player goes on exit before the other goals are achieved, that the goal is not completed
        
        DungeonResponse d = c.newGame("simple-and-goal", "Standard");
        assertEquals("(:enemies AND :exit)", d.getGoals());

        d = c.tick(null, Direction.RIGHT);
        assertEquals("(:enemies AND :exit)", d.getGoals());

        // Player moves to the top of the exit
        d = c.tick(null, Direction.RIGHT);
        assertEquals("(:enemies AND :exit)", d.getGoals());

        
        // Mercenary and player meet on the same square (battle)
        d = c.tick(null, Direction.LEFT);
        assertEquals("(:enemies AND :exit)", d.getGoals());

        //Battle ended, enemies goal achieved
        d = c.tick(null, Direction.DOWN);
        assertEquals(":exit", d.getGoals());
        
        d = c.tick(null, Direction.RIGHT);
        assertEquals("", d.getGoals());
    }
    
}
