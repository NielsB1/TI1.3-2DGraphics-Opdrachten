import java.awt.*;
import java.awt.geom.*;
import java.nio.DoubleBuffer;
import java.util.Collections;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spirograph extends Application {
    private TextField v1;
    private TextField v2;
    private TextField v3;
    private TextField v4;
    private Button draw;
    private Button clear;

    private boolean firstDraw = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);

        VBox mainBox = new VBox();
        HBox topBar = new HBox();
        mainBox.getChildren().add(topBar);
        mainBox.getChildren().add(new Group(canvas));

        topBar.getChildren().add(v1 = new TextField("300"));
        topBar.getChildren().add(v2 = new TextField("1"));
        topBar.getChildren().add(v3 = new TextField("300"));
        topBar.getChildren().add(v4 = new TextField("10"));
        topBar.getChildren().add(draw = new Button("draw"));
        topBar.getChildren().add(clear = new Button("clear"));


        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainBox));
        primaryStage.setTitle("Spirograph");
        primaryStage.show();

        draw.setOnAction(event -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));

        clear.setOnAction(event -> clear(new FXGraphics2D(canvas.getGraphicsContext2D())));
    }


    public void draw(FXGraphics2D graphics) {
        //you can use Double.parseDouble(v1.getText()) to get a double value from the first textfield
        //feel free to add more textfields or other controls if needed, but beware that swing components might clash in naming

        graphics.setColor(Color.getHSBColor((float) (Math.random() * 256), 1, 1));
        if (firstDraw) {
            graphics.translate(1920 / 2, 1080 / 2);
            graphics.scale(1, -1);
            firstDraw = false;
        }

        if (!v1.getText().isEmpty() && !v2.getText().isEmpty() && !v3.getText().isEmpty() && !v4.getText().isEmpty()) {
            double a = Double.parseDouble(v1.getText());
            double b = Double.parseDouble(v2.getText());
            double c = Double.parseDouble(v3.getText());
            double d = Double.parseDouble(v4.getText());


            double resolution = 0.001;
            double scale = 1.0;
            double lastY = (a * Math.sin(b * 0) + c * Math.sin(d * 0));
            double lastX = (a * Math.cos(b * 0) + c * Math.cos(d * 0));

            for (double i = 0; i < 2 * Math.PI; i += resolution) {
                float x = (float) (a * Math.cos(b * i) + c * Math.cos(d * i));
                float y = (float) (a * Math.sin(b * i) + c * Math.sin(d * i));
                graphics.draw(new Line2D.Double(x * scale, y * scale, lastX * scale, lastY * scale));
                lastY = y;
                lastX = x;
            }

            graphics.setColor(Color.red);
            graphics.drawLine(-1000, 0, 1000, 0); // x as
            graphics.drawLine(0, 1000, 0, -1000); // y as
        }
    }

    public void clear(FXGraphics2D graphics) {
        graphics.setColor(Color.white);
        graphics.fillRect(-1000, -1000, 2000, 2000);

        graphics.setColor(Color.red);
        graphics.drawLine(-1000, 0, 1000, 0); // x as
        graphics.drawLine(0, 1000, 0, -1000); // y as
    }


    public static void main(String[] args) {
        launch(Spirograph.class);
    }
}
