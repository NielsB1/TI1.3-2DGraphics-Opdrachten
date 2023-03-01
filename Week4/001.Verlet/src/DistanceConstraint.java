import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class DistanceConstraint implements Constraint {

    private double distance;
    private Particle a;
    private Particle b;

    private double force;

    private Color color;
    private float greenValue = 1/360f * 120f;

    public DistanceConstraint(Particle a, Particle b) {
        this(a, b, a.getPosition().distance(b.getPosition()));
    }

    public DistanceConstraint(Particle a, Particle b, double distance) {
        this.a = a;
        this.b = b;
        this.distance = distance;
        this.force = 0.0;
        this.color = Color.green;
    }

    @Override
    public void satisfy() {

        double currentDistance = a.getPosition().distance(b.getPosition());
        this.force = (currentDistance - distance) / 2;

        //todo monkey code not working, please fix future me :D
        float hue = greenValue;

        if (force > 0) {
            hue = (float) (1 / 360f * (120f - (10 * force)));
        }
        if (hue < 0.0f) {
            hue = 0;
        }

        if (hue > greenValue) {
            hue = greenValue;
        }

        Color color = Color.getHSBColor(hue, 1, 1);
        this.color = color;


        Point2D BA = new Point2D.Double(b.getPosition().getX() - a.getPosition().getX(), b.getPosition().getY() - a.getPosition().getY());
        double length = BA.distance(0, 0);
        if (length > 0.0001) // We kunnen alleen corrigeren als we een richting hebben
        {
            BA = new Point2D.Double(BA.getX() / length, BA.getY() / length);
        } else {
            BA = new Point2D.Double(1, 0);
        }

        a.setPosition(new Point2D.Double(a.getPosition().getX() + BA.getX() * force,
                a.getPosition().getY() + BA.getY() * force));
        b.setPosition(new Point2D.Double(b.getPosition().getX() - BA.getX() * force,
                b.getPosition().getY() - BA.getY() * force));
    }

    @Override
    public void draw(FXGraphics2D g2d) {
        g2d.setColor(getColor());
        g2d.draw(new Line2D.Double(a.getPosition(), b.getPosition()));
    }

    public Color getColor() {
        return color;
    }
}
