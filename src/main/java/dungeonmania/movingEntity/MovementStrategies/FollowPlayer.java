package dungeonmania.movingEntity.MovementStrategies;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import dungeonmania.World;
import dungeonmania.movingEntity.*;
import dungeonmania.util.Position;

public class FollowPlayer implements Movement {

    @Override
    public void move(MovingEntity me, World world) {
        List<Position> path = bfs(me, world.getPlayer(), world);
        // maybe check for errors (list is empty or just has 1 etc...)
        // calculate distance inbetween player and mercenary, if in battle radius 
        // use path.get(1) 

        if (!path.isEmpty() && path.size() >= 1) {
            // still need to use the battle radius, translate by two
            // THE PATH MAY INCLUDE THE START, if MERC NOT MOVING CHANGE me.getSpeed() +1`
            me.setPosition(path.get(1));
        }
    }

    /**
     * BFS method used to follow the player. Returns the shortest path to the player from the given moving entity
     * @param me moving entity
     * @param player player to follow
     * @param world current game world
     * @return shortest path from moving entity to player
     */
    private List<Position> bfs(MovingEntity me, Player player, World world) {
        // validmove will keep track of whether we can reach the player (if it returns old position then not a valid path)
        // for each cell, get all adjacent, test for validmove, if position thats not old position returned, add to Queue
        // continue until player reached and then thats our path, go two positions if in battle radius
        Set<Position> visited = new HashSet<>();
        // start at where mercenary is
        // add position to queue
        Queue<Position> queue = new ArrayDeque<>();
        // gets merc position
        queue.add(me.getPosition());
        visited.add(me.getPosition());
        // we need a prev hashmap, <key,value> being <position, it's previous Position>
        Map<Position,Position> prev = new HashMap<>();
        prev.put(me.getPosition(), null);
        while(!queue.isEmpty()) {
            Position node = queue.remove();
            // now verify the adjacent neighbours
            Set<Position> neighbours = getNeighbours(me, node, world);
            for (Position p : neighbours) {
                if (!visited.contains(p)) { // for the neighbours that havent been visited
                    queue.add(p);
                    visited.add(p);
                    prev.put(p, node);
                    if (p.equals(player.getPosition())) break; // player is found and added, break

                }
            }
        }

        // backtrack through prev getting the reverse path
        List<Position> path = new ArrayList<>();

        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        if (!Objects.isNull(prev.get(new Position(x,y)))) {
            for(Position p = new Position(x, y); p != null; p = prev.get(p)) {
                path.add(p);
            }
        }

        
        Collections.reverse(path);

        return path;
    }

    /**
     * Returns the set of positions neighbouring a given position that would be a valid move for a given entity
     * @param me entity
     * @param position position to check neighbours of
     * @param world current game world
     * @return set of valid neighbour positions
     */
    private Set<Position> getNeighbours(MovingEntity me, Position position, World world) {
        List<Position> neighbours = position.getAdjacentPositions();

        Set<Position> validNeighbours = new HashSet<>();
        
        // only get valid neighbours

        validNeighbours.add(me.validMove(neighbours.get(1), world));
        validNeighbours.add(me.validMove(neighbours.get(3), world));
        validNeighbours.add(me.validMove(neighbours.get(5), world));
        validNeighbours.add(me.validMove(neighbours.get(7), world));

        validNeighbours.removeIf(filter -> filter.equals(position));

        return validNeighbours;
    }
}