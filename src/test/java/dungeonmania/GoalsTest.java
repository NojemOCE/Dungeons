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
        //Battle ended, goal achieved
        d = c.tick(null, Direction.DOWN);
        assertEquals("", d.getGoals());

        /*d = c.tick(null, Direction.DOWN);
        assertEquals("(:enemies OR :exit)", d.getGoals());
        
        d = c.tick(null, Direction.DOWN);
        assertEquals("(:enemies OR :exit)", d.getGoals());*/

        //Battle ended, goal achieved
        //d = c.tick(null, Direction.DOWN);
        //assertEquals("", d.getGoals());
    }
    @Test
    public void simpleAndTest1(){
        DungeonManiaController c = new DungeonManiaController();

        DungeonResponse d = c.newGame("simple-and-goal", "Standard");
        assertEquals("(:enemies AND :exit)", d.getGoals());
        
        
        d = c.tick(null, Direction.UP);
        assertEquals("(:enemies AND :exit)", d.getGoals());

        //Mercenary and player meet on the same square and battle
        d = c.tick(null, Direction.RIGHT);
        assertEquals(":exit", d.getGoals());

        //Battle ended, enemies goal achieved
        d = c.tick(null, Direction.UP);
        assertEquals(":exit", d.getGoals());
        
        d = c.tick(null, Direction.DOWN);
        assertEquals(":exit", d.getGoals());

        // Move to exit
        d = c.tick(null, Direction.DOWN);
        assertEquals("", d.getGoals());
    }

    @Test
    public void simpleAndTest2(){
        DungeonManiaController c = new DungeonManiaController();
        // Check if the player goes on exit before the other goals are achieved, that the goal is not completed
        
        DungeonResponse d = c.newGame("simple-and-goal", "Standard");
        assertEquals("(:enemies AND :exit)", d.getGoals());

        // Player moves to the top of the exit
        d = c.tick(null, Direction.RIGHT);
        assertEquals(":enemies", d.getGoals());

        
        // Mercenary and player meet on the same square and battle
        d = c.tick(null, Direction.UP);
        assertEquals(":exit", d.getGoals());

        //Battle ended, enemies goal achieved
        d = c.tick(null, Direction.UP);
        assertEquals(":exit", d.getGoals());

        d = c.tick(null, Direction.DOWN);
        assertEquals(":exit", d.getGoals());
        
        d = c.tick(null, Direction.DOWN);
        assertEquals("", d.getGoals());
    }

    @Test
    public void simpleBoulderExitTest1(){
        DungeonManiaController c = new DungeonManiaController();
        // Check if the player goes on exit before the other goals are achieved, that the goal is not completed
        
        DungeonResponse d = c.newGame("simple-boulders", "Standard");
        assertEquals("(:exit OR :boulders)", d.getGoals());

        // Player pushes one boulder onto a switch (note, in this map, one boulder is created on top of a switch)
        d = c.tick(null, Direction.DOWN);
        assertEquals("(:exit OR :boulders)", d.getGoals());

        
        // Player moves up
        d = c.tick(null, Direction.UP);
        assertEquals("(:exit OR :boulders)", d.getGoals());

        // Player pushes the last boulder onto the switch
        d = c.tick(null, Direction.RIGHT);
        assertEquals("", d.getGoals());
    }

    @Test
    public void simpleBoulderExitTest2(){
        DungeonManiaController c = new DungeonManiaController();
        // Check if the player goes on exit before the other goals are achieved, that the goal is not completed
        
        DungeonResponse d = c.newGame("simple-boulders", "Standard");
        assertEquals("(:exit OR :boulders)", d.getGoals());

        // Player pushes one boulder onto a switch (note, in this map, one boulder is created on top of a switch)
        d = c.tick(null, Direction.DOWN);
        assertEquals("(:exit OR :boulders)", d.getGoals());

        
        // Player moves right
        d = c.tick(null, Direction.RIGHT);
        assertEquals("(:exit OR :boulders)", d.getGoals());

        // Player moves down
        d = c.tick(null, Direction.DOWN);
        assertEquals("(:exit OR :boulders)", d.getGoals());

        d = c.tick(null, Direction.RIGHT);
        assertEquals("", d.getGoals());
    }

    @Test
    public void simpleTreasureExitTest1(){
        DungeonManiaController c = new DungeonManiaController();
        // Check if the player goes on exit before the other goals are achieved, that the goal is not completed
        
        DungeonResponse d = c.newGame("simple-treasure", "Standard");
        assertEquals("(:exit AND :treasure)", d.getGoals());

        // Player moves right
        d = c.tick(null, Direction.RIGHT);
        assertEquals("(:exit AND :treasure)", d.getGoals());

        
        // Player moves right
        d = c.tick(null, Direction.RIGHT);
        assertEquals("(:exit AND :treasure)", d.getGoals());

        // Player moves down
        d = c.tick(null, Direction.DOWN);
        assertEquals("(:exit AND :treasure)", d.getGoals());

        // Player moves down (currently on top of exit)
        d = c.tick(null, Direction.DOWN);
        assertEquals(":treasure", d.getGoals());


        // Player moves left
        d = c.tick(null, Direction.LEFT);
        assertEquals("(:exit AND :treasure)", d.getGoals());
        
        // Player moves left (collects treasure 1)
        d = c.tick(null, Direction.LEFT);
        assertEquals("(:exit AND :treasure)", d.getGoals());

        // Player moves up
        d = c.tick(null, Direction.UP);
        assertEquals("(:exit AND :treasure)", d.getGoals());

        // Player moves right (collects treasure 2, achieving treasure goal)
        d = c.tick(null, Direction.RIGHT);
        assertEquals(":exit", d.getGoals());

        // Player moves down (goal should now just be exit)
        d = c.tick(null, Direction.DOWN);
        assertEquals(":exit", d.getGoals());

        // Player moves right (exit, goals should be achieved)
        d = c.tick(null, Direction.RIGHT);
        assertEquals("", d.getGoals());
    }

    @Test
    public void simpleTreasureExitTest2(){
        DungeonManiaController c = new DungeonManiaController();
        // Check if the player goes on exit before the other goals are achieved, that the goal is not completed
        
        DungeonResponse d = c.newGame("simple-treasure", "Standard");
        assertEquals("(:exit AND :treasure)", d.getGoals());

        // Player moves down
        d = c.tick(null, Direction.DOWN);
        assertEquals("(:exit AND :treasure)", d.getGoals());

        
        // Player moves down (collects treasure 1)
        d = c.tick(null, Direction.DOWN);
        assertEquals("(:exit AND :treasure)", d.getGoals());

        // Player moves right
        d = c.tick(null, Direction.RIGHT);
        assertEquals("(:exit AND :treasure)", d.getGoals());

        // Player moves up (collects treasure 2, achieving treasure goal)
        d = c.tick(null, Direction.UP);
        assertEquals(":exit", d.getGoals());

        // Player moves right
        d = c.tick(null, Direction.RIGHT);
        assertEquals(":exit", d.getGoals());

        // Player moves down
        d = c.tick(null, Direction.DOWN);
        assertEquals("", d.getGoals());
    }
    
}
