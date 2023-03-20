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
    private MonkeyHillClimbRacing monkeyHillClimbRacing;
    private MainMenu mainMenu;

    private Area backButton;


    public MapSelectionMenu(ResizableCanvas canvas, MonkeyHillClimbRacing monkeyHillClimbRacing, MainMenu mainMenu) {

        this.monkeyHillClimbRacing = monkeyHillClimbRacing;
        this.mainMenu = mainMenu;


        this.canvas = canvas;

        try {
            background = ImageIO.read(getClass().getResource("jungleBackground.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        backButton = new Area(new Rectangle2D.Double(100, 910, 105, 50));

    }

    public void draw(Graphics2D g2d) {

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(1.6, 1.6);
        g2d.drawImage(background, affineTransform, null);

        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(backButton);


        g2d.setColor(Color.darkGray);
        g2d.fill(backButton);



        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 64));
        g2d.drawString("Select map", 800, 200);

        g2d.setFont(new Font("Arial", Font.PLAIN, 46));


        g2d.drawString("Back", 100, 950);

        g2d.setFont(new Font("Arial", Font.PLAIN, 32));

    }

    public void resetMouseEventListeners() {
        canvas.setOnMouseClicked(event -> {
            Point2D point = new Point2D.Double(event.getX(), event.getY());
            if (backButton.contains(point)) {
                canvas.setOnMouseClicked(null);
                mainMenu.setMapSelectionSelected(false);
                mainMenu.resetMouseEventListeners();
            }
        });
    }

}

