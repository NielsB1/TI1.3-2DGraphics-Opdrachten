
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Eindopdracht extends Application {

    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Body floor;
    private NoiseMapGenerator noiseMapGenerator;
    private ArrayList<Line2D> lines = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        NoiseMapGenerator noiseMapGenerator = new NoiseMapGenerator();
        BorderPane mainPane = new BorderPane();

        // Add debug button
        javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");
        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });
        mainPane.setTop(showDebug);

        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        camera = new Camera(canvas, g -> draw(g), g2d);
        mousePicker = new MousePicker(canvas);

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Joe");
        stage.show();
        draw(g2d);
    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        noiseMapGenerator = new NoiseMapGenerator();
//        lines.add(new Line2D.Double(100, noiseMapGenerator.noise(0.1) * 5000, 200, noiseMapGenerator.noise(0.2) * 5000));
//        lines.add(new Line2D.Double(200, noiseMapGenerator.noise(0.2) * 5000, 300, noiseMapGenerator.noise(0.3) * 5000));
//        lines.add(new Line2D.Double(300, noiseMapGenerator.noise(0.3) * 5000, 400, noiseMapGenerator.noise(0.4) * 5000));
//        lines.add(new Line2D.Double(400, noiseMapGenerator.noise(0.4) * 5000, 500, noiseMapGenerator.noise(0.5) * 5000));

        for (int i = 0; i < 10000; i+= 50) {
            lines.add(new Line2D.Double(i, noiseMapGenerator.noise(i/1000f) * 1000, i + 50, noiseMapGenerator.noise((i + 50) / 1000f) * 1000));
            Body line = new Body();
            Point2D topLeft = new Point2D.Double(i, noiseMapGenerator.noise(i/1000f) * 1000);
            Point2D bottemRight = new Point2D.Double(i + 50, noiseMapGenerator.noise((i + 50) / 1000f) * 1000);
            Vector2 v1 = new Vector2(((Point2D.Double) topLeft).x, ((Point2D.Double) topLeft).y);
            Vector2 v2 = new Vector2(((Point2D.Double) bottemRight).x, ((Point2D.Double) bottemRight).y);
            double y = v2.y + ((v1.y - v2.y) /2);
            double x = v1.x + 25;
            line.addFixture(Geometry.createRectangle(60, 10));
            line.getTransform().setTranslation(x, y);
            line.rotate(Math.atan((v2.y - v1.y) /(v2.x - v1.x) ), new Vector2(x, y));
            line.setMass(MassType.INFINITE);
            world.addBody(line);
        }


//        floor = new Body();
//        floor.addFixture(Geometry.createRectangle(100, 1));
//        floor.getFixture(0).setFriction(3);
//        floor.getTransform().setTranslation(1, -5);
//        floor.setMass(MassType.INFINITE);
//        world.addBody(floor);
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = graphics.getTransform();

        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);

        graphics.setColor(Color.green);
        graphics.setStroke(new BasicStroke(3));
        for (Line2D line : lines) {
            graphics.draw(line);
        }

        for (GameObject go : gameObjects) {
            go.draw(graphics);
        }

        if (debugSelected) {
            graphics.setColor(Color.blue);
            DebugDraw.draw(graphics, world, 1);
        }

        graphics.setTransform(originalTransform);
    }

    public void update(double deltaTime) {
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 1);
        world.update(deltaTime);
    }

    public static void main(String[] args) {
        launch(Eindopdracht.class);
    }

}