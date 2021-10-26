package dungeonmania.movingEntity;

import dungeonmania.util.*;
import dungeonmania.World;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.movingEntity.MovementStrategies.FollowPlayer;
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
import java.util.Objects;

public class Mercenary extends MovingEntity {

    private double BATTLE_RADIUS = 10;
    private Player subject;

    public Mercenary(int x, int y, String id, Gamemode gameMode) {
        //Set layer and attack damage to 1 for now
        super(new Position(x, y, 1), id, "mercenary", new HealthPoint(gameMode.getStartingHP()), 1, gameMode);

        setAlly(false);
    }


    @Override
    public void move(World world) {

        Position distance = Position.calculatePositionBetween(world.getPlayer().getPosition(), this.getPosition());
        double x = (double)distance.getX();
        double y = (double)distance.getY();
        double distanceSquared = ((x*x) + (y*y));
        if ((Math.pow(distanceSquared, 1/2)) <= BATTLE_RADIUS) {
            // mount player as in range
            world.getPlayer().registerEntity(this);
            this.subject = world.getPlayer();
        } else {
            this.subject = null;
            world.getPlayer().unregisterEntity(this);
        }
        
        getMovement().move(this, world);
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
