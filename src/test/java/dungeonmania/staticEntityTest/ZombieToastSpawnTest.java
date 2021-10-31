package dungeonmania.staticEntityTest;


import dungeonmania.movingEntity.*;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Position;
import dungeonmania.TestHelpers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.Test;



public class ZombieToastSpawnTest {
    @Test
    public void constructionTest(){

        Position zombieSpawnPos = new Position(1,1);
        ZombieToastSpawn zombieSpawn = new ZombieToastSpawn(1, 1, "1");
        assertNotNull(zombieSpawn);
        assert(zombieSpawnPos.equals(zombieSpawn.getPosition()));

    }

    @Test
    public void zombieSpawn(){

        Position zombieSpawnPos = new Position(1,1);
        ZombieToastSpawn zombieSpawn = new ZombieToastSpawn(1, 1, "1");
        List<Position> zombieSpawnPositions = zombieSpawn.spawn();
        List <Position> expected = new ArrayList<>();
        expected.add(new Position(0, 1));
        expected.add(new Position(1, 0));
        expected.add(new Position(2, 1));
        expected.add(new Position(1, 2));
        //assertListAreEqualIgnoringOrder(expected, zombieSpawnPositions);

    }
}
