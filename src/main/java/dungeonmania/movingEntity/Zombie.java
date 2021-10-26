package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.util.*;
import dungeonmania.response.models.EntityResponse;

import java.util.Random;

public class Zombie extends MovingEntity{

    public Zombie(int x, int y, String id) {
        super(new Position(x, y), id, "zombie", new HealthPoint(100), 10);
        setMovement(new RandomMovement());
        setAlly(false);
    }
  
    @Override
    public void move(World world) {
       getMovement().move(this, world);
    }



    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "spider", getPosition(), false);
    }
}

