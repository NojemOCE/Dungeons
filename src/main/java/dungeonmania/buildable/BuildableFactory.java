package dungeonmania.buildable;

public class BuildableFactory {

    public static Buildable getBuildable(String bType) {
        if (bType == null) {
            return null;
        } else if (bType.equalsIgnoreCase("bow")) {
            return new Bow();
        } else if (bType.equalsIgnoreCase("shield")) {
            return new Shield();
        }
        return null;
    }

}
