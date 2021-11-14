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
import java.util.Random;
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

        String[] skins = {"default","lava", "spongebob"};

        Random ran = new Random();

        int i = ran.nextInt(skins.length);
        return skins[i];
        

        //return "default";
    }

    public List<String> getSkins() {
        return Arrays.asList("lava", "default");
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("standard", "peaceful", "hard");
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
        
        gameMode = gameMode.toLowerCase();
        if (!dungeons().contains(dungeonName)) throw new IllegalArgumentException(dungeonName  + "is not a dungeon. Please select a valid dungeon.");
        if (!(getGameModes().contains(gameMode))) throw new IllegalArgumentException(gameMode + " is not a valid game mode.");

        Random ran = new Random();

        World newGame = new World(dungeonName, gameMode, ran.nextInt());
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

        JSONObject saveState = current.saveGame();

        String dungeonID = name;
        current.setId(dungeonID);


        saveState.put("id", dungeonID);

        File saveDirectory = new File("src/main/savedGames");
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
            String file = FileLoader.loadFileOutsideOfResources("src/main/savedGames/" + name + ".json");
            JSONObject game = new JSONObject(file);

            Random ran = new Random();

            newGame = new World(game.getString("dungeon-name"), game.getString("gamemode"), game.getString("id"), ran.nextInt());

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
            savedGames = FileLoader.listFileNamesInDirectoryOutsideOfResources("src/main/savedGames");
            //savedGames = FileLoader.listFileNamesInResourceDirectory("/savedGames");
        }
        catch(Exception e) {
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

    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String gameMode) throws IllegalArgumentException {
        
        gameMode = gameMode.toLowerCase();

        Random ran = new Random();

        World newGame = new World("primsRandom", gameMode, ran.nextInt());

        newGame.generateDungeon(xStart, yStart, xEnd, yEnd);

        this.current = newGame;
        
        return current.worldDungeonResponse();
    }


}