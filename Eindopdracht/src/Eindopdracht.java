
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.collision.Filter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.*;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Eindopdracht extends Application {

    private ResizableCanvas canvas;
    private World world;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Body floor;
    private MousePicker mousePicker;
    private NoiseMapGenerator noiseMapGenerator;
    private ArrayList<Line2D> lines = new ArrayList<>();
    private boolean firstDraw = true;

    @Override
    public void start(Stage stage) throws Exception {
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
        mousePicker = new MousePicker(canvas);

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Joe");
        stage.show();
        draw(g2d);
    }

    private Body car;
    private Body leftWheel;
    private Body rightWheel;
    private WheelJoint leftSpring;
    private WheelJoint rightSpring;
    private Body leftSpringPoint;
    private Body rightSpringPoint;

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.81));

        noiseMapGenerator = new NoiseMapGenerator();
        int resolution = 50;
        for (int i = 0; i < 10000; i += resolution) {
            lines.add(new Line2D.Double(i, noiseMapGenerator.noise(i / 1000f) * 1000, i + resolution, noiseMapGenerator.noise((i + resolution) / 1000f) * 1000));
            Body line = new Body();
            Point2D topLeft = new Point2D.Double(i, noiseMapGenerator.noise(i / 1000f) * 1000);
            Point2D bottomRight = new Point2D.Double(i + resolution, noiseMapGenerator.noise((i + resolution) / 1000f) * 1000);
            Vector2 v1 = new Vector2(((Point2D.Double) topLeft).x, ((Point2D.Double) topLeft).y);
            Vector2 v2 = new Vector2(((Point2D.Double) bottomRight).x, ((Point2D.Double) bottomRight).y);
            double y = v2.y + ((v1.y - v2.y) / 2);
            double x = v1.x + (resolution / 2f);
            line.rotate(Math.atan((v2.y - v1.y) / (v2.x - v1.x)), new Vector2(x / 100, y / 100));
            line.addFixture(Geometry.createRectangle((0.45 + (Math.abs(line.getTransform().getRotation()) / 3.5)) * (resolution / 50f), 0.05));
            line.getTransform().setTranslation(x / 100, y / 100);
            line.setMass(MassType.INFINITE);
            world.addBody(line);
        }
        car = new Body();
        car.addFixture(Geometry.createRectangle(2.4, .30));
        car.addFixture(Geometry.createRectangle(1, .70));

        car.getTransform().setTranslation(-3, 1);
        car.setMass(MassType.NORMAL);
        world.addBody(car);
        gameObjects.add(new GameObject("Everything/Car.png", car, new Vector2(car.getTransform().getTranslationX(), car.getTransform().getTranslationY() - 10), 1));


        //todo make monkey springs works
        leftSpringPoint = new Body();
        leftSpringPoint.addFixture(Geometry.createRectangle(0.05, 0.05));
        leftSpringPoint.getTransform().setTranslation(-3.85, 0.55);
        leftSpringPoint.setMass(MassType.NORMAL);

        RevoluteJoint leftSpringToCar = new RevoluteJoint(leftSpringPoint, car, new Vector2(leftSpringPoint.getTransform().getTranslationX(), leftSpringPoint.getTransform().getTranslationY()));

        world.addJoint(leftSpringToCar);
//        world.addBody(leftSpringPoint);

        leftWheel = new Body();
        leftWheel.addFixture(Geometry.createCircle(.25));
        leftWheel.getFixture(0).setFriction(10);
        leftWheel.getTransform().setTranslation(leftSpringPoint.getTransform().getTranslation());
        leftWheel.setMass(MassType.NORMAL);
        world.addBody(leftWheel);
        gameObjects.add(new GameObject("Everything/Tire.png", leftWheel, new Vector2(leftWheel.getTransform().getTranslationX() + 2.5, leftWheel.getTransform().getTranslationY()), 0.85));


        leftSpring = new WheelJoint(leftWheel, car, leftSpringPoint.getTransform().getTranslation(), new Vector2(0, 1));


        rightSpringPoint = new Body();
        rightSpringPoint.addFixture(Geometry.createRectangle(0.05, 0.05));
        rightSpringPoint.getTransform().setTranslation(-2.2, 0.55);
        rightSpringPoint.setMass(MassType.NORMAL);

        RevoluteJoint rightSpringToCar = new RevoluteJoint(rightSpringPoint, car, new Vector2(rightSpringPoint.getTransform().getTranslationX(), rightSpringPoint.getTransform().getTranslationY()));

        world.addJoint(rightSpringToCar);
//        world.addBody(rightSpringPoint);

        rightWheel = new Body();
        rightWheel.addFixture(Geometry.createCircle(.25));
        rightWheel.getFixture(0).setFriction(10);
        rightWheel.getTransform().setTranslation(rightSpringPoint.getTransform().getTranslation());
        rightWheel.setMass(MassType.NORMAL);
        world.addBody(rightWheel);
        gameObjects.add(new GameObject("Everything/Tire.png", rightWheel, new Vector2(rightWheel.getTransform().getTranslationX() + 2.5, rightWheel.getTransform().getTranslationY()), 0.85));

        rightSpring = new WheelJoint(rightWheel, car, rightSpringPoint.getTransform().getTranslation(), new Vector2(0, 1));

        world.addJoint(leftSpring);
        world.addJoint(rightSpring);


        floor = new Body();
        floor.addFixture(Geometry.createRectangle(20.00, .05));
        floor.getFixture(0).setFriction(3);
        floor.getTransform().setTranslation(-10.00, 0);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);
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
            DebugDraw.draw(graphics, world, 100);
        }

        graphics.setTransform(originalTransform);
    }

    public void update(double deltaTime) {
        world.update(deltaTime);
        leftWheel.applyImpulse(-0.012);
        rightWheel.applyImpulse(-0.012);
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
    }

    public static void main(String[] args) {
        launch(Eindopdracht.class);
    }

}