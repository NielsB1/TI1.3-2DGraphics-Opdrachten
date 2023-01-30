import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Rainbow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {

        graphics.translate(1920 / 2, 1080 / 2);
        graphics.scale(1, -1);

        double resolution = 0.001;
        float radiusBinnen = 300;
        float radiusBuiten = 500;

        for (float i = 0; i < Math.PI; i += resolution) {
            float x1 = (float) (radiusBinnen * Math.cos(i));
            float y1 = (float) (radiusBinnen * Math.sin(i));
            float x2 = (float) (radiusBuiten * Math.cos(i));
            float y2 = (float) (radiusBuiten * Math.sin(i));

            graphics.setColor(Color.getHSBColor((float) (i / Math.PI), 0.7f, 1));
            graphics.draw(new Line2D.Float(x1, y1, x2, y2));

        }


        graphics.setColor(Color.red);
        graphics.drawLine(-1000, 0, 1000, 0); // x as
        graphics.drawLine(0, 1000, 0, -1000); // y as
    }


    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
