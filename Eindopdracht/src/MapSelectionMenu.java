import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapSelectionMenu {

    private ResizableCanvas canvas;
    private BufferedImage background;
    private BufferedImage grassMap;
    private BufferedImage highwayMap;
    private BufferedImage moonMap;
    private BufferedImage mountainMap;
    private MonkeyHillClimbRacing monkeyHillClimbRacing;
    private MainMenu mainMenu;

    private Area backButton;
    private Area map1;
    private Area map2;
    private Area map3;
    private Area map4;


    public MapSelectionMenu(ResizableCanvas canvas, MonkeyHillClimbRacing monkeyHillClimbRacing, MainMenu mainMenu) {

        this.monkeyHillClimbRacing = monkeyHillClimbRacing;
        this.mainMenu = mainMenu;


        this.canvas = canvas;

        try {
            background = ImageIO.read(getClass().getResource("jungleBackground.png"));
            grassMap = ImageIO.read(getClass().getResource("grassMap.png"));
            highwayMap = ImageIO.read(getClass().getResource("highwayMap.png"));
            moonMap = ImageIO.read(getClass().getResource("moonMap.png"));
            mountainMap = ImageIO.read(getClass().getResource("mountainMap.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        backButton = new Area(new Rectangle2D.Double(100, 910, 105, 50));

        map1 = new Area(new Rectangle2D.Double(150, 400, 375, 300));
        map2 = new Area(new Rectangle2D.Double(575, 400, 375, 300));
        map3 = new Area(new Rectangle2D.Double(1000, 400, 375, 300));
        map4 = new Area(new Rectangle2D.Double(1425, 400, 375, 300));

    }

    public void draw(Graphics2D g2d) {

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(1.6, 1.6);
        g2d.drawImage(background, affineTransform, null);

        affineTransform.scale(1, 1);
        affineTransform.setToTranslation(150,400);
        g2d.drawImage(grassMap, affineTransform, null);

        affineTransform.setToTranslation(575,400);
        g2d.drawImage(highwayMap, affineTransform, null);

        affineTransform.setToTranslation(1000,400);
        g2d.drawImage(moonMap, affineTransform, null);

        affineTransform.setToTranslation(1425,400);
        g2d.drawImage(mountainMap, affineTransform, null);

        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(backButton);
        g2d.draw(map1);
        g2d.draw(map2);
        g2d.draw(map3);
        g2d.draw(map4);


        g2d.setColor(Color.darkGray);
        g2d.fill(backButton);
//        g2d.fill(map1);
//        g2d.fill(map2);
//        g2d.fill(map3);
//        g2d.fill(map4);


        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 64));
        g2d.drawString("Select map", 800, 200);

        g2d.setFont(new Font("Arial", Font.PLAIN, 46));
        g2d.drawString("Grasslands", 230, 380);
        g2d.drawString("Highway", 690, 380);
        g2d.drawString("Moon", 1120, 380);
        g2d.drawString("Mountains", 1500, 380);


        g2d.drawString("Back", 100, 950);

        g2d.setFont(new Font("Arial", Font.PLAIN, 32));
        g2d.drawString("High score: " + monkeyHillClimbRacing.getPlayerStats().getHighScore() + " m", 1550, 100);
        g2d.drawString("Coins: " + monkeyHillClimbRacing.getPlayerStats().getCoins(), 1550, 150);

    }

    public void resetMouseEventListeners() {
        canvas.setOnMouseClicked(event -> {
            Point2D point = new Point2D.Double(event.getX(), event.getY());
            if (backButton.contains(point)) {
                canvas.setOnMouseClicked(null);
                mainMenu.setMapSelectionSelected(false);
                mainMenu.resetMouseEventListeners();
            } else if (map1.contains(point)) {
                PlayerStats playerStats = monkeyHillClimbRacing.getPlayerStats();
                playerStats.setSelectedLevel(1);
                monkeyHillClimbRacing.setPlayerStats(playerStats);
                monkeyHillClimbRacing.changeMap(playerStats.getSelectedLevel());

                canvas.setOnMouseClicked(null);
                mainMenu.setMapSelectionSelected(false);
                mainMenu.resetMouseEventListeners();
            } else if (map2.contains(point)) {
                PlayerStats playerStats = monkeyHillClimbRacing.getPlayerStats();
                playerStats.setSelectedLevel(2);
                monkeyHillClimbRacing.setPlayerStats(playerStats);
                monkeyHillClimbRacing.changeMap(playerStats.getSelectedLevel());

                canvas.setOnMouseClicked(null);
                mainMenu.setMapSelectionSelected(false);
                mainMenu.resetMouseEventListeners();
            } else if (map3.contains(point)) {
                PlayerStats playerStats = monkeyHillClimbRacing.getPlayerStats();
                playerStats.setSelectedLevel(3);
                monkeyHillClimbRacing.setPlayerStats(playerStats);
                monkeyHillClimbRacing.changeMap(playerStats.getSelectedLevel());

                canvas.setOnMouseClicked(null);
                mainMenu.setMapSelectionSelected(false);
                mainMenu.resetMouseEventListeners();
            } else if (map4.contains(point)) {
                PlayerStats playerStats = monkeyHillClimbRacing.getPlayerStats();
                playerStats.setSelectedLevel(4);
                monkeyHillClimbRacing.setPlayerStats(playerStats);
                monkeyHillClimbRacing.changeMap(playerStats.getSelectedLevel());

                canvas.setOnMouseClicked(null);
                mainMenu.setMapSelectionSelected(false);
                mainMenu.resetMouseEventListeners();
            }
        });
    }

}

