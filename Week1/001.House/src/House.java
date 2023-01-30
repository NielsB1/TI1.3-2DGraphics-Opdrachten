import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class House extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) throws IOException {
        //frame
        graphics.drawLine(100,1000,100,500);
        graphics.drawLine(1000,1000,1000,500);
        graphics.drawLine(100,1000,1000,1000);
        graphics.drawLine(100,500,550,100);
        graphics.drawLine(550,100,1000,500);

        //door
        graphics.drawLine(200,1000,200,700);
        graphics.drawLine(200,700,350,700);
        graphics.drawLine(350,700,350,1000);

        //window
        graphics.drawLine(500,900,850,900);
        graphics.drawLine(500,900,500,650);
        graphics.drawLine(500,650,850,650);
        graphics.drawLine(850,650,850,900);
    }



    public static void main(String[] args) {
        launch(House.class);
    }

}
