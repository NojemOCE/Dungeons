package dungeonmania.character;

import dungeonmania.util.*;
import dungeonmania.World;


public class Mercenary extends Character {

    
    public Mercenary(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
        setAlly(false);
    }


    @Override
    public void move(World world) {
        followPlayer(world.getPlayer());
        return;
    }

    private void followPlayer(Player player){
        
    }


}
