import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Spotlight extends Application {
    private ResizableCanvas canvas;
    private Shape cirle;
    private Shape currentlySelected = null;
    double x;
    double y;
    private AffineTransform affineTransform = new AffineTransform();
    private ArrayList<Line2D> line2DS = new ArrayList<>();
    private ArrayList<Color> colors = new ArrayList<>();


    @Override
    public void start(Stage stage) throws Exception {


        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(this::mouseReleased);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Spotlight");
        stage.show();
        draw(g2d);


    }

    private void mouseReleased(MouseEvent e) {
    }

    private void mousePressed(MouseEvent e) {

    }


    private void mouseDragged(MouseEvent e) {
        this.x = e.getX() + 50;
        this.y = e.getY() - 50;
        affineTransform.setToTranslation(x - (1920 / 2), y - (1080 / 2));
    }

    public void draw(FXGraphics2D graphics) {
        graphics.clearRect(0, 0, 1920, 1080);
        graphics.setBackground(Color.white);
        graphics.setColor(Color.black);
        graphics.draw(affineTransform.createTransformedShape(cirle));


        Random r = new Random();
        graphics.setClip(affineTransform.createTransformedShape(cirle));
        colors.add(Color.getHSBColor(r.nextFloat(), 1, 1));
        Line2D line2D = new Line2D.Double(0, r.nextInt(1080), 1920, r.nextInt(1080));
        line2DS.add(line2D);
        for (int i = 0; i < line2DS.size(); i++) {
            graphics.setColor(colors.get(i));
            graphics.draw(line2DS.get(i));
        }

        graphics.setClip(null);
    }

    public void init() {
        x = 800;
        y = 500;
        cirle = new Ellipse2D.Double(x, y, 200, 200);
    }

    public void update(double deltaTime) {

    }

    public static void main(String[] args) {
        launch(Spotlight.class);
    }

}
