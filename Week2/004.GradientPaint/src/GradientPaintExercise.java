import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;
    private Point2D center = new Point2D.Double(0, 0);
    private boolean first = true;


    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);

        canvas.setOnMouseDragged(event -> {
            center = new Point2D.Double(event.getX(), (event.getY()));
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });


        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));


    }


    public void draw(FXGraphics2D graphics) {


        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        if (first) {
            graphics.translate(1920 / 2, 1080 / 2);
            graphics.scale(1, -1);
            first = false;
        }


        Rectangle2D.Double bruh = new Rectangle2D.Double(-10000, -10000, 20000, 20000);


        float[] fractions = new float[]{
                0.31f,
                0.34f,
                0.35f
        };

        Color[] colors = new Color[]{
                Color.blue,
                Color.green,
                Color.white
        };

        RadialGradientPaint radialGradientPaint = new RadialGradientPaint(center, 100f, center, fractions, colors, MultipleGradientPaint.CycleMethod.REPEAT);

        graphics.setPaint(radialGradientPaint);
        graphics.fill(bruh);

        graphics.draw(bruh);


    }


    public static void main(String[] args) {
        launch(GradientPaintExercise.class);
    }

}
