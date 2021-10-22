package dungeonmania.staticEntityTest;


import dungeonmania.movingEntity.*;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;



public class ZombieToastSpawnTest {
    @Test
    public void constructionTest(){

        Position zombieSpawnPos = new Position(1,1);
        ZombieToastSpawn zombieSpawn = new ZombieToastSpawn(zombieSpawnPos);
        assertNotNull(zombieSpawn);
        assert(zombieSpawnPos.equals(zombieSpawn.getPosition()));

    }

    @Test
    public void zombieSpawn(){

        Position zombieSpawnPos = new Position(1,1);
        ZombieToastSpawn zombieSpawn = new ZombieToastSpawn(zombieSpawnPos);
        Zombie zombie = zombieSpawn.spawn();
        assert(zombieSpawnPos.equals(zombie.getPosition()));

    }
}
