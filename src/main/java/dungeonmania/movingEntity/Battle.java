package dungeonmania.movingEntity;

import dungeonmania.util.Position;

public class Battle {
    private Player player;
    private MovingEntity character;
    private Position position;

    public Battle(Player player, MovingEntity character) {
        this.player = player;
        this.character = character;
    }

    public void battleTick() {

    }

    public Player getPlayer() {
        return player;
    }

    public MovingEntity getCharacter() {
        return character;
    }

    public Position getPosition() {
        return position;
    }

    private void endBattle() {

    }
}
