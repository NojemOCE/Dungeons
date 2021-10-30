package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.google.gson.JsonParser;

import org.eclipse.jetty.util.IO;
import org.json.*;

public class DungeonManiaController {

    // saved worlds
    private List<World> savedGames = new ArrayList<>();
    private World current;

    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("Standard", "Peaceful", "Hard");
    }

    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        
        if (!dungeons().contains(dungeonName)) throw new IllegalArgumentException(dungeonName  + "is not a dungeon. Please select a valid dungeon.");
        if (!(getGameModes().contains(gameMode))) throw new IllegalArgumentException(gameMode + " is not a valid game mode.");

        World newGame = new World(dungeonName, gameMode);
        // create JSON object
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
            
            JSONObject game = new JSONObject(file);
            
            newGame.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
            // TODO discard world if exception thrown?
        }

        this.current = newGame;


        return current.worldDungeonResponse();
    }
    
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        // if no game is loaded then we can't save
        if (current.equals(null)) {
            throw new IllegalArgumentException("No game currently loaded");
        }

        JSONObject saveState = current.saveGame();
        //TODO save game
        return current.worldDungeonResponse();
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        return current.worldDungeonResponse();
    }

    public List<String> allGames() {
        return new ArrayList<>();
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws InvalidActionException, IllegalArgumentException {
        current.tick(itemUsed, movementDirection);

        return current.worldDungeonResponse();
    }

    /**
     * Interacts with a mercenary (where the character bribes the mercenary) 
     * or a zombie spawner, where the character destroys the spawner.
     * @param entityId id of the entity to be interacted with
     * @return DungeonResponse of the game state after interact
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        // check that id is valid
        current.interact(entityId);

        return current.worldDungeonResponse();

    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        current.build(buildable);
        return current.worldDungeonResponse();

    }

}