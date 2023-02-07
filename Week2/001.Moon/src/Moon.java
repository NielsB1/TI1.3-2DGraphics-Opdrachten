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

public class Moon extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Moon");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {

        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        GeneralPath myShape = new GeneralPath();

        graphics.translate(1920/2 , 1080/2);
        graphics.scale(1,-1);

        myShape.moveTo(-50f, 200f);
        myShape.quadTo(250f, -20f, -100f, -150f);

        myShape.quadTo(100, -20, -50, 200);




        myShape.closePath();

        graphics.setColor(Color.yellow);
        graphics.fill(myShape);

        graphics.setColor(Color.BLACK);


        graphics.draw(myShape);

        graphics.scale(1,-1);
        graphics.drawString("Monkey", -300,-300);
    }


    public static void main(String[] args)
    {
        launch(Moon.class);
    }

}
