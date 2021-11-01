package dungeonmania.movingEntityTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.InputValueSwitch;

import dungeonmania.DungeonManiaController;
import dungeonmania.World;
import dungeonmania.exceptions.*;
import dungeonmania.gamemode.Standard;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.collectable.InvincibilityPotion;
import dungeonmania.collectable.InvisibilityPotion;
import dungeonmania.inventory.Inventory;
import dungeonmania.movingEntity.*;
import dungeonmania.movingEntity.MovementStrategies.CircleMovement;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class CharacterTest {


    @Test
    public void constructTest() {
        // Testing construction + basic getters

        HealthPoint playerHealth = new HealthPoint(100);

        Player player = new Player(1, 1, "player", playerHealth);

        // Ensure attributes are correct
        assertEquals(player.getHealthPoint().getHealth(), 100);
        assertEquals(player.getHealthPoint().getMaxHealth(), 100);
        assertEquals(player.getAttackDamage(), 3);
        assertEquals(player.getPosition(), new Position(1, 1));

    }


    @Test
    public void testPlayerMovement() {
        World world = new World("test", "Standard");
        
        HealthPoint playerHealth = new HealthPoint(100);

        Player player = new Player(2, 2, "player", playerHealth);

        player.tick(Direction.UP, world);
        assertEquals(player.getPosition(), new Position(2, 1));

        player.tick(Direction.RIGHT, world);

        assertEquals(player.getPosition(), new Position(3, 1));

    }

    @Test
    public void testCharacterMovement() {
        World world = new World("key+door-noMatch", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "key+door-noMatch" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Spider spider = new Spider(1,1,"spider");
        Zombie zombie = new Zombie(1,1,"zombie");

        assertEquals(spider.getPosition(), new Position(1, 1));
        spider.move(world);
        assertEquals(spider.getPosition(), new Position(1, 0));
        spider.move(world);
        assertEquals(spider.getPosition(), new Position(2, 0));

        Position current = zombie.getPosition();
        zombie.move(world);
        assert(Position.isAdjacent(current, zombie.getPosition()));
        zombie.move(world);
        zombie.move(world);
        zombie.move(world);
        zombie.move(world);
        zombie.move(world);
        zombie.move(world);
        zombie.move(world);
        zombie.updateMovement("invincibility_potion");
        zombie.updateMovement("N/A");

        spider.setMovement(new CircleMovement("UP", "DOWN", 1, 1, true));
    }

    @Test
    public void testBattle() {

        HealthPoint playerHealth = new HealthPoint(30);
        Player player = new Player(1, 1, "player", playerHealth);
        Mercenary mercenary =  new Mercenary(1,1,"merc");
        Inventory inventory = new Inventory();
        Battle battle = new Battle(player, mercenary, true);

        // on same cell, battle should be created

        double playerAttack = (player.getHealthPoint().getHealth() * player.getAttackDamage())/10;
        // Character attack modified by players defence weapons
        double characterAttack = (mercenary.getHealthPoint().getHealth() * mercenary.getAttackDamage())/10;
        battle.battleTick(inventory);

        assertEquals(player.getHealthPoint().getHealth(), player.getHealthPoint().getMaxHealth() - characterAttack);

        assertEquals(mercenary.getHealthPoint().getHealth(), (mercenary.getHealthPoint().getMaxHealth() - playerAttack));

        assertEquals(battle.isActiveBattle(),true);

        battle.battleTick(inventory);
        battle.battleTick(inventory);

        assertEquals(battle.isActiveBattle(),false);
        assertEquals(battle.getPlayerWins(), true);

    }

    @Test
    public void testMercenaryBFS() {
        World world = new World("merc", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "advanced" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Mercenary merc = (Mercenary) world.getCharacter(new Position(3,5));
        world.tick(null, null);
        assertEquals(merc.getPosition(), new Position(2, 5));
        world.tick(null, null);
        assertEquals(merc.getPosition(), new Position(1, 5));
        world.tick(null, null);
        assertEquals(merc.getPosition(), new Position(1, 4));

        world.getPlayer().tick(Direction.RIGHT, world);
        world.tick(null, null);
        assertEquals(merc.getPosition(), new Position(1, 3));
        world.tick(null, null);
        assertEquals(merc.getPosition(), new Position(1, 2));

        world.tick(null, null);
        assertEquals(merc.getPosition(), new Position(1, 1));
        world.tick(null, null);

        assertEquals(merc.getPosition(), new Position(2, 1));
        world.tick(null, null);
        assertEquals(merc.getPosition(), new Position(2, 1)); // on top of player
        assertEquals(world.getBattle().isActiveBattle(), true);
        // recalculate bfs
    }

    @Test
    public void testRunAway() {
        World world = new World("merc", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "advanced" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Mercenary merc = (Mercenary) world.getCharacter(new Position(3,5));
        CollectableEntity potion = world.getCollectableEntity(new Position(11,10));
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 2; i++) {
            world.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 2; i++) {
            world.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.DOWN);
        }
        world.tick(null, Direction.UP);

        Spider spider = new Spider(1,1,"spider");
        world.getPlayer().subscribePassiveObserver((PlayerPassiveObserver)spider);
        // Test for mercenary runningaway
        assertEquals(world.getPlayer().getPosition(), new Position(11, 10));
        world.tick(potion.getId(), null);
        world.getPlayer().saveGameJson();

        assertEquals(world.getPlayer().getAttackDamage(), 999);
        world.tick(null, Direction.UP);
        world.tick(null, Direction.UP);
        world.getPlayer().saveGameJson();

        assertEquals(merc.getMovement().getMovementType(), "runAway");
        world.tick(null, Direction.LEFT);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.UP);
        world.tick(null, Direction.UP);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.UP);
        world.tick(null, Direction.RIGHT);


        world.getPlayer().addPotion(new InvisibilityPotion(10));
        world.tick(null, Direction.LEFT);
        world.tick(null, Direction.LEFT);

        assertEquals(world.getPlayer().getAttackDamage(), 3);

    }

    @Test
    public void testSaveJSON() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "Standard");
        for (int i = 0; i < 4; i++) {
            controller.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 4; i++) {
            controller.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 2; i++) {
            controller.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 2; i++) {
            controller.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 4; i++) {
            controller.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 4; i++) {
            controller.tick(null, Direction.DOWN);
        }
        controller.tick(null, Direction.UP);

        DungeonResponse a = controller.tick(null,null);
        String id = "";
        for (ItemResponse i : a.getInventory()) {
            if (i.getType().equals("invincibility_potion")) {
                id = i.getId();
            }
        }
        controller.tick(id, null);
        controller.tick(null, null);

        controller.saveGame("advanced");
        
        assertDoesNotThrow(() -> controller.loadGame("advanced"));

    }

    @Test
    public void testMercOutofRange() {
        World world = new World("merc", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "advanced" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Mercenary merc = (Mercenary) world.getCharacter(new Position(3,5));
        CollectableEntity potion = world.getCollectableEntity(new Position(11,10));
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 2; i++) {
            world.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 2; i++) {
            world.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.DOWN);
        }
        world.tick(null, Direction.UP);

        Zombie zombie = new Zombie(1,1,"zambie");
        world.getPlayer().subscribePassiveObserver((PlayerPassiveObserver)zombie);
        // Test for mercenary runningaway
        assertEquals(world.getPlayer().getPosition(), new Position(11, 10));
        world.tick(potion.getId(), null);
        assertEquals(world.getPlayer().getAttackDamage(), 999);
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.DOWN);

        assertEquals(merc.getMovement().getMovementType(), "runAway");
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.UP);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.UP);
        world.tick(null, Direction.RIGHT);


        assertEquals(world.getPlayer().getAttackDamage(), 3);

    }

    @Test
    public void testBribe() {
        World world = new World("merc", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "advanced" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        world.getPlayer().addHealth(1);

        Mercenary merc = (Mercenary) world.getCharacter(new Position(3,5));
        assertThrows(InvalidActionException.class, () -> world.interact(merc.getId()));

        CollectableEntity treasure = world.getCollectableEntity(new Position(7,10));
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 4; i++) {
            world.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 2; i++) {
            world.tick(null, Direction.RIGHT);
        }
        assertThrows(InvalidActionException.class, () -> world.interact(merc.getId()));

        for (int i = 0; i < 5; i++) {
            world.tick(null, Direction.DOWN);
        }
        
        world.tick(null, Direction.UP);
        world.interact(merc.getId());
        assertEquals(merc.getAlly(), true);
        world.tick(null, null);
        
        assertEquals(world.getPlayerPosition(), new Position(7,9));
        Zombie zambie = new Zombie(7, 9, "zombie");
        world.getPlayer().subscribePassiveObserver(zambie);
        Battle battle = world.getPlayer().battle(zambie, new Standard());
        
        battle.battleTick(new Inventory());
        assertEquals(zambie.getHealthPoint().getHealth(), 0);
        
        assertEquals(world.inInventory(treasure), false);

        Zombie zambie1 = new Zombie(7, 9, "zombie1");

        world.getPlayer().addPotion(new InvincibilityPotion("a", 1, true));

        Battle battle1 = world.getPlayer().battle(zambie1, new Standard());
        battle1.battleTick(new Inventory());
        Zombie zambie2 = new Zombie(7, 9, "zombie2");

        world.getPlayer().addPotion(new InvisibilityPotion(10));

        Battle battle2 = world.getPlayer().battle(zambie2, new Standard());
        assertEquals(battle2, null);
    }

    /**
     * Test that the game ends when the player dies
     */
    @Test
    public void testGameOver() {
        World world = new World("player-gameOver-test", "Peaceful");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "player-gameOver-test" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        // Battle battle1 = world.getPlayer().battle(zambie1, new Standard());
        world.getPlayer().defend(100);
        d = world.tick(null, null);

        List<EntityResponse> entities = d.getEntities();
        
        boolean noPlayer = true;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                noPlayer = false;
                break;
            }
        }

        assertTrue(noPlayer);

    }

    /**
     * Test no valid spider spawn positions
     */
    @Test
    public void testInvalidSpiderSpawn() {
        World world = new World("a-lot-of-boulders", "Peaceful");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "a-lot-of-boulders" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // tick 21 times and make sure there are no spiders on the map

        DungeonResponse d;
        List<EntityResponse> entities;
        for (int i = 0; i < 21; i++) {
            d = world.tick(null, null);
            entities = world.getEntityResponses();
            for (EntityResponse er : entities) {
                assertFalse(er.getType().equals("spider"));
            }
        }

    }

}