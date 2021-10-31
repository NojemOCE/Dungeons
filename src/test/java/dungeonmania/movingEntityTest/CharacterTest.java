package dungeonmania.movingEntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.World;
import dungeonmania.inventory.Inventory;
import dungeonmania.movingEntity.*;
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
        assertEquals(player.getHealthPoint(), new HealthPoint(100));
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
        zombie.move(world);
        assertEquals(spider.getPosition(), new Position(1, 0));
        spider.move(world);
        assertEquals(spider.getPosition(), new Position(2, 0));

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
}