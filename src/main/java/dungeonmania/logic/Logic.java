package dungeonmania.logic;

public interface Logic {
    public LogicComponent getLogic();
    public void addObserver(LogicComponent e);
    public void removeObserver(LogicComponent e);
}
