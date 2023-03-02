
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.annotation.Resources;

public class VerletEngine extends Application {

    private ResizableCanvas canvas;
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Constraint> constraints = new ArrayList<>();
    private PositionConstraint mouseConstraint = new PositionConstraint(null);

    private boolean showControls = false;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);

        Button doekButton = new Button("doek");
        Button saveButton = new Button("save");
        Button loadButton = new Button("load");
        Button controlsButton = new Button("toggle controls");


        Label label = new Label("Controls:\nLeft click: ball with one line\nLeft click + CTRL: static ball\nLeft click + SHIFT: ball with rope line\nRight click: ball with two lines\nRight click + CTRL: ball with 2 line(same length)\nRight click + SHIFT: line\n\n--SAVE AND LOAD BUTTONS DON'T WORK--");
        label.setFont(Font.font(16));

        HBox hBox = new HBox(10, doekButton, saveButton, loadButton, controlsButton);
        doekButton.setOnAction(event -> drawDoek());
        saveButton.setOnAction(event -> {
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loadButton.setOnAction(event -> {
            try {
                load();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        controlsButton.setOnAction(event -> {
            if (!showControls) {
                mainPane.setRight(label);
            } else {
                mainPane.setRight(null);
            }
            showControls = !showControls;
        });
        mainPane.setTop(hBox);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
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

        // Mouse Events
        canvas.setOnMouseClicked(e -> mouseClicked(e));
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Verlet Engine");
        stage.show();
        draw(g2d);
    }

    private void load() throws IOException, ClassNotFoundException {
        File file = new File("saveParticlesFile.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        this.particles = (ArrayList<Particle>) objectInputStream.readObject();

        File file2 = new File("saveConstraintsFile.txt");
        objectInputStream = new ObjectInputStream(new FileInputStream(file2));
        this.constraints = (ArrayList<Constraint>) objectInputStream.readObject();

    }

    private void save() throws IOException {
        File file = new File("saveParticlesFile.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));

        objectOutputStream.writeObject(this.particles);
        objectOutputStream.close();

        File file2 = new File("saveConstraintsFile.txt");
        objectOutputStream = new ObjectOutputStream(new FileOutputStream(file2));

        objectOutputStream.writeObject(this.constraints);
        objectOutputStream.close();
    }

    private void drawDoek() {
        particles.clear();
        constraints.clear();
        constraints.add(mouseConstraint);
        int width = 16;
        int height = 9;

        ArrayList<Particle> topRow = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            Particle particle = new Particle(new Point2D.Double(100 + (100 * i), 100));
            PositionConstraint positionConstraint = new PositionConstraint(particle);
            if (topRow.size() > 0) {
                DistanceConstraint distanceConstraint = new DistanceConstraint(particle, topRow.get(i - 1));
                constraints.add(distanceConstraint);
            }
            topRow.add(particle);
            constraints.add(positionConstraint);
        }
        particles.addAll(topRow);

        ArrayList<Particle> previousRow;
        previousRow = topRow;
        ArrayList<Particle> currentRow = new ArrayList<>();
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width; x++) {
                Particle particle = new Particle(new Point2D.Double(100 + (100 * x), 200 + (100 * y)));
                DistanceConstraint distanceConstraint2 = new DistanceConstraint(particle, previousRow.get(x));
                constraints.add(distanceConstraint2);
                if (currentRow.size() > 0) {
                    DistanceConstraint distanceConstraint = new DistanceConstraint(particle, currentRow.get(x - 1));
                    constraints.add(distanceConstraint);
                }
                currentRow.add(particle);
            }
            particles.addAll(currentRow);

            previousRow.clear();
            previousRow.addAll(currentRow);
            currentRow.clear();
        }
    }

    public void init() {
        for (int i = 0; i < 11; i++) {
            particles.add(new Particle(new Point2D.Double(100 + 50 * i, 100)));
        }

        for (int i = 0; i < 10; i++) {
            constraints.add(new DistanceConstraint(particles.get(i), particles.get(i + 1)));
        }

        constraints.add(new PositionConstraint(particles.get(10)));
        constraints.add(mouseConstraint);
    }

    private void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Constraint c : constraints) {
            c.draw(graphics);
        }

        for (Particle p : particles) {
            p.draw(graphics);
        }
    }

    private void update(double deltaTime) {
        for (Particle p : particles) {
            p.update((int) canvas.getWidth(), (int) canvas.getHeight());
        }

        for (Constraint c : constraints) {
            c.satisfy();
        }
    }

    private void mouseClicked(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        Particle newParticle = new Particle(mousePosition);

        if (!e.isShiftDown()) {
            particles.add(newParticle);
        }
        if (!e.isControlDown() && !e.isShiftDown()) {
            constraints.add(new DistanceConstraint(newParticle, nearest));
        }


        if (e.getButton() == MouseButton.SECONDARY) {
            ArrayList<Particle> sorted = new ArrayList<>();
            sorted.addAll(particles);

            //sorteer alle elementen op afstand tot de muiscursor. De toegevoegde particle staat op 0, de nearest op 1, en de derde op 2
            Collections.sort(sorted, (o1, o2) -> (int) (o1.getPosition().distance(mousePosition) - o2.getPosition().distance(mousePosition)));

            if (e.isControlDown()) {
                constraints.add(new DistanceConstraint(newParticle, nearest, 100));
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2), 100));
            } else if (e.isShiftDown()) {
                constraints.add(new DistanceConstraint(sorted.get(0), sorted.get(1)));
            } else
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2)));
        } else if (e.getButton() == MouseButton.MIDDLE) {
            // Reset
            particles.clear();
            constraints.clear();
            init();
        } else {
            if (e.isControlDown()) {
                constraints.add(new PositionConstraint(newParticle));
            }
            if (e.isShiftDown()) {
                particles.add(newParticle);
                constraints.add(new RopeConstraint(newParticle, nearest));
            }
        }
    }

    private Particle getNearest(Point2D point) {
        Particle nearest = particles.get(0);
        for (Particle p : particles) {
            if (p.getPosition().distance(point) < nearest.getPosition().distance(point)) {
                nearest = p;
            }
        }
        return nearest;
    }

    private void mousePressed(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        if (nearest.getPosition().distance(mousePosition) < 10) {
            mouseConstraint.setParticle(nearest);
        }
    }

    private void mouseReleased(MouseEvent e) {
        mouseConstraint.setParticle(null);
    }

    private void mouseDragged(MouseEvent e) {
        mouseConstraint.setFixedPosition(new Point2D.Double(e.getX(), e.getY()));
    }

    public static void main(String[] args) {
        launch(VerletEngine.class);
    }

}
