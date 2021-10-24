package dungeonmania.movingEntity;

import dungeonmania.util.*;
import dungeonmania.World;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.response.models.EntityResponse;

import java.util.Queue;
import java.util.Set;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Mercenary extends MovingEntity {

    private int BATTLE_RADIUS = 3;
    private Player subject;

    public Mercenary(int x, int y, String id, Gamemode gameMode) {
        //Set layer and attack damage to 1 for now
        super(new Position(x, y, 1), id, "mercenary", new HealthPoint(gameMode.getStartingHP()), 1, gameMode);

        setAlly(false);
    }


    @Override
    public void move(World world) {
        List<Position> path = bfs(world.getPlayer(), world);
        // maybe check for errors (list is empty or just has 1 etc...)
        if (!path.isEmpty()) {
            setPosition(getPosition().translateBy(Position.calculatePositionBetween(path.get(0), getPosition())));
        }
    }

    private List<Position> bfs(Player player, World world) {
        // validmove will keep track of whether we can reach the player (if it returns old position then not a valid path)
        // for each cell, get all adjacent, test for validmove, if position thats not old position returned, add to Queue
        // continue until player reached and then thats our path, go two positions if in battle radius
        Set<Position> visited = new HashSet<>();
        // start at where mercenary is
        // add position to queue
        Queue<Position> queue = new ArrayDeque<>();
        // gets merc position
        queue.add(getPosition());
        visited.add(getPosition());
        // we need a prev hashmap, <key,value> being <position, it's previous Position>
        Map<Position,Position> prev = new HashMap<>();
        prev.put(getPosition(), null);
        while(!queue.isEmpty()) {
            Position node = queue.remove();
            // now verify the adjacent neighbours
            Set<Position> neighbours = getNeighbours(node, world);
            for (Position p : neighbours) {
                if (!visited.contains(p)) { // for the neighbours that havent been visited
                    queue.add(p);
                    visited.add(p);
                    prev.put(p, node);
                }
            }
        }

        // backtrack through prev getting the reverse path
        List<Position> path = new ArrayList<>();
        for(Position p = player.getPosition(); p != null; p = prev.get(p)) {
            path.add(p);
        }
        Collections.reverse(path);

        return path;
    }

    private Set<Position> getNeighbours(Position position, World world) {
        List<Position> neighbours = position.getAdjacentPositions();

        Set<Position> validNeighbours = new HashSet<>();
        
        // only get valid neighbours
        validNeighbours.add(validMove(neighbours.get(1), world));
        validNeighbours.add(validMove(neighbours.get(3), world));
        validNeighbours.add(validMove(neighbours.get(5), world));
        validNeighbours.add(validMove(neighbours.get(7), world));

        validNeighbours.removeIf(filter -> filter.equals(position));

        return validNeighbours;
    }

    public void update(Movement movement) {
        // take in duration left,
        //after duration is 0 revert back to normal pattern;
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), true);
    }
}
