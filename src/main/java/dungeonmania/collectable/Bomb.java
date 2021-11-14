package dungeonmania.collectable;

import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;

public class Bomb extends CollectableEntity implements Logic {
    private LogicComponent logic; 

    public Bomb(int x, int y, String itemId, LogicComponent logic) {
        super(x, y, itemId, "bomb");
        this.logic = logic;
    }

    public Bomb(int x, int y, String itemId, int durability, LogicComponent logic) {
        this(x, y, itemId, logic);
        setDurability(durability);
    }
    
    public Bomb(String itemId, int durability, LogicComponent logic) {
        this(0, 0, itemId, durability, logic);
    }

    @Override
    public LogicComponent getLogic() {
        return logic;
    }

    public String logicString() {
        return logic.logicString();
    }
}
