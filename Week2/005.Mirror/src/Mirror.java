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

public class Mirror extends Application {
    ResizableCanvas canvas;
    private double rc = 2.5;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Mirror");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.translate(1920 / 2, 1080 / 2);
        graphics.scale(1, -1);


        graphics.setColor(Color.red);
        graphics.drawLine(-1000, 0, 1000, 0);
        graphics.setColor(Color.blue);
        graphics.drawLine(0, -1000, 0, 1000);


        graphics.setColor(Color.black);


        graphics.drawLine(-3000, (int)(-3000 * rc), 3000, (int) (3000 * rc));

        Rectangle2D rectangle2D = new Rectangle2D.Double(-50,100,100,100);
        graphics.draw(rectangle2D);

        AffineTransform tx = new AffineTransform();
        tx.concatenate(new AffineTransform(((2 / (1 + Math.pow(rc, 2))) - 1),(2 * rc / (1 + Math.pow(rc, 2))),(2 * rc)/ (1 + Math.pow(rc, 2)),(((2 * Math.pow(rc, 2)) / (1 + Math.pow(rc, 2))) - 1),0,0));

        graphics.draw(tx.createTransformedShape(rectangle2D));


    }


    public static void main(String[] args) {
        launch(Mirror.class);
    }

}
