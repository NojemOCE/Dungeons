package dungeonmania;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import dungeonmania.gamemode.Gamemode;

public abstract class Factory {
    Gamemode gamemode;
    int entityCount = 0;
    int highestX = 5;
    int highestY = 5;

    public Factory(Gamemode gamemode) {
        this.gamemode = gamemode;
    }

    protected int incrementEntityCount() {
        entityCount +=1;
        return entityCount;
    }

    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
    }

    abstract public Entity createEntity(JSONObject jsonObject, World world);

    public Entity createEntity(int x, int y, String type, World world) {
        JSONObject newEntityObj = new JSONObject();
        newEntityObj.put("x", x);
        newEntityObj.put("y", y);
        newEntityObj.put("type", type);
        updateBounds(x, y);

        return createEntity(newEntityObj, world);
    }

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
    
    
}
