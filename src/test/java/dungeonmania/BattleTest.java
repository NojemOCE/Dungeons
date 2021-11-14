package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;


public class BattleTest {
    @Test
    public void mercSwordBattle() {
        World w = new World("battle-test", "standard", 4);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "battle-test" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }

        // Player collects sword
        DungeonResponse d = w.tick(null, Direction.UP);
        boolean mercenary = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
        }

        assertEquals(true, mercenary);

        d = w.tick(null, Direction.UP);
        mercenary = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
        }

        assertEquals(false, mercenary);
    }

    @Test
    public void assassinSwordBattle() {
        World w = new World("battle-test", "standard", 6);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "battle-test" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        // Player collects sword
        DungeonResponse d = w.tick(null, Direction.UP);
        boolean assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(true, assassin);
        d = w.tick(null, Direction.UP);
        assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(false, assassin);


    }

    @Test
    public void mercArmourBattle() {
        World w = new World("battle-armour-test", "standard", 4);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "battle-armour-test" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }

        // Player collects armour
        DungeonResponse d = w.tick(null, Direction.UP);
        boolean mercenary = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
        }

        assertEquals(true, mercenary);

        d = w.tick(null, Direction.UP);
        mercenary = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
        }

        assertEquals(false, mercenary);
    }

    @Test
    public void assassinArmourBattle() {
        World w = new World("battle-armour-test", "standard", 6);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "battle-armour-test" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        // Player collects armour
        DungeonResponse d = w.tick(null, Direction.UP);
        boolean assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(true, assassin);
        d = w.tick(null, Direction.UP);
        assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(false, assassin);


    }

    @Test
    public void mercMidnightArmourBattle() {
        World w = new World("battle-midnight-armour-test", "standard", 4);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "battle-midnight-armour-test" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }

        // Player collects midnight armour
        DungeonResponse d = w.tick(null, Direction.UP);
        boolean mercenary = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
        }

        assertEquals(true, mercenary);

        d = w.tick(null, Direction.UP);
        mercenary = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
        }

        assertEquals(false, mercenary);
    }

    @Test
    public void assassinMidnightArmourBattle() {
        World w = new World("battle-midnight-armour-test", "standard", 6);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "battle-midnight-armour-test" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        // Player collects midnight armour
        DungeonResponse d = w.tick(null, Direction.UP);
        boolean assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(true, assassin);
        d = w.tick(null, Direction.UP);
        assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(false, assassin);


    }

    @Test
    public void mercBowBattle() {
        World w = new World("battle-bow-test", "standard", 4);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "battle-bow-test" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }

        // Player collects bow
        DungeonResponse d = w.tick(null, Direction.UP);
        boolean mercenary = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
        }

        assertEquals(true, mercenary);

        d = w.tick(null, Direction.UP);
        mercenary = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
        }

        assertEquals(false, mercenary);
    }

    @Test
    public void assassinBowBattle() {
        World w = new World("battle-bow-test", "standard", 6);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "battle-bow-test" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        // Player collects bow
        DungeonResponse d = w.tick(null, Direction.UP);
        boolean assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(true, assassin);
        d = w.tick(null, Direction.UP);
        assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(false, assassin);


    }


}