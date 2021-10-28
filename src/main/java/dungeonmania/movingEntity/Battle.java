package dungeonmania.movingEntity;

import dungeonmania.inventory.Inventory;
import dungeonmania.util.Position;

public class Battle {
    private Player player;
    private MovingEntity character;
    private Position position;

    private boolean playerWins;
    private boolean activeBattle;


    public Battle(Player player, MovingEntity character) {
        this.player = player;
        this.character = character;
        this.activeBattle = true;
    }

    public void battleTick(Inventory inventory) {
        //
        character.defend(player.attack(inventory.attackModifier(player.getAttackDamage())));

        if (character.getHealthPoint().getHealth() == 0) endBattle(true);

        player.defend(inventory.defenceModifier(character.getAttackDamage()));

        if (player.getHealthPoint().getHealth() == 0) endBattle(false);
        // game over
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

    private void endBattle(boolean playerWin) {
        setActiveBattle(false);
        player.endBattle();
        this.playerWins = playerWin;
    }

    public boolean getPlayerWins() {
        return this.playerWins;
    }

}
