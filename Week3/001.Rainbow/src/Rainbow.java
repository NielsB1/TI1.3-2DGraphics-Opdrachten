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

public class Rainbow extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Rainbow");
        stage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics) {

        graphics.setBackground(Color.white);
        graphics.setTransform(new AffineTransform());
//        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(1920 / 2, 1080 / 2);

        String string = "Boogregen";
        Font font = new Font("Monospaced", Font.BOLD, 150);
        double angle = -(Math.PI / 2) + (Math.PI / string.length() / 4);

        for (int i = 0; i < string.length(); i++) {
            Shape shape = font.createGlyphVector(graphics.getFontRenderContext(), String.valueOf(string.charAt(i)).toUpperCase()).getOutline();
            AffineTransform affineTransform = new AffineTransform();

            affineTransform.translate(300 * Math.cos(angle - (Math.PI / 2)), 300 * Math.sin(angle - (Math.PI / 2)));
            affineTransform.rotate((angle));

            graphics.setColor(Color.getHSBColor((float) i / string.length(), 0.5f, 1));
            graphics.fill(affineTransform.createTransformedShape(shape));

            graphics.setColor(Color.black);
            graphics.draw(affineTransform.createTransformedShape(shape));
            angle += (Math.PI / string.length());
        }
    }


    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
