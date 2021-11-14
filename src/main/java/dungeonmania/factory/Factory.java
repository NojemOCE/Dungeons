package dungeonmania.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.goal.*;
import dungeonmania.logic.AndLogic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.logic.NoLogic;
import dungeonmania.logic.NotLogic;
import dungeonmania.logic.OrLogic;
import dungeonmania.logic.XorLogic;
import dungeonmania.movingEntity.MercenaryComponent;
import dungeonmania.movingEntity.Zombie;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.ZombieToastSpawn;
import dungeonmania.util.Position;

public abstract class Factory {
    Gamemode gamemode;
    protected int randomSeed;
    private int entityCount = 0;
    private int highestX = 5;
    private int highestY = 5;
    static final int MAX_SPIDERS = 6;
    static final int SPIDER_SPAWN = 20;
    private int tickCount = 1;
    static final double MERCENARY_ARMOUR_DROP = 0.4;
    static final double ZOMBIE_ARMOUR_DROP = 0.2;
    static final double ONE_RING_DROP = 0.1;
    protected Random ran;

    /**
     * Constructor for Factory taking in a GameMode
     * @param gamemode gamemode of factory
     * @param randomSeed
     */
    public Factory(Gamemode gamemode, int randomSeed) {
        this.gamemode = gamemode;
        this.randomSeed = randomSeed;
        this.ran = new Random(randomSeed);

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
     * Sets the entity count at a given int
     * @param entityCount entity count to set
     */
    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
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

        //Random ran1 = new Random(randomSeed);
        //Random ran2 = new Random(randomSeed);

        int x = ran.nextInt(highestX);
        int y = ran.nextInt(highestY);
        
        int numChecks = 0;
        while (!world.validSpiderSpawnPosition(new Position(x,y)) && numChecks < 10) {
            x = ran.nextInt(highestX);
            y = ran.nextInt(highestY);
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
        //Random random = new Random(randomSeed);
        while (!(possibleSpawnPositions.isEmpty())) {
            int posIndex = ran.nextInt(possibleSpawnPositions.size());
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
     * Gets the total entity count
     * @return current entity count
     */
    public int getEntityCount() {
        return entityCount;
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

    public int getHighestX() {
        return highestX;
    }

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

    /**
     * Drops armour:
     * 20% of the time if the player has beaten a zombie
     * 40% of the time if the player has beaten a mercenary/assassin
     *
     * Drops the one ring:
     * 10% of the time
     *
     * If an item is dropped, it is automatically added to the players inventory
     */
    public List<CollectableEntity> dropBattleReward(World world){

        List<CollectableEntity> battleRewards = new ArrayList<>();
        Position characterPos = world.getBattleCharacter().getPosition();
        int charX = characterPos.getX();
        int charY = characterPos.getY();


        if (world.getBattleCharacter() instanceof MercenaryComponent) {
            //Random ran = new Random(randomSeed);
            int next = ran.nextInt(10);
            if (10 * MERCENARY_ARMOUR_DROP > next)  {
                // return an armour
                Entity armour = createEntity(charX, charY, "armour", world);
                battleRewards.add((CollectableEntity) armour);
            }
        }

        else if (world.getBattleCharacter() instanceof Zombie) {
            //Random ran = new Random(randomSeed);
            int next = ran.nextInt(10);
            if (10 * ZOMBIE_ARMOUR_DROP > next)  {
                // return an armour
                Entity armour = createEntity(charX, charY, "armour", world);
                battleRewards.add((CollectableEntity) armour);
            }
        }

        //Random ran = new Random(randomSeed);
        int next = ran.nextInt(10);
        if (10 * ONE_RING_DROP > next)  {
            // return the one ring
            Entity oneRing = createEntity(charX, charY, "one_ring", world);
            battleRewards.add((CollectableEntity) oneRing);
        }

        return battleRewards;
    }

}
