package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.util.*;
import dungeonmania.response.models.EntityResponse;

import java.util.Random;

public class Zombie extends MovingEntity{


    public Zombie(int x, int y, String id, Gamemode gameMode) {
        //Attack damage set to 1 for now, layer set to 1
        super(new Position(x, y, 1), id, "zombie", new HealthPoint(gameMode.getStartingHP()), 1, gameMode);

        setMovement(new RandomMovement());

        setAlly(false);
    }
  
    @Override
    public void move(World world) {
       getMovement().move(this, world);
    }



}

