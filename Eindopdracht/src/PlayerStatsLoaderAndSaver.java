import java.io.*;


public class PlayerStatsLoaderAndSaver {

    public void save(PlayerStats playerStats) {
        try {
            String filePath = "Eindopdracht/resources/playerStats.txt";
            File file = new File(filePath);
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

    public PlayerStats load() throws Exception {
            String filePath = "Eindopdracht/resources/playerStats.txt";
            File file = new File(filePath);
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            PlayerStats playerStats = (PlayerStats) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("PlayerStats loaded successfully from " + filePath);
            return playerStats;
    }
}
