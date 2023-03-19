import java.io.Serializable;

public class PlayerStats implements Serializable {
    private int engineUpgradeLvl;
    private int tireUpgradeLvl;
    private int aerialControlUpgradeLvl;
    private int fuelUpgradeLvl;
    private int highScore;
    private int coins;
    private int selectedLevel;

    public int getEngineUpgradeLvl() {
        return engineUpgradeLvl;
    }

    public void setEngineUpgradeLvl(int engineUpgradeLvl) {
        this.engineUpgradeLvl = engineUpgradeLvl;
    }

    public int getTireUpgradeLvl() {
        return tireUpgradeLvl;
    }

    public void setTireUpgradeLvl(int tireUpgradeLvl) {
        this.tireUpgradeLvl = tireUpgradeLvl;
    }

    public int getAerialControlUpgradeLvl() {
        return aerialControlUpgradeLvl;
    }

    public void setAerialControlUpgradeLvl(int aerialControlUpgradeLvl) {
        this.aerialControlUpgradeLvl = aerialControlUpgradeLvl;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }

    public void setSelectedLevel(int selectedLevel) {
        this.selectedLevel = selectedLevel;
    }

    public int getFuelUpgradeLvl() {
        return fuelUpgradeLvl;
    }

    public void setFuelUpgradeLvl(int fuelUpgradeLvl) {
        this.fuelUpgradeLvl = fuelUpgradeLvl;
    }
}
