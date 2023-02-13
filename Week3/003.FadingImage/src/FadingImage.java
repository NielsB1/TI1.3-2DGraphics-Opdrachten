import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class FadingImage extends Application {
    private ResizableCanvas canvas;

    private BufferedImage image1;
    private BufferedImage image2;
    private float step = 0.005f;

    private float blendingNumber1 = 0.00f;

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        try {
            image1 = ImageIO.read(getClass().getResource("monko1.png"));
            image2 = ImageIO.read(getClass().getResource("monko2.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 10000000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Fading image");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());

        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        graphics.drawImage(image2, 0, 0, 1920, 1080, null);

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, blendingNumber1));
        graphics.drawImage(image1, 0, 0, 1920, 1080, null);

    }


    public void update(double deltaTime) {
        blendingNumber1 += step;

        if (blendingNumber1 >= 1.00f || blendingNumber1 <= 0.00f) {
            step = -step;
        }
    }

    public static void main(String[] args) {
        launch(FadingImage.class);
    }

}