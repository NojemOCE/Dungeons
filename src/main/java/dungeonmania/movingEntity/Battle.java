package dungeonmania.movingEntity;

import dungeonmania.util.Position;

public class Battle {
    private Player player;
    private MovingEntity character;
    private Position position;

    private boolean activeBattle;

    public Battle(Player player, MovingEntity character) {
        this.player = player;
        this.character = character;
        this.activeBattle = true;
    }

    public void battleTick() {
        // 
        endBattle();
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

    public boolean isActiveBattle() {
        return activeBattle;
    }

    public void setActiveBattle(boolean activeBattle) {
        this.activeBattle = activeBattle;
    }

    private void endBattle() {
        setActiveBattle(false);
    }
}
