package dungeonmania.factory;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.gamemode.Gamemode;

public abstract class Factory {
    Gamemode gamemode;
    int entityCount = 0;

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

        return createEntity(newEntityObj, world);
    }

    public int getEntityCount() {
        return entityCount;
    }
    
}
