import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Screensaver extends Application {
    private ResizableCanvas canvas;

    private Point point1;
    private Point point2;
    private Point point3;
    private Point point4;
    private ArrayList<Point> points = new ArrayList<>();

    private ArrayList<Line2D> previousLines = new ArrayList<>();

    private boolean firstDraw = true;

    private double velocity;

    private float color = 0;

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
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
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.black);
        graphics.clearRect(-1000, -1000, 4000, 4000);

        if (firstDraw) {
//            graphics.setColor(Color.magenta);
            graphics.translate(1920 / 2, 1080 / 2);
            graphics.scale(1, -1);
        }


        previousLines.add(new Line2D.Double(point1.getPoint().getX(), point1.getPoint().getY(), point2.getPoint().getX(), point2.getPoint().getY()));
        previousLines.add(new Line2D.Double(point2.getPoint().getX(), point2.getPoint().getY(), point3.getPoint().getX(), point3.getPoint().getY()));
        previousLines.add(new Line2D.Double(point3.getPoint().getX(), point3.getPoint().getY(), point4.getPoint().getX(), point4.getPoint().getY()));
        previousLines.add(new Line2D.Double(point4.getPoint().getX(), point4.getPoint().getY(), point1.getPoint().getX(), point1.getPoint().getY()));

        if (previousLines.size() > 300) {
            for (int i = 0; i < 4; i++) {
                previousLines.remove(i);
            }
        }

        graphics.setColor(Color.getHSBColor(this.color, 0.8f, 1));
        if (this.color >= 1)
            this.color = 0;
        else this.color += 1 /256f;

        for (Line2D previousLine : previousLines) {
//            graphics.setColor(Color.getHSBColor((float) Math.random(), 0.8f, 1));
            graphics.draw(previousLine);
        }


    }

    public void init() {
        point1 = new Point(new Point2D.Double(-500, 100), -1, 1);
        point2 = new Point(new Point2D.Double(-100, 100), -1, -1);
        point3 = new Point(new Point2D.Double(-100, -300), 1, 1);
        point4 = new Point(new Point2D.Double(-500, -300), 1, -1);

        velocity = 5;

        Collections.addAll(points, point1, point2, point3, point4);
    }

    public void update(double deltaTime) {
        for (Point point : points) {
            double oldX = point.getPoint().getX();
            double oldY = point.getPoint().getY();

            if (oldX <= -960 || oldX >= 960) {
                point.setDirection(-point.getDirection());
            } else if (oldY <= -540 || oldY >= 540) {
                point.setRc(-(1 / point.getRc()));
            }

            if (point.getRc() > 0 && oldY > 540) {
                point.setRc(-(1 / point.getRc()));
            } else if (point.getRc() < 0 && oldY < -540) {
                point.setRc(-(1 / point.getRc()));
            }

            double x = oldX + (point.getDirection() * velocity);
            double y = oldY + (velocity * point.getRc());
            point.setPoint(new Point2D.Double(x, y));
        }
    }

    public static void main(String[] args) {
        launch(Screensaver.class);
    }

}
