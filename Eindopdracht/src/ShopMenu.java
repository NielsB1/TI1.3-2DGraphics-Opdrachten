import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ShopMenu {

    private ResizableCanvas canvas;
    private BufferedImage background;
    private BufferedImage car;
    private MonkeyHillClimbRacing monkeyHillClimbRacing;
    private MainMenu mainMenu;

    private Area backButton;
    private Area upgradeEngineButton;
    private Area upgradeTiresButton;
    private Area upgradeAerialControlButton;
    private Area upgradeFuelButton;

    private int engineCost;
    private int tiresCost;
    private int aerialControlCost;
    private int fuelCost;


    public ShopMenu(ResizableCanvas canvas, MonkeyHillClimbRacing monkeyHillClimbRacing, MainMenu mainMenu) {
        this.monkeyHillClimbRacing = monkeyHillClimbRacing;
        this.mainMenu = mainMenu;


        this.canvas = canvas;

        try {
            background = ImageIO.read(getClass().getResource("jungleBackground.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            car = ImageIO.read(getClass().getResource("monkeyCar.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        backButton = new Area(new Rectangle2D.Double(100, 910, 105, 50));

        upgradeEngineButton = new Area(new Rectangle2D.Double(470, 700, 220, 120));
        upgradeTiresButton = new Area(new Rectangle2D.Double(710, 700, 180, 120));
        upgradeAerialControlButton = new Area(new Rectangle2D.Double(910, 700, 300, 120));
        upgradeFuelButton = new Area(new Rectangle2D.Double(1230, 700, 210, 120));
    }

    public void draw(Graphics2D g2d) {
        calculateCosts();

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(1.6, 1.6);
        g2d.drawImage(background, affineTransform, null);


        affineTransform.setToTranslation(650, 300);
        affineTransform.scale(2.5, 2.5);
        g2d.drawImage(car, affineTransform, null);

        Rectangle2D rectangle = new Rectangle2D.Double(75, 50, 450, 375);

        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(rectangle);
        g2d.draw(backButton);
        g2d.draw(upgradeEngineButton);
        g2d.draw(upgradeTiresButton);
        g2d.draw(upgradeAerialControlButton);
        g2d.draw(upgradeFuelButton);


        g2d.setColor(Color.darkGray);
        g2d.fill(rectangle);
        g2d.fill(backButton);
        g2d.fill(upgradeEngineButton);
        g2d.fill(upgradeTiresButton);
        g2d.fill(upgradeAerialControlButton);
        g2d.fill(upgradeFuelButton);


        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 64));
        g2d.drawString("Shop", 900, 200);

        g2d.setFont(new Font("Arial", Font.PLAIN, 46));
        g2d.drawString("Engine lvl: " + monkeyHillClimbRacing.getPlayerStats().getEngineUpgradeLvl(), 100, 100);
        g2d.drawString("Tires lvl: " + monkeyHillClimbRacing.getPlayerStats().getTireUpgradeLvl(), 100, 200);
        g2d.drawString("Aerial control lvl: " + monkeyHillClimbRacing.getPlayerStats().getAerialControlUpgradeLvl(), 100, 300);
        g2d.drawString("Fuel lvl: " + monkeyHillClimbRacing.getPlayerStats().getFuelUpgradeLvl(), 100, 400);


        g2d.drawString("Back", 100, 950);

        g2d.setFont(new Font("Arial", Font.PLAIN, 32));
        g2d.drawString("High score: " + monkeyHillClimbRacing.getPlayerStats().getHighScore() + " m", 1550, 100);
        g2d.drawString("Coins: " + monkeyHillClimbRacing.getPlayerStats().getCoins(), 1550, 150);


        g2d.drawString("Engine lvl " + (monkeyHillClimbRacing.getPlayerStats().getEngineUpgradeLvl() + 1), 480, 750);
        g2d.drawString(engineCost + " coins", 480, 800);

        g2d.drawString("Tires lvl " + (monkeyHillClimbRacing.getPlayerStats().getTireUpgradeLvl() + 1), 720, 750);
        g2d.drawString(tiresCost + " coins", 720, 800);

        g2d.drawString("Aerial control lvl " + (monkeyHillClimbRacing.getPlayerStats().getAerialControlUpgradeLvl() + 1), 920, 750);
        g2d.drawString(aerialControlCost + " coins", 920, 800);

        g2d.drawString("Fuel lvl " + (monkeyHillClimbRacing.getPlayerStats().getFuelUpgradeLvl() + 1), 1240, 750);
        g2d.drawString(fuelCost + " coins", 1240, 800);

    }

    public void resetMouseEventListeners() {
        canvas.setOnMouseClicked(event -> {
            Point2D point = new Point2D.Double(event.getX(), event.getY());
            if (backButton.contains(point)) {
                canvas.setOnMouseClicked(null);
                mainMenu.setShopSelected(false);
                mainMenu.resetMouseEventListeners();
            } else if (upgradeEngineButton.contains(point)) {
                PlayerStats playerStats = monkeyHillClimbRacing.getPlayerStats();

                if (playerStats.getCoins() >= engineCost) {
                    playerStats.setEngineUpgradeLvl(playerStats.getEngineUpgradeLvl() + 1);
                    playerStats.setCoins(playerStats.getCoins() - engineCost);
                    monkeyHillClimbRacing.setPlayerStats(playerStats);
                }
            } else if (upgradeTiresButton.contains(point)) {
                PlayerStats playerStats = monkeyHillClimbRacing.getPlayerStats();

                if (playerStats.getCoins() >= tiresCost) {
                    playerStats.setTireUpgradeLvl(playerStats.getTireUpgradeLvl() + 1);
                    playerStats.setCoins(playerStats.getCoins() - tiresCost);
                    monkeyHillClimbRacing.setPlayerStats(playerStats);
                }
            } else if (upgradeAerialControlButton.contains(point)) {
                PlayerStats playerStats = monkeyHillClimbRacing.getPlayerStats();

                if (playerStats.getCoins() >= aerialControlCost) {
                    playerStats.setAerialControlUpgradeLvl(playerStats.getAerialControlUpgradeLvl() + 1);
                    playerStats.setCoins(playerStats.getCoins() - aerialControlCost);
                    monkeyHillClimbRacing.setPlayerStats(playerStats);
                }
            } else if (upgradeFuelButton.contains(point)) {
                PlayerStats playerStats = monkeyHillClimbRacing.getPlayerStats();

                if (playerStats.getCoins() >= fuelCost) {
                    playerStats.setFuelUpgradeLvl(playerStats.getFuelUpgradeLvl() + 1);
                    playerStats.setCoins(playerStats.getCoins() - fuelCost);
                    monkeyHillClimbRacing.setPlayerStats(playerStats);
                }
            }
        });
    }

    public void calculateCosts() {
        this.engineCost = 25 + (25 * monkeyHillClimbRacing.getPlayerStats().getEngineUpgradeLvl());
        this.tiresCost = 25 + (25 * monkeyHillClimbRacing.getPlayerStats().getTireUpgradeLvl());
        this.aerialControlCost = 25 + (25 * monkeyHillClimbRacing.getPlayerStats().getAerialControlUpgradeLvl());
        this.fuelCost = 25 + (25 * monkeyHillClimbRacing.getPlayerStats().getFuelUpgradeLvl());
    }
}

