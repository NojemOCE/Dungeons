package dungeonmania;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.collectable.*;




public class collectableTesting {
     /**
      * Test whether a collected entity is placed within the players inventory and removed from the map
      */
     @Test
     public void testCollected() {

         Armour armour = new Armour(1, 1, "armour1");
         armour.collect();
         assertTrue(armour.isCollected());

         Arrows arrows = new Arrows(1, 1, "arrows2");
         arrows.collect();
         assertTrue(arrows.isCollected());

         Bomb bomb = new Bomb(1, 1, "bomb3");
         bomb.collect();
         assertTrue(bomb.isCollected());

         HealthPotion hpot = new HealthPotion(1, 1, "health_potion4");
         hpot.collect();
         assertTrue(hpot.isCollected());

         InvincibilityPotion invinc = new InvincibilityPotion(1, 1, "invincibility_potion5", true);
         invinc.collect();
         assertTrue(invinc.isCollected());

         InvisibilityPotion invis = new InvisibilityPotion(1, 1, "invisibility_potion6");
         invis.collect();
         assertTrue(invis.isCollected());

         Key key = new Key(1, 1, "key7", 0);
         key.collect();
         assertTrue(key.isCollected());

         OneRing ring = new OneRing(1, 1, "onering8");
         ring.collect();
         assertTrue(ring.isCollected());

         Treasure treasure = new Treasure(1, 1, "treasure9");
         treasure.collect();
         assertTrue(treasure.isCollected());

         Wood wood = new Wood(1, 1, "wood10");
         wood.collect();
         assertTrue(wood.isCollected());
     }
}
