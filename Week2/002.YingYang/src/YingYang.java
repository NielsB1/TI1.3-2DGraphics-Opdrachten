import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class YingYang extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Ying Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics) {


        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.translate(1920 / 2, 1080 / 2);
        graphics.scale(5, -5);


        Area circle = new Area(new Ellipse2D.Double(-50, -50, 100, 100)); //main circle
        Area circle2 = new Area(new Ellipse2D.Double(-25, -50, 50, 50)); //bottom black circle
        Area circle3 = new Area(new Ellipse2D.Double(-25, 0, 51, 51)); //top white circle
        Area circle4 = new Area(new Rectangle.Double(-100,-50,100,100)); //weird rectangle to subtract circle 1
        Area circle5 = new Area(new Ellipse2D.Double(-50, -50, 100, 100)); //copy of circle 1 top draw a outline


        Area circle6 = new Area(new Ellipse2D.Double(-5, 20, 10, 10)); //small black circle
        Area circle7 = new Area(new Ellipse2D.Double(-5, -30, 10, 10)); //small white circle




        circle.subtract(circle4);

        graphics.draw(circle2);
        graphics.fill(circle2);
        graphics.fill(circle);

        graphics.setColor(Color.white);
        graphics.fill(circle3);
        graphics.fill(circle7);

        graphics.setColor(Color.black);
        graphics.draw(circle5);

        graphics.fill(circle6);

    }


    public static void main(String[] args) {
        launch(YingYang.class);
    }

}
