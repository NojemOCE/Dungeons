package dungeonmania.character;

import dungeonmania.util.*;
import dungeonmania.World;
import java.util.Queue;
import java.util.ArrayDeque;

public class Mercenary extends Character {

    private int BATTLE_RADIUS = 3;
    
    public Mercenary(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
        setAlly(false);
    }


    @Override
    public void move(World world) {
        followPlayer(world.getPlayer());
        return;
    }

    private void followPlayer(Player player) {
        // validmove will keep track of whether we can reach the player (if it returns old position then not a valid path)
        // for each cell, get all adjacent, test for validmove, if position thats not old position returned, add to Queue
        // continue until player reached and then thats our path, go two positions if in battle radius
        
        // start at where mercenary is
        // add position to queue
        Queue<Position> queue = new ArrayDeque<>();
        
    }


}
