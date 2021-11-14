package dungeonmania.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.goal.*;
import dungeonmania.logic.AndLogic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.logic.NoLogic;
import dungeonmania.logic.NotLogic;
import dungeonmania.logic.OrLogic;
import dungeonmania.logic.XorLogic;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.ZombieToastSpawn;
import dungeonmania.util.Position;

public abstract class Factory {
    protected Gamemode gamemode;
    private Position playerStartingPos;
    protected int randomSeed;
    private int entityCount = 0;
    private int highestX = 5;
    private int highestY = 5;
    private static final int MAX_SPIDERS = 6;
    private static final int SPIDER_SPAWN = 20;
    private int tickCount = 1;
    static final int MERC_SPAWN_RATE = 40;

    /**
     * Constructor for Factory taking in a GameMode
     * @param gamemode gamemode of factory
     * @param randomSeed
     */
    public Factory(Gamemode gamemode, int randomSeed) {
        this.gamemode = gamemode;
        this.randomSeed = randomSeed;

    }

    /**
     * Gets the players starting position
     * @return players starting position
     */
    public Position getPlayerStartingPos() {
        return playerStartingPos;
    }

    /**
     * Sets the players starting position as a given input
     * @param playerStartingPos position to set players starting position as
     */
    public void setPlayerStartingPos(Position playerStartingPos) {
        this.playerStartingPos = playerStartingPos;
    }

    /**
     * Increments and returns the next entity coutn
     * @return new entity count
     */
    protected int incrementEntityCount() {
        entityCount +=1;
        return entityCount;
    }

    /**
     * Gets the total entity count
     * @return current entity count
     */
    public int getEntityCount() {
        return entityCount;
    }

