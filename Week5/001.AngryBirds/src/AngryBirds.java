
import java.awt.*;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.collision.Filter;
import org.dyn4j.collision.narrowphase.Sat;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.DetectResult;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.PinJoint;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Shape;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class AngryBirds extends Application {

    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    private Body catapult;
    private PinJoint catapultJoint;

    private Body ball;

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
        stage.setTitle("Angry Birds");
        stage.show();
        draw(g2d);
    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.8));


        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(100, 1));
        floor.getFixture(0).setFriction(3);
        floor.getTransform().setTranslation(1, -5);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);
        gameObjects.add(new GameObject("ground.png", floor, new Vector2(0, -175), 1));

        Body leftWall = new Body();
        leftWall.addFixture(Geometry.createRectangle(1, 1000));
        leftWall.getFixture(0).setFriction(0.6);
        leftWall.getTransform().setTranslation(-10.2, 0);
        leftWall.setMass(MassType.INFINITE);
        world.addBody(leftWall);

        Body rightWall = new Body();
        rightWall.addFixture(Geometry.createRectangle(1, 1000));
        rightWall.getFixture(0).setFriction(0.6);
        rightWall.getTransform().setTranslation(10.2, 0);
        rightWall.setMass(MassType.INFINITE);
        world.addBody(rightWall);

        this.catapult = new Body();
        this.catapult.getTransform().setTranslation(-7.25, -2.85);
        this.catapult.setMass(MassType.INFINITE);
        world.addBody(this.catapult);

        Body catapult = new Body();
        catapult.getTransform().setTranslation(-7.2, -3.5);
        catapult.setMass(MassType.INFINITE);
        world.addBody(catapult);
        gameObjects.add(new GameObject("Catapult.png", catapult, new Vector2(225, 0), 0.25));

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10 - y; x++) {
                Body box = new Body();
                box.addFixture(Geometry.createRectangle(0.5, 0.5));
                box.setMass(MassType.NORMAL);
                box.getTransform().setTranslation(4 + (x * 0.5) + 0.25 * y, (y * 0.5) - 4.25);
                world.addBody(box);
                gameObjects.add(new GameObject("crate.png", box, new Vector2(0, 0), 0.2));

            }
        }

        ball = new Body();
        ball.addFixture(Geometry.createCircle(0.15));
        ball.getTransform().setTranslation(this.catapult.getTransform().getTranslation());
        ball.getContacts(false);
        Mass mass = new Mass(new Vector2(0, 0), 4, 0.000795);
        ball.setMass(mass);
        ball.getFixture(0).setRestitution(0.4);
        world.addBody(ball);
        gameObjects.add(new GameObject("AngreMonkeyBall.png", ball, new Vector2(0, -50), 0.06));



        catapultJoint = new PinJoint(ball, this.catapult.getTransform().getTranslation(), 10000, 0, 700);
        catapultJoint.setTarget(this.catapult.getTransform().getTranslation());
        world.addJoint(catapultJoint);
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = graphics.getTransform();

        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);

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
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);
    }

    public static void main(String[] args) {
        launch(AngryBirds.class);
    }

    public void throwMonkey() {
        world.removeJoint(catapultJoint);
    }

}
