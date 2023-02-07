import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class BlockDrag extends Application {
    ResizableCanvas canvas;
    private ArrayList<Renderable> renderables = new ArrayList<>();
    private Renderable currentlySelected;

    private boolean first = true;

    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics) {


        graphics.setTransform(new AffineTransform());
        this.x = 0;
        this.y = 0;


        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        if (first) {
            graphics.translate(1920 / 2, 1080 / 2);
            graphics.scale(1, -1);
//            first = false;
        }

        for (Renderable renderable : renderables) {
            Rectangle2D blok = new Rectangle2D.Double(renderable.getPosition().getX() - 50, renderable.getPosition().getY() - 50, 100, 100);
            graphics.setColor(renderable.getColor());
            graphics.fill(blok);

            graphics.setColor(Color.black);
            graphics.draw(blok);
        }

    }


    public static void main(String[] args) {
        launch(BlockDrag.class);
    }

    private void mousePressed(MouseEvent e) {

        double x = e.getX() - (1920 / 2f);
        double y = e.getY() * -1 + (1080 / 2f);


        if (e.getButton().equals(MouseButton.PRIMARY)) {
            for (Renderable renderable : renderables) {
                if (renderable.getPosition().getX() - x < 50 && renderable.getPosition().getX() - x > -50 && renderable.getPosition().getY() - y < 50 && renderable.getPosition().getY() - y > -50) {
                    this.currentlySelected = renderable;
                    break;
                }
            }
        }
    }

    private void mouseReleased(MouseEvent e) {
        this.currentlySelected = null;
    }

    private void mouseDragged(MouseEvent e) {
        if (currentlySelected != null) {
            renderables.remove(currentlySelected);


            System.out.println("sleep die blok");
            Point2D point2D = new Point2D.Double(e.getX() - (1920 / 2f), e.getY() * -1 + (1080 / 2f));
            currentlySelected.setPosition(point2D);

            Renderable blok = new Renderable(currentlySelected.getShape(), point2D, currentlySelected.getColor());
            this.currentlySelected = blok;
            renderables.add(blok);

            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        } else if (e.getButton().equals(MouseButton.SECONDARY)){

            this.x = this.x + (e.getX() - (1920 / 2f));
            this.y = this.y - (e.getY() * -1 + (1080 / 2f));

            canvas.setTranslateX(this.x);
            canvas.setTranslateY(this.y);


        }
    }

    public void init() {
        int width = 100;
        int height = 100;
        for (int i = 0; i < 10; i++) {
            int x = (int) ((Math.random() * 1600) - 800);
            int y = (int) ((Math.random() * 900) - 450);

            Rectangle2D blok = new Rectangle2D.Double(x, y, width, height);
            Point2D point2D = new Point2D.Double(x + (width / 2f), y + (height / 2f));
            Color color = (Color.getHSBColor((float) (Math.random() * 256), 0.7f, 1));


            Renderable blokje = new Renderable(blok, point2D, color);
            renderables.add(blokje);
        }
    }
}