    /**
     * Sets the entity count at a given int
     * @param entityCount entity count to set
     */
    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
    }

    /**
     * Setter for tickcount
     * @param tickCount tickcount to set
     */
    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }
    /**
     * Creates and returns an entity from a given JSON object, taking in the world
     * @param jsonObject JSON oject to create entity from
     * @param world world that the entity will be built into
     * @return entity
     */
    abstract public Entity createEntity(JSONObject jsonObject, World world);

    /**
     * Creates and returns an entity from x,y coordinates, string type, and current world
     * @param x x coordinate of the entity
     * @param y y coordinate of the entity
     * @param type string type of the entity
     * @param world world that the entity will be built into
     * @return entity
     */
    public Entity createEntity(int x, int y, String type, World world) {
        JSONObject newEntityObj = new JSONObject();
        newEntityObj.put("x", x);
        newEntityObj.put("y", y);
        newEntityObj.put("type", type);
        updateBounds(x, y);

        return createEntity(newEntityObj, world);
    }

    /**
     * Creates and returns an entity from x,y coordinates, string type, and current world
     * @param x x coordinate of the entity
     * @param y y coordinate of the entity
     * @param type string type of the entity
     * @param world world that the entity will be built into
     * @param logic the type of logic component
     * @return entity
     */
    public Entity createEntity(int x, int y, String type, World world, String logic) {
        JSONObject newEntityObj = new JSONObject();
        newEntityObj.put("x", x);
        newEntityObj.put("y", y);
        newEntityObj.put("type", type);
        newEntityObj.put("logic", logic);
        updateBounds(x, y);

        return createEntity(newEntityObj, world);
    }    

    /**
     * Taking the world, it returns a list of new entities that have spawned on that tick
     * @param world current world
     * @return list of new entities
     */
    public List<Entity> tick(World world) {
        List<Entity> newEntities =  new ArrayList<>();
        Entity spider = tickSpiderSpawn(world);
        if (!(spider == null)) {
            newEntities.add(spider);
        }

        List<Entity> newZombies = tickZombieToastSpawn(world);
        newEntities.addAll(newZombies);

        if (tickCount%MERC_SPAWN_RATE == 0) {
            newEntities.add(createEntity(playerStartingPos.getX(), playerStartingPos.getY(), "mercenary", world));
        }

        tickCount++;

        return newEntities;
    }

    /**
     * Helper function to create a new spider at relevant ticks
     */
    private Entity tickSpiderSpawn(World world) {
        if (!(tickCount % SPIDER_SPAWN == 0) || world.currentSpiders() == MAX_SPIDERS) {
            return null;
            
        }

        Random ran1 = new Random(randomSeed);
        Random ran2 = new Random(randomSeed);

        int x = ran1.nextInt(highestX);
        int y = ran2.nextInt(highestY);
        
        int numChecks = 0;
        while (!world.validSpiderSpawnPosition(new Position(x,y)) && numChecks < 10) {
            x = ran1.nextInt(highestX);
            y = ran2.nextInt(highestY);
            numChecks++;
        }

        // no valid positions found in reasonable time
        if (numChecks == 10) {
            return null;
        }

        Entity e = createEntity(x, y, "spider", world);
        return e;

    }

    /**
     * Updates zombie toast spawners
     */
    private List<Entity> tickZombieToastSpawn(World world) {
        List <Entity> newZombies = new ArrayList<>();

        for (StaticEntity s : world.getStaticEntities().values()) {
            if (s instanceof ZombieToastSpawn) {
                ZombieToastSpawn spawner = (ZombieToastSpawn) s;
                // update interactable state
                spawner.update(world.getPlayer());
                Entity e = spawnZombie(spawner, world);
                if (!(e == null)) {
                    newZombies.add(e);
                }
            }
        }
        return newZombies;
    }

    /**
     * Helper function to create a new zombie at relevant ticks
     * @param spawner Zombie spawner to spawn from
     */
    private Entity spawnZombie(ZombieToastSpawn spawner, World world) {

        if (!(tickCount % gamemode.getSpawnRate() == 0)) {
            return null;
        }
        List<Position> possibleSpawnPositions = spawner.spawn();
        Position newPos = getSpawnPosition(possibleSpawnPositions, world);
        if (newPos == null) {
            // no valid spawn positions
            return null;
        }

        Entity e = createEntity(newPos.getX(), newPos.getY(), "zombie_toast", world);
        //Zombie newZombie = new Zombie(newPos.getX(), newPos.getY(), "zombie_toast" + String.valueOf(incrementEntityCount()));
        //movingEntities.put(newZombie.getId(), newZombie);
        //player.subscribePassiveObserver((PlayerPassiveObserver) newZombie);
        return e;
    }
    
    /**
     * Get a random spawn position for new zombie
     * @param possibleSpawnPositions List of possible cardinally adjacent positions to a spawner
     * @return position to spawn, or null if no valid positions
     */
    private Position getSpawnPosition(List<Position> possibleSpawnPositions, World world) {
        Position newPos = null;
        Random random = new Random(randomSeed);
        while (!(possibleSpawnPositions.isEmpty())) {
            int posIndex = random.nextInt(possibleSpawnPositions.size());
            newPos = possibleSpawnPositions.get(posIndex);
            if (world.validZombieSpawnPosition(newPos)) {
                break;
            } else {
                possibleSpawnPositions.remove(posIndex);
            }
            newPos = null;
        }
        return newPos;
    }


    /**
     * Sets the largest bounds of the map
     * @param x x co-ordinate
     * @param y y co-ordinate
     */
    public void updateBounds(int x, int y) {
        if (x > highestX) {
            highestX = x;
        }
        if (y > highestY) {
            highestY = y;
        }
    }

    /**
     * Get the highest X
     * @return highest X
     */
    public int getHighestX() {
        return highestX;
    }

    /**
     * Get the highest Y
     * @return highest Y
     */
    public int getHighestY() {
        return highestY;
    }
    
    
    /**
     * Creates and returns a GoalComponent, taking in a JSON object
     * @param goal JSON object to build goal from
     * @return GoalComponent
     */
    public GoalComponent createGoal(JSONObject goal){
        String currGoal = goal.getString("goal");

        // Will return null if the goal is not exit,enemies,treasure, AND or OR
        if (currGoal.equals("exit")) {
            return new ExitGoal(currGoal);
        }
        else if (currGoal.equals("enemies")) {
            return new EnemiesGoal(currGoal);
        }
        else if (currGoal.equals("boulders")) {
            return new BoulderGoals(currGoal);
        }
        else if (currGoal.equals("treasure")) {
            return new TreasureGoals(currGoal);
        }
        else if (currGoal.equals("AND")) {
            AndGoal andGoal = new AndGoal(currGoal);
            JSONArray subGoals = goal.getJSONArray("subgoals");

            for (int i = 0; i < subGoals.length(); i++) {
                GoalComponent subGoal = createGoal(subGoals.getJSONObject(i));
                andGoal.addSubGoal(subGoal);
            }
            return andGoal; 
        }
        else if (currGoal.equals("OR")) {
            OrGoal orGoal = new OrGoal(currGoal);
            JSONArray subGoals = goal.getJSONArray("subgoals");

            for (int i = 0; i < subGoals.length(); i++) {
                GoalComponent subGoal = createGoal(subGoals.getJSONObject(i));
                orGoal.addSubGoal(subGoal);
            }
            return orGoal; 
        }

        return null;
    }
    

    /**
     * Creates a logic component given a logic string
     * @param logic String form of the logic
     * @return LogicComponent
     */
    public LogicComponent createLogicComponent(String logic) {
        if (logic.equals("and")) {
            return new AndLogic();
        } else if (logic.equals("or")) {
            return new OrLogic();
        } else if (logic.equals("xor")) {
            return new XorLogic();
        } else if (logic.equals("not")) {
            return new NotLogic();
        } else if (logic.equals("co_and")) {
            return null;
        } else if (logic.equals("no")) {
            return new NoLogic();
        } else {
            return null;
        }
    }

}
