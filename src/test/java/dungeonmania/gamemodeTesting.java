package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.gamemode.*;

public class gamemodeTesting {

    /**
     * In standard mode, standard game rules should apply
     */
    @Test
    public void testStandard() {
        Gamemode standardMode = new Standard();
        assertTrue(standardMode.getSpawnRate() == 20);
        assertTrue(standardMode.isEnemyAttackEnabled());
        assertTrue(standardMode.isInvincibilityEnabled());
    }

    
    /**
     * In peaceful mode, enemies do not attack character
     */
    @Test
    public void testPeaceful() {
        Gamemode peacefulMode = new Peaceful();
        assertFalse(peacefulMode.isEnemyAttackEnabled());
    }

    /**
     * In hard mode:
     *  - spawn rate is 15
     *  - player has less health than standard
     *  - invicibility has no effect
     */
    @Test
    public void testHard() {
        Gamemode hardMode = new Hard();
        Gamemode standardMode = new Standard();

        assertTrue(hardMode.getSpawnRate() == 15);
        assertTrue(hardMode.getStartingHP() < standardMode.getStartingHP());
        assertTrue(hardMode.isInvincibilityEnabled() == false);
    }
}
