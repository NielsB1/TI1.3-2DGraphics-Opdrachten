import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Graph extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Graph");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        graphics.translate(1920 / 2, 1080 / 2);
        graphics.scale(1, -1);

        double resolution = 0.1;
        double scale = 150.0;
        double lastY = Math.pow(-100, 3);

        for (double x = -100; x < 100; x += resolution) {
            float y = (float) Math.pow(x, 3);
            graphics.draw(new Line2D.Double(x * scale, y * scale, (x - resolution) * scale, lastY * scale));

            lastY = y;
        }

        graphics.setColor(Color.red);
        graphics.drawLine(-1000, 0, 1000, 0); // x as
        graphics.drawLine(0, 1000, 0, -1000); // y as
    }


    public static void main(String[] args) {
        launch(Graph.class);
    }

}
