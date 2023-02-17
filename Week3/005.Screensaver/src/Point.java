import java.awt.geom.Point2D;

public class Point {
    private Point2D point;
    private int direction;
    private double rc;


    public Point(Point2D point, int direction, double rc) {
        this.point = point;
        this.direction = direction;
        this.rc = rc;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public double getRc() {
        return rc;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }
}
