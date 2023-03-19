import java.io.*;


public class PlayerStatsLoaderAndSaver {

    public void save(PlayerStats playerStats) {
        try {
            String filePath = "/playerStats.txt"; // note the leading slash to indicate the file is in the resource folder
            File file = new File(getClass().getResource(filePath).toURI());
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(playerStats);
            objectOut.close();
            fileOut.close();
            System.out.println("PlayerStats saved successfully to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerStats load() {
        try {
            String filePath = "/playerStats.txt"; // note the leading slash to indicate the file is in the resource folder
            File file = new File(getClass().getResource(filePath).toURI());
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            PlayerStats playerStats = (PlayerStats) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("PlayerStats loaded successfully from " + filePath);
            return playerStats;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
