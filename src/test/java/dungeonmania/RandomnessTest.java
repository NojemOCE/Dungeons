package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.DirectoryNotEmptyException;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntity.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;


public class RandomnessTest {
    @Test
    public void mercArmourRandomSpawn() {
        World w = new World("simple-and-goal", "standard", 4);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-and-goal" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }

        DungeonResponse d = w.tick(null, Direction.UP);
        boolean mercenary = false;
        boolean assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(true, mercenary);
        assertEquals(false, assassin);

        d = w.tick(null, Direction.UP);
        d = w.tick(null, Direction.UP);
        d = w.tick(null, Direction.UP);

        boolean armour = false;
        for (ItemResponse i: d.getInventory()) {
            if (i.getType().equals("armour")) {
                armour = true;
            }
        }

        assertEquals(true, armour);
    }

    @Test
    public void mercOneRingRandomSpawn() {

        World w = new World("simple-and-goal", "standard", 8);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-and-goal" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }

        DungeonResponse d = w.tick(null, Direction.UP);
        boolean mercenary = false;
        boolean assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(true, mercenary);
        assertEquals(false, assassin);

        d = w.tick(null, Direction.UP);
        d = w.tick(null, Direction.UP);
        d = w.tick(null, Direction.UP);

        boolean oneRing = false;
        for (ItemResponse i: d.getInventory()) {
            if (i.getType().equals("one_ring")) {
                oneRing = true;
            }
        }

        assertEquals(true, oneRing);
    }

    @Test
    public void assassinRandomSpawn() {
        World w = new World("simple-and-goal", "standard", 6);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-and-goal" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }

        DungeonResponse d = w.tick(null, Direction.UP);
        boolean mercenary = false;
        boolean assassin = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenary = true;
            }
            if (e.getType().equals("assassin")) {
                assassin = true;
            }
        }

        assertEquals(false, mercenary);
        assertEquals(true, assassin);
    }

    @Test
    public void randomSpiderSpawn() {

        World w = new World("simple-spider-spawning-world", "standard", 8);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-spider-spawning-world" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        DungeonResponse d = null;
        for (int i =0; i<20; i++) {
            d = w.tick(null, Direction.UP);
        }

        boolean spider = false;
        Position spiderPos = null;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("spider")) {
                spider = true;
                spiderPos = e.getPosition();
            }
        }

        assertEquals(true, spider);
        assertNotNull(spiderPos);
        assertEquals(new Position(4, 1), spiderPos);
    }

    @Test
    public void randomSpiderNoSpawn() {

        World w = new World("a-lot-of-boulders", "standard", 8);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "a-lot-of-boulders" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        DungeonResponse d = null;
        for (int i =0; i<20; i++) {
            d = w.tick(null, Direction.DOWN);
        }

        boolean spider = false;
        Position spiderPos = null;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("spider")) {
                spider = true;
                spiderPos = e.getPosition();
            }
        }

        assertEquals(false, spider);
        assertEquals(null, spiderPos);
    }

    @Test
    public void randomZombieSpawn() {

        World w = new World("simple-zombie-spawning-world", "standard", 8);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-zombie-spawning-world" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        DungeonResponse d = null;
        for (int i =0; i<20; i++) {
            d = w.tick(null, Direction.UP);
        }

        boolean zombie = false;
        Position zombiePos = null;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("zombie_toast")) {
                zombie = true;
                zombiePos = e.getPosition();
            }
        }

        assertEquals(true, zombie);
        assertNotNull(zombiePos);
        assertEquals(new Position(5, 1), zombiePos);
    }

    @Test
    public void randomZombieBattle() {
        World w = new World("simple-zombie-battle", "standard", 0);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-zombie-battle" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        DungeonResponse d = w.tick(null, Direction.UP);

        boolean zombie = false;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("zombie_toast")) {
                zombie = true;
            }
        }
        assertEquals(false, zombie);

        boolean armour = false;
        for (ItemResponse i: d.getInventory()) {
            if (i.getType().equals("armour")) {
                armour = true;
            }
        }

        assertEquals(true, armour);
    }

    @Test
    public void randomMercPeriodicSpawn() {

        World w = new World("simple-spider-spawning-world", "standard", 8);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-spider-spawning-world" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }


        DungeonResponse d = w.tick(null, Direction.RIGHT);
        w.tick(null, Direction.UP);
        for (int i =0; i<38; i++) {
            d = w.tick(null, Direction.LEFT);
        }

        boolean mercenary = false;
        for (EntityResponse e: d.getEntities()) {
            System.out.println(e.getType());
            if (e.getType().equals("mercenary") || e.getType().equals("assassin")) {
                mercenary = true;
            }
        }
        assertEquals(true, mercenary);
    }
    @Test
    public void randomHydraSpawn() {

        World w = new World("simple-spider-spawning-world", "hard", 8);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-spider-spawning-world" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }

        DungeonResponse d = null;

        for (int i =0; i<50; i++) {
            d = w.tick(null, Direction.UP);
        }

        boolean hydra = false;
        Position hydraPos = null;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("hydra")) {
                hydra = true;
                hydraPos = e.getPosition();
            }
        }

        assertEquals(true, hydra);
        assertNotNull(hydraPos);
        assertEquals(new Position(0, 4), hydraPos);
    }

    @Test
    public void randomHydraNoSpawn1() {

        World w = new World("simple-spider-spawning-world", "standard", 8);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-spider-spawning-world" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        DungeonResponse d = null;
        for (int i =0; i<50; i++) {
            d = w.tick(null, Direction.UP);
        }

        boolean hydra = false;
        Position hydraPos = null;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("hydra")) {
                hydra = true;
                hydraPos = e.getPosition();
            }
        }

        assertEquals(false, hydra);
        assertEquals(null, hydraPos);
    }

    @Test
    public void randomHydraNoSpawn2() {

        World w = new World("a-lot-of-boulders", "hard", 8);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "a-lot-of-boulders" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        DungeonResponse d = null;
        for (int i =0; i<20; i++) {
            d = w.tick(null, Direction.DOWN);
        }

        boolean hydra = false;
        Position hydraPos = null;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("hydra")) {
                hydra = true;
                hydraPos = e.getPosition();
            }
        }

        assertEquals(false, hydra);
        assertEquals(null, hydraPos);
    }

    @Test
    public void randomHydraAndurilSpawn() {

        World w = new World("simple-hydra-world", "hard", 8);

        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-hydra-world" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            w.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace
            ();
        }
        //Player moves down and collects the anduril
        DungeonResponse d = w.tick(null, Direction.DOWN);

        boolean anduril = false;
        for (ItemResponse i: d.getInventory()) {
            if (i.getType().equals("anduril")) {
                anduril = true;
            }
        }

        assertEquals(true, anduril);

        //Player moves down and battles with the hydra
        d = w.tick(null, Direction.DOWN);
        

        boolean hydra = false;
        boolean player = false;
        Position playerPos = null;
        Position hydraPos = null;
        for (EntityResponse e: d.getEntities()) {
            if (e.getType().equals("hydra")) {
                hydra = true;
                hydraPos = e.getPosition();
            }
            if (e.getType().equals("player")) {
                player = true;
                playerPos = e.getPosition();
            }
        }
        if(hydraPos == null) {
            assertEquals(true, player);
        }
        if (playerPos == null) {
            assertEquals(true, hydra);
        }
    }
}
