import java.awt.*;
import java.awt.geom.Point2D;

public class Renderable {
    private Shape shape;
    private Point2D position;
    private Color color;
    public Renderable(Shape shape, Point2D position, Color color)
    {
        this.shape = shape;
        this.position = position;
        this.color = color;
    }

    public Shape getShape() {
        return shape;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}