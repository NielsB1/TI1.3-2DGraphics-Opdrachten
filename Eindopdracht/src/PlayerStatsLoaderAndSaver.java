import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class PlayerStatsLoaderAndSaver {

    public void save(PlayerStats playerStats) {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(getClass().getResource("playerStats.txt").toURI())));
            objectOut.writeObject(playerStats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerStats load() throws Exception {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("playerStats.txt");
        ObjectInputStream objectIn = new ObjectInputStream(resourceAsStream);
        PlayerStats playerStats = (PlayerStats) objectIn.readObject();
        objectIn.close();
        System.out.println("PlayerStats loaded successfully");
        return playerStats;
    }
}
