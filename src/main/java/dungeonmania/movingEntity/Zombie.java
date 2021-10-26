package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.util.*;
import dungeonmania.response.models.EntityResponse;

import java.util.Random;

public class Zombie extends MovingEntity{

    public Zombie(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
        setAlly(false);
    }
  
    @Override
    public void move(World world) {
       randomMovement(world);
    }


    private void randomMovement(World world) {
        Random randomMovement = new Random();

        int upperbound = 4;
        int random = randomMovement.nextInt(upperbound);

        setPosition(validMove(this.getPosition().translateBy(Direction.UP), world));
        switch(random) {
            case 0:
                setPosition(validMove(this.getPosition().translateBy(Direction.UP), world)); 
                break;
            case 1:
                setPosition(validMove(this.getPosition().translateBy(Direction.DOWN), world));
                break;
            case 2:
                setPosition(validMove(this.getPosition().translateBy(Direction.LEFT), world));
                break;
            case 3:
                setPosition(validMove(this.getPosition().translateBy(Direction.RIGHT), world));
                break;
            default:
                // no change in position
                setPosition(getPosition());
        }        
    }

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "spider", getPosition(), false);
    }
}

