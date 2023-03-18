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
    private Area startButton;
    private Area mapSelectionButton;
    private Area shopButton;
    private Area quitButton;
    private ResizableCanvas canvas;
    private BufferedImage background;
    private MonkeyHillClimbRacing monkeyHillClimbRacing;


    public MainMenu(ResizableCanvas canvas, MonkeyHillClimbRacing monkeyHillClimbRacing) {
        this.monkeyHillClimbRacing = monkeyHillClimbRacing;

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

        canvas.setOnMouseClicked(event -> {
            Point2D point = new Point2D.Double(event.getX(), event.getY());
            if (startButton.contains(point)){
                monkeyHillClimbRacing.setGameStarted(true);
            }
        });
    }

    public void draw(Graphics2D g2d) {

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

        g2d.setFont(new Font("Arial", Font.PLAIN, 46));
        g2d.drawString("SELECT MAP", 100, 700);

        g2d.setFont(new Font("Arial", Font.PLAIN, 46));
        g2d.drawString("SHOP", 100, 800);

        g2d.setFont(new Font("Arial", Font.PLAIN, 46));
        g2d.drawString("QUIT", 100, 900);

    }


}
