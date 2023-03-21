import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainMenu {
    private ShopMenu shopMenu;
    private MapSelectionMenu mapSelectionMenu;
    private Area startButton;
    private Area mapSelectionButton;
    private Area shopButton;
    private Area quitButton;
    private ResizableCanvas canvas;
    private BufferedImage background;
    private MonkeyHillClimbRacing monkeyHillClimbRacing;
    private boolean shopSelected;
    private boolean mapSelectionSelected;


    public MainMenu(ResizableCanvas canvas, MonkeyHillClimbRacing monkeyHillClimbRacing) {
        this.monkeyHillClimbRacing = monkeyHillClimbRacing;
        this.shopMenu = new ShopMenu(canvas, monkeyHillClimbRacing, this);
        this.mapSelectionMenu = new MapSelectionMenu(canvas, monkeyHillClimbRacing, this);

        startButton = new Area(new Rectangle2D.Double(100, 560, 300, 50));
        mapSelectionButton = new Area(new Rectangle2D.Double(100, 660, 295, 50));
        shopButton = new Area(new Rectangle2D.Double(100, 760, 135, 50));
        quitButton = new Area(new Rectangle2D.Double(100, 860, 115, 50));

        this.canvas = canvas;

        try {
            background = ImageIO.read(getClass().getResource("jungleBackground.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resetMouseEventListeners();
    }

    public void draw(Graphics2D g2d) {
        if (!shopSelected && !mapSelectionSelected) {

            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(1.6, 1.6);
            g2d.drawImage(background, affineTransform, null);

            g2d.setColor(Color.white);
            g2d.setStroke(new BasicStroke(5));
            g2d.draw(startButton.getBounds2D());
            g2d.draw(mapSelectionButton.getBounds2D());
            g2d.draw(shopButton.getBounds2D());
            g2d.draw(quitButton.getBounds2D());

            g2d.setColor(Color.darkGray);
            g2d.fill(startButton.getBounds2D());
            g2d.fill(mapSelectionButton.getBounds2D());
            g2d.fill(shopButton.getBounds2D());
            g2d.fill(quitButton.getBounds2D());

            g2d.setColor(Color.white);
            g2d.setFont(new Font("Arial", Font.BOLD, 64));
            g2d.drawString("Monkey Hill Climb Racing", 550, 200);

            g2d.setFont(new Font("Arial", Font.PLAIN, 46));
            g2d.drawString("START GAME", 100, 600);

            g2d.drawString("SELECT MAP", 100, 700);

            g2d.drawString("SHOP", 100, 800);

            g2d.drawString("QUIT", 100, 900);

            g2d.setFont(new Font("Arial", Font.PLAIN, 32));
            g2d.drawString("High score: " + monkeyHillClimbRacing.getPlayerStats().getHighScore() + " m", 1550, 100);
            g2d.drawString("Coins: " + monkeyHillClimbRacing.getPlayerStats().getCoins(), 1550, 150);

        } else if (shopSelected) {
            shopMenu.draw(g2d);
        } else if (mapSelectionSelected) {
            mapSelectionMenu.draw(g2d);
        }
    }

    public void resetMouseEventListeners() {
        canvas.setOnMouseClicked(event -> {
            Point2D point = new Point2D.Double(event.getX(), event.getY());
            if (startButton.contains(point)) {
                canvas.setOnMouseClicked(null);
                monkeyHillClimbRacing.setGameStarted(true);
            } else if (quitButton.contains(point)) {
                PlayerStatsLoaderAndSaver playerStatsLoaderAndSaver = monkeyHillClimbRacing.getPlayerStatsLoaderAndSaver();
                playerStatsLoaderAndSaver.save(monkeyHillClimbRacing.getPlayerStats());
                System.exit(1);
            } else if (shopButton.contains(point)) {
                canvas.setOnMouseClicked(null);
                shopMenu.resetMouseEventListeners();
                shopSelected = true;
            } else if (mapSelectionButton.contains(point)) {
                canvas.setOnMouseClicked(null);
                mapSelectionMenu.resetMouseEventListeners();
                mapSelectionSelected = true;
            }
        });
    }

    public void setShopSelected(boolean shopSelected) {
        this.shopSelected = shopSelected;
    }

    public void setMapSelectionSelected(boolean mapSelectionSelected) {
        this.mapSelectionSelected = mapSelectionSelected;
    }
}
