package kdTree;
import edu.princeton.cs.algs4.SET;
import  java.lang.IllegalArgumentException;
import java.util.Stack;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private SET<Point2D> pointset;

    public PointSET() {
        pointset = new SET<Point2D>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return pointset.isEmpty();
    }                      // is the set empty?

    public int size() {
        return pointset.size();
    }                         // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        pointset.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointset.contains(p);
    }            // does the set contain point p?

    public void draw() {
        for (Point2D point : pointset) {
            point.draw();
        }
    }                         // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> points = new Stack<>();
        for (Point2D point : pointset) {
            if (rect.contains(point))
                points.push(point);
        }
        return points;
    }             // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (pointset == null) return null;
        double distance;
        Point2D nearest = p;

        if (!p.equals(pointset.max()))
            distance = p.distanceSquaredTo(pointset.max());
        else
            distance = p.distanceSquaredTo(pointset.min());

        for (Point2D point : pointset) {
            double temp = p.distanceSquaredTo(point);
            if (temp < distance) {
                nearest = point;
                distance = temp;
            }
        }
        return nearest;
    }             // a nearest neighbor in the set to point p; null if the set is empty

   // public static void main(String[] args)                  // unit testing of the methods (optional)
}
