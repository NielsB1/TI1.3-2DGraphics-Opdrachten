import java.io.*;


public class PlayerStatsLoaderAndSaver {

    public void save(PlayerStats playerStats) {
        try {
            String filePath = "Eindopdracht/resources/playerStats.txt";
            File file = new File(String.valueOf(this.getClass().getResource("playerStats.txt")));
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
        InputStream resourceAsStream = this.getClass().getResourceAsStream("playerStats.txt");
//        String filePath = "Eindopdracht/resources/playerStats.txt";
//            File file = new File(filePath);
//            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(resourceAsStream);
            PlayerStats playerStats = (PlayerStats) objectIn.readObject();
            objectIn.close();
//            fileIn.close();
            System.out.println("PlayerStats loaded successfully");
        System.out.println(playerStats.getHighScore());
            return playerStats;
    }
}
