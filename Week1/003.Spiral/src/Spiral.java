import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spiral extends Application {
    @Override
    public void start(Stage primaryStage){
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Spiral");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        graphics.translate(1920 / 2, 1080 / 2);
        graphics.scale(1, -1);

        double resolution = 0.01;
        double scale = 5.0;
        double lastY = Math.sin(0);
        double lastX = Math.cos(0);
        float straal = 1;

        for (int j = 0; j < 10; j++) {
            for (double i = 0; i < 2 * Math.PI; i += resolution) {
                float x = (float) (straal * Math.cos(i));
                float y = (float) (straal * Math.sin(i));
                graphics.draw(new Line2D.Double(x * scale, y * scale, lastX * scale, lastY * scale));
                lastY = y;
                lastX = x;
                straal += 0.05;
            }
        }

        graphics.setColor(Color.red);
        graphics.drawLine(-1000, 0, 1000, 0); // x as
        graphics.drawLine(0, 1000, 0, -1000); // y as
    }


    public static void main(String[] args) {
        launch(Spiral.class);
    }

}
