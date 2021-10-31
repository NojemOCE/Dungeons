package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.gson.JsonParser;

import org.eclipse.jetty.util.IO;
import org.json.*;

public class DungeonManiaController {

    private World current;

    public DungeonManiaController() {
        this.current = null;
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
        if (current == null) {
            throw new IllegalArgumentException("No game currently loaded");
        }

        if (!current.getId().equals(name)) {
            throw new IllegalArgumentException("You cannot save a game that is not currently loaded.");
        }

        JSONObject saveState = current.saveGame();
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
         
        //Calendar calendar = Calendar.getInstance();
        //String dateStr = new SimpleDateFormat("ddMMyyyyHHmmss").format(calendar.getTime());

        String dungeonID = current.getDungeonName() + "-"+ dateStr;
        current.setId(dungeonID);

        //WRITE TO FILE AND ADD to saved games list

        saveState.put("id", dungeonID);

        File saveDirectory = new File("src/main/resources/savedGames");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdir();
        }

        try {   
            File output = new File(saveDirectory + "/" + dungeonID + ".json");
            output.createNewFile();
            FileWriter fileWriter = new FileWriter(output);

            fileWriter.write(saveState.toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return current.worldDungeonResponse();
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        if(!allGames().contains(name)) {
            throw new IllegalArgumentException("Please load a valid game.");
        }

        World newGame = null;
        try {
            String file = FileLoader.loadResourceFile("/savedGames/" + name + ".json");
            
            JSONObject game = new JSONObject(file);
            
            newGame = new World(game.getString("dungeon-name"), game.getString("gamemode"), game.getString("id"));

            newGame.buildWorldFromFile(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        current = newGame;
        return current.worldDungeonResponse();
    }

    public List<String> allGames() {
        // Get all current file names
        List<String> savedGames =  new ArrayList<>();

        try{
            savedGames = FileLoader.listFileNamesInResourceDirectory("/savedGames");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return savedGames;
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

    /**
     * Builds the given entity, where buildable is one of bow and shield.
     * @param buildable Item to build (bow or shield)
     * @return Current world DungeonResponse
     * @throws IllegalArgumentException If the given buildable is not a valid type
     * @throws InvalidActionException If the player doesn't have sufficient items to craft the buildable.
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        current.build(buildable);
        return current.worldDungeonResponse();

    }

}