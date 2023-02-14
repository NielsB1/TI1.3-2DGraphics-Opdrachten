import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;

    private BufferedImage image;
    private BufferedImage[] walking = new BufferedImage[7];
    private BufferedImage[] jumping = new BufferedImage[7];
    private BufferedImage[] animation = new BufferedImage[7];
    double positionX = 0;
    private boolean first = true;
    private boolean isJumping = false;

    private int frame;

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        canvas.setOnMousePressed(e -> mousePressed(e));

        this.image = ImageIO.read(getClass().getResource("images/sprite.png"));
        for (int i = 0; i < 7; i++) {
            walking[i] = image.getSubimage(64 * i, 256, 64, 64);
            jumping[i] = image.getSubimage(64 * i, 320, 64, 64);
        }

        animation = walking;


        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 100.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
        draw(g2d);
    }

    private void mousePressed(MouseEvent e) {
        this.isJumping = true;
        this.animation = jumping;
        this.frame = 0;
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setBackground(Color.black);
        if (first)
            graphics.clearRect(0, 0, 1920, 1080);
        else graphics.clearRect((int) positionX, 300, 64, 64);
        graphics.scale(1, -1);

        AffineTransform tx = new AffineTransform();
        tx.translate(positionX, 300);


        frame = (int) (positionX / 30) % animation.length;

        graphics.drawImage(animation[frame], tx, null);

        if (this.animation == jumping && frame == 6){
            this.animation = walking;
        }


    }


    public void update(double deltaTime) {
//        if (!stop){
//        if (!isJumping)
            positionX += 2;
//        else positionX += 0;

        if (positionX >= 1920.0) {
            positionX = 0.0;
        }
//        }

    }

    public static void main(String[] args) {
        launch(MovingCharacter.class);
    }

}
