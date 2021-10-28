package dungeonmania.movingEntity;

import dungeonmania.inventory.Inventory;
import dungeonmania.util.Position;

public class Battle {
    private Player player;
    private MovingEntity character;
    private Position position;
    private Inventory inventory;

    private boolean playerWins;
    private boolean activeBattle;


    public Battle(Player player, MovingEntity character, Inventory inventory) {
        this.player = player;
        this.character = character;
        this.activeBattle = true;
        this.inventory = inventory;
    }

    public void battleTick() {

        // Base attack modified by inventory weapons
        double playerAttack = inventory.attackModifier(player.getAttackDamage());
        // Character attack modified by players defence weapons
        double characterAttack = inventory.defenceModifier(character.getAttackDamage());


        character.defend(playerAttack);
        player.defend(characterAttack);

        if (character.getHealthPoint().getHealth() == 0) {
            endBattle(true);
        }

        if (player.getHealthPoint().getHealth() == 0) {
            endBattle(false);
        }
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
        // TODO is this necessary the endBattle call
        player.endBattle();
        this.playerWins = playerWin;
    }

    public boolean getPlayerWins() {
        return this.playerWins;
    }

}
