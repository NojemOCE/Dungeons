package dungeonmania.movingEntity.MovementStrategies;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.*;
import dungeonmania.util.Position;

public class FollowPlayer implements MovementStrategy {

    @Override
    public void move(MovingEntity me, World world) {


        List<Position> path = getPath(dijkstras(me, world.getPlayer(), bfs(me, world.getPlayer(),world), world), world.getPlayer());
        // maybe check for errors (list is empty or just has 1 etc...)
        // calculate distance inbetween player and mercenary, if in battle radius 
        // use path.get(1)
        if (path.size() < 1) return;
        if (path.get(1).equals(world.getPlayer().getPosition())) {
            if (!Objects.isNull(world.getBattle()) || me.getAlly()) me.setPosition(path.get(0));
            else me.setPosition(me.validMove(path.get(1), world));
        } else me.setPosition(me.validMove(path.get(1), world));


       
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
        // gets merc position
        visited.add(me.getPosition());
        // we need a prev hashmap, <key,value> being <position, it's previous Position>
        //Map<Position,Position> prev = new HashMap<>();
        //prev.put(me.getPosition(), null);
    
        Queue<Position> queue = new ArrayDeque<>();
        queue.add(me.getPosition());
        while(!queue.isEmpty()) {
            Position node = queue.remove();
            // now verify the adjacent neighbours
            Set<Position> neighbours = getNeighbours(me, node, world);
            for (Position p : neighbours) {
                if (!visited.contains(p)) { // for the neighbours that havent been visited
                    queue.add(p);
                    visited.add(p);
                    //prev.put(p, node);
                    //if (p.equals(player.getPosition())) break;

                }
            }
        }
        return new ArrayList<>(visited);
    }

    private Map<Position, Position> dijkstras(MovingEntity me, Player player, List<Position> visited, World world) {
        Map<Position, Position> prev = new HashMap<>();
        Map<Position, Double> dist = new HashMap<>();

        visited.forEach(position -> {
            dist.put(position, 99.0);
            prev.put(position, null);
        });

        dist.put(me.getPosition(), 0.0);

        while(!visited.isEmpty()) {
            Position u = getSmallestDistance(dist, visited);
            visited.remove(u);
            for (Position v: getNeighbours(me, u, world)) {
                if (dist.get(u) + world.getDistance(v) < dist.get(v)) { // TODO CHANGE 1.0 to actual cost
                    dist.put(v, dist.get(u) + world.getDistance(v));
                    prev.put(v,u);
                } 
            }
        }
        return prev;
    }

    private Position getSmallestDistance(Map<Position, Double> dist, List<Position> visited) {
        Position smallest = new Position(0, 0);
        double currDistance = 99.0;
        for (Position p : dist.keySet()) {
            if (visited.contains(p) && dist.get(p) <= currDistance){
                currDistance = dist.get(p);
                smallest = p;
            }
        }
        return smallest;
    }

    private List<Position> getPath(Map<Position, Position> prev,Player player) {
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

        for (int i = 1; i <= 7; i = i + 2) {
            if (neighbours.get(i).equals(world.getPlayer().getPosition())) validNeighbours.add(neighbours.get(i));
        }
        for (int i = 1; i <= 7; i = i + 2) {
            validNeighbours.add(me.validMove(neighbours.get(i), world));
        }

        validNeighbours.removeIf(filter -> filter.equals(position));

        return validNeighbours;
    }

    @Override
    public String getMovementType() {
        return "followPlayer";
    }

    @Override
    public JSONObject getMovementJson() {
        JSONObject obj = new JSONObject();
        obj.put("movement", getMovementType());
        return obj;
    }
}