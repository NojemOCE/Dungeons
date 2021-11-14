package dungeonmania.collectable;

import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;

public class Bomb extends CollectableEntity implements Logic {
    private LogicComponent logic; 

    /**
     * Constructor for bomb taking an x and a y coordinate, its unique itemID and logic component
     * @param x x coordinate of bomb
     * @param y y coordinate of bomb
     * @param itemId unique item id of bomb
     * @param logic logic component of the bomb
     */
    public Bomb(int x, int y, String itemId, LogicComponent logic) {
        super(x, y, itemId, "bomb");
        this.logic = logic;
    }

    /**
     * Constructor for bomb taking an x and a y coordinate, its unique itemID, durability and logic component
     * @param x x coordinate of bomb
     * @param y y coordinate of bomb
     * @param itemId unique item id of bomb
     * @param durability int value of durability
     * @param logic logic component of the bomb
     */
    public Bomb(int x, int y, String itemId, int durability, LogicComponent logic) {
        this(x, y, itemId, logic);
        setDurability(durability);
    }
    /**
     * Constructor for bomb taking its unique itemID, durability and logic component
     * @param itemId unique item id of bomb
     * @param durability int value of durability
     * @param logic logic component of the bomb
     */
    public Bomb(String itemId, int durability, LogicComponent logic) {
        this(0, 0, itemId, durability, logic);
    }

    @Override
    public LogicComponent getLogic() {
        return logic;
    }

    /**
     * Gets the logic string of the logic component of the bomb
     * @return logic strinng of the bomb
     */
    public String logicString() {
        return logic.logicString();
    }
}
