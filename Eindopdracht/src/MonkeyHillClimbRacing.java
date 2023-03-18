
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.*;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class MonkeyHillClimbRacing extends Application {

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
    private double scale = 100;
    private MainMenu mainMenu;
    private boolean isGameStarted = false;


    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();

        // Add debug button
            javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");
            showDebug.setFocusTraversable(false);

            showDebug.setOnAction(e -> {
                debugSelected = showDebug.isSelected();
            });
            mainPane.setTop(showDebug);

        canvas = new ResizableCanvas(g -> {
            if (isGameStarted) {
                draw(g);
            } else {
                mainMenu.draw(g);
            }
        }, mainPane);

        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        camera = new Camera(canvas, g -> draw(g), g2d);
        canvas.setFocusTraversable(true);

        canvas.setOnKeyPressed(event -> onKeyPressed(event));
        canvas.setOnKeyReleased(event -> onKeyReleased(event));

        mainMenu = new MainMenu(canvas, this);
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;

                if (isGameStarted) {
                    draw(g2d);
                } else {
                    mainMenu.draw(g2d);
                }
            }
        }.start();
        mousePicker = new MousePicker(canvas);

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Joe");
        stage.show();
    }

    private KeyCode currentKey = null;

    private void onKeyReleased(KeyEvent event) {
        currentKey = null;
    }

    private void onKeyPressed(KeyEvent event) {
        currentKey = event.getCode();
    }

    private Body car;
    private Body leftWheel;
    private Body rightWheel;
    private WheelJoint leftSpring;
    private WheelJoint rightSpring;
    private Body leftSpringPoint;
    private Body rightSpringPoint;
    private Body driverHead;

    private ArrayList<Body> groundBodies = new ArrayList<>();

    private int resolution = 100;
    private int amplitude = 500;
    private double distance = 1000;

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.81));

        noiseMapGenerator = new NoiseMapGenerator(420);


        for (int i = 0; i < (1000 * resolution); i += resolution) {
            Body line = new Body();

            Point2D.Double topLeft = new Point2D.Double(i, noiseMapGenerator.noise(i / distance) * amplitude);
            Point2D.Double bottomRight = new Point2D.Double(i + resolution, noiseMapGenerator.noise((i + resolution) / distance) * amplitude);

            lines.add(new Line2D.Double(topLeft, bottomRight));

            double y = bottomRight.y + ((topLeft.y - bottomRight.y) / 2);
            double x = topLeft.x + (resolution / 2f);
            line.rotate(Math.atan((bottomRight.y - topLeft.y) / (bottomRight.x - topLeft.x)));

            line.addFixture(Geometry.createRectangle((topLeft.distance(bottomRight) / scale) - 0.029, 0.01));
            line.getTransform().setTranslation(x / scale, y / scale);
            line.setMass(MassType.INFINITE);

            groundBodies.add(line);
            world.addBody(line);
        }

        car = new Body();
        car.addFixture(Geometry.createRectangle(2.4, .30));
        car.addFixture(Geometry.createRectangle(1, .65));

        car.getTransform().setTranslation(-3, 1);
        car.setMass(MassType.NORMAL);
        world.addBody(car);

        driverHead = new Body();
        driverHead.addFixture(Geometry.createRectangle(0.4, 0.25));
        driverHead.setMass(MassType.NORMAL);
        driverHead.getTransform().setTranslation(car.getTransform().getTranslationX(), car.getTransform().getTranslationY() + 0.5);
        world.addBody(driverHead);


        gameObjects.add(new GameObject("Everything/monkeyHead.png", driverHead, new Vector2(driverHead.getTransform().getTranslationX() - 20, driverHead.getTransform().getTranslationY() + 75), 0.2));


        gameObjects.add(new GameObject("Everything/Car.png", car, new Vector2(car.getTransform().getTranslationX(), car.getTransform().getTranslationY() - 10), 1));


        leftSpringPoint = new Body();
        leftSpringPoint.addFixture(Geometry.createRectangle(0.05, 0.05));
        leftSpringPoint.getTransform().setTranslation(-3.85, 0.52);
        leftSpringPoint.setMass(MassType.NORMAL);

        WeldJoint leftSpringToCar = new WeldJoint(leftSpringPoint, car, new Vector2(leftSpringPoint.getTransform().getTranslationX(), leftSpringPoint.getTransform().getTranslationY()));

        world.addJoint(leftSpringToCar);

        leftWheel = new Body();
        leftWheel.addFixture(Geometry.createCircle(.25));
        leftWheel.getFixture(0).setFriction(25);
        leftWheel.getTransform().setTranslation(leftSpringPoint.getTransform().getTranslation());
        leftWheel.setMass(MassType.NORMAL);
        world.addBody(leftWheel);
        gameObjects.add(new GameObject("Everything/Tire.png", leftWheel, new Vector2(leftWheel.getTransform().getTranslationX() + 2.7, leftWheel.getTransform().getTranslationY() - 0.1), 0.85));


        leftSpring = new WheelJoint(leftWheel, car, leftSpringPoint.getTransform().getTranslation(), new Vector2(0, 1));
        leftSpring.setDampingRatio(0.6);


        rightSpringPoint = new Body();
        rightSpringPoint.addFixture(Geometry.createRectangle(0.05, 0.05));
        rightSpringPoint.getTransform().setTranslation(-2.2, 0.52);
        rightSpringPoint.setMass(MassType.NORMAL);

        WeldJoint rightSpringToCar = new WeldJoint(rightSpringPoint, car, new Vector2(rightSpringPoint.getTransform().getTranslationX(), rightSpringPoint.getTransform().getTranslationY()));

        world.addJoint(rightSpringToCar);

        rightWheel = new Body();
        rightWheel.addFixture(Geometry.createCircle(.25));
        rightWheel.getFixture(0).setFriction(25);
        rightWheel.getTransform().setTranslation(rightSpringPoint.getTransform().getTranslation());
        rightWheel.setMass(MassType.NORMAL);
        world.addBody(rightWheel);
        gameObjects.add(new GameObject("Everything/Tire.png", rightWheel, new Vector2(rightWheel.getTransform().getTranslationX() + 2.5, rightWheel.getTransform().getTranslationY() - 0.1), 0.85));

        rightSpring = new WheelJoint(rightWheel, car, rightSpringPoint.getTransform().getTranslation(), new Vector2(0, 1));
        rightSpring.setDampingRatio(0.6);

        leftSpring.setFrequency(8);
        rightSpring.setFrequency(8);

        world.addJoint(leftSpring);
        world.addJoint(rightSpring);


        RevoluteJoint headToCar = new RevoluteJoint(driverHead, car, new Vector2(car.getTransform().getTranslationX(), car.getTransform().getTranslationY() + 0.2));
        headToCar.setCollisionAllowed(true);
        world.addJoint(headToCar);


        floor = new Body();
        floor.addFixture(Geometry.createRectangle(20.00, .01));
        floor.getFixture(0).setFriction(1);
        floor.getTransform().setTranslation(-10.00, 0);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = graphics.getTransform();

        Vector2 carPosition = car.getTransform().getTranslation();
        camera.setCenter(carPosition.x * scale, carPosition.y * scale);

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
            DebugDraw.draw(graphics, world, scale);
        }

        graphics.setTransform(originalTransform);

    }

    public void update(double deltaTime) {
        if (isGameStarted) {
            world.update(deltaTime);

            int x = (int) (driverHead.getTransform().getTranslationX() * scale);
            if (driverHead.getTransform().getTranslationX() > 0)


                if ((driverHead.getTransform().getTranslationY() * scale) - (noiseMapGenerator.noise(x / distance) * amplitude) < 50) {
                    //todo monkey do be dead, return to start screen
                    this.isGameStarted = false;
                    gameObjects.clear();
                    lines.clear();
                    groundBodies.clear();
                    init();
                }

//        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), scale);

            if (currentKey != null) {
                if (currentKey.equals(KeyCode.W)) {
                    driveForwards();
                } else if (currentKey.equals(KeyCode.S)) {
                    driveBackwards();
                } else if (currentKey.equals(KeyCode.A)) {
                    rotateLeft();
                } else if (currentKey.equals(KeyCode.D)) {
                    rotateRight();
                }
            }

            if (groundBodies.get((groundBodies.size() / 3) * 2).getTransform().getTranslationX() < car.getTransform().getTranslationX()) {
                generateGround(100);
            } else if (groundBodies.get(groundBodies.size() / 3).getTransform().getTranslationX() > car.getTransform().getTranslationX()
                    && car.getTransform().getTranslationX() > 100) {
                generatePreviousGround();
            }
        }
    }

    private void rotateRight() {
        car.applyImpulse(-0.04);
    }

    private void rotateLeft() {
        car.applyImpulse(0.04);
    }

    private void driveBackwards() {
        leftWheel.applyImpulse(0.008);
        rightWheel.applyImpulse(0.008);
    }

    private void driveForwards() {
        leftWheel.applyImpulse(-0.008);
        rightWheel.applyImpulse(-0.008);
    }

    public void generateGround(int amount) {
        int i = 0;
        Iterator<Body> iterator = groundBodies.iterator();
        Iterator<Line2D> iter = lines.iterator();
        while (iterator.hasNext()) {
            Body groundBody = iterator.next();
            Line2D line = iter.next();
            if (i < amount) {
                world.removeBody(groundBody);
                iterator.remove();
                iter.remove();
            }
            i++;
        }


        System.out.println(groundBodies.get(groundBodies.size() - 1).getTransform().getTranslationX() * scale);
        int startIndex = (int) ((groundBodies.get(groundBodies.size() - 1).getTransform().getTranslationX() * scale) + resolution / 2);

        for (int j = startIndex; j < startIndex + (amount * resolution); j += resolution) {

            Body line = new Body();

            Point2D.Double topLeft = new Point2D.Double(j, noiseMapGenerator.noise(j / distance) * amplitude);
            Point2D.Double bottomRight = new Point2D.Double(j + resolution, noiseMapGenerator.noise((j + resolution) / distance) * amplitude);

            lines.add(new Line2D.Double(topLeft, bottomRight));

            double y = bottomRight.y + ((topLeft.y - bottomRight.y) / 2);
            double x = topLeft.x + (resolution / 2f);
            line.rotate(Math.atan((bottomRight.y - topLeft.y) / (bottomRight.x - topLeft.x)));

            line.addFixture(Geometry.createRectangle((topLeft.distance(bottomRight) / scale) - 0.029, 0.01));
            line.getTransform().setTranslation(x / scale, y / scale);
            line.setMass(MassType.INFINITE);

            groundBodies.add(line);
            world.addBody(line);
        }
    }

    public void generatePreviousGround() {

        //todo maybe fix later
//        int i = 0;
//        Iterator<Body> iterator = groundBodies.iterator();
//        while (iterator.hasNext()) {
//            Body groundBody = iterator.next();
//            if (i > groundBodies.size() - 101) {
//                world.removeBody(groundBody);
//                iterator.remove();
//            }
//            i++;
//        }
//        System.out.println(groundBodies.get(0).getTransform().getTranslationX() * scale);
//        int startIndex = (int) ((groundBodies.get(0).getTransform().getTranslationX() * scale) + resolution / 2);
//        for (int j = startIndex; j < startIndex + (100 * resolution); j += resolution) {
//
//            Body line = new Body();
//
//            Point2D.Double topLeft = new Point2D.Double(j, noiseMapGenerator.noise(j / distance) * amplitude);
//            Point2D.Double bottomRight = new Point2D.Double(j + resolution, noiseMapGenerator.noise((j + resolution) / distance) * amplitude);
//
//            lines.add(new Line2D.Double(topLeft, bottomRight));
//
//            double y = bottomRight.y + ((topLeft.y - bottomRight.y) / 2);
//            double x = topLeft.x + (resolution / 2f);
//            line.rotate(Math.atan((bottomRight.y - topLeft.y) / (bottomRight.x - topLeft.x)));
//
//            line.addFixture(Geometry.createRectangle((topLeft.distance(bottomRight) / scale) - 0.029, 0.01));
//            line.getTransform().setTranslation(x / scale, y / scale);
//            line.setMass(MassType.INFINITE);
//
//            groundBodies.add(line);
//            world.addBody(line);
//        }

    }

    public static void main(String[] args) {
        launch(MonkeyHillClimbRacing.class);
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }
}