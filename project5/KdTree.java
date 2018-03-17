package kdTree;
import  java.lang.IllegalArgumentException;
import java.util.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class KdTree {
    private static boolean odd = false;
    private static boolean even = true;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean parity;

        public Node(Point2D p, RectHV rect, Node lb, Node rt, boolean parity) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.parity = parity;
        }
    }
    private Node root;

    private boolean isEven(Node x) {
        return x.parity == even;
    }

    public KdTree() {
        root = null;
    }// construct an empty set of points

    public boolean isEmpty() {
        return root == null;
    }                      // is the set empty?

    public int size() {
        return size(root);
    }                         // number of points in the set

    private int size(Node x) {
        if (x == null) return 0;
        return 1+size(x.lb) +size(x.rt);
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (root == null) {
            root = new Node(p,new RectHV(0,0,1,1),null,null,odd);
        }

        Node copy = root;

        while (true) {
            if (copy.p.equals(p)) {
                return;
            }
            if (isAtLeftSideOfNode(p, copy)) {
                if (copy.lb == null) {
                    RectHV rect =  isEven(copy) ?  new RectHV(copy.rect.xmin(), copy.rect.ymin(), copy.rect.xmax(), p.y()) :
                            new RectHV(copy.rect.xmin(), copy.rect.ymin(), p.x(), copy.rect.ymax());
                    copy.lb = new Node(p, rect , null, null, isEven(copy) ? odd : even);
                    break;
                } else {
                    copy = copy.lb;
                }
            } else {
                if (copy.rt  == null) {
                    RectHV rect =  isEven(copy) ?  new RectHV(copy.rect.xmin(), p.y(), copy.rect.xmax(), copy.rect.ymax()) :
                            new RectHV(p.x(), copy.rect.ymin(), copy.rect.xmax(), copy.rect.ymax());
                    copy.rt = new Node(p, rect , null, null, isEven(copy) ? odd : even);
                    break;
                } else {
                    copy = copy.rt;
                }
            }
        }
    }              // add the point to the set (if it is not already in the set)

    private boolean isAtLeftSideOfNode(Point2D p,Node x) {
        if (isEven(x)) {
            return p.y() < x.p.y();
        } else {
            return p.x() < x.p.x();
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(p,root);
    }            // does the set contain point p?

    private boolean contains(Point2D p, Node x) {
        if (x == null) return false;
        if (p == x.p) {
            return true;
        } else {
            return contains(p,x.lb) || contains(p,x.rt);
        }
    }

    public void draw() {

    }                         // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> stack = new Stack<>();
        search(rect, root, stack);
        return stack;
    }             // all points that are inside the rectangle (or on the boundary)

    private void search(RectHV rect, Node x, Stack stack) {
        if (rect.contains(x.p)) {
            stack.push(x.p);
        } else {
            if (isEven(x)) {
                if (rect.ymax() >= x.p.y())
                    search(rect, x.rt, stack);
                if (rect.ymin() < x.p.y())
                    search(rect, x.lb, stack);
            } else {
                if (rect.xmax() >= x.p.x())
                    search(rect, x.rt, stack);
                if (rect.xmin() < x.p.x())
                    search(rect, x.lb, stack);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double distance = distance(p,root.p);
        Point2D nearest = root.p;
        return nearest(p,root,distance,nearest);
    }             // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D nearest(Point2D p, Node x, double distance,Point2D nearest) {
        if (distance(p, x.p) < distance) {
            distance = distance(p, x.p);
            nearest = x.p;
        }

        if (isAtLeftSideOfNode(p, x)) {
            nearest = nearest(p, x.lb, distance, nearest);
            if (isEven(x)) {
                if (x.p.y() - p.y() < distance)
                    nearest = nearest(p, x.rt, distance, nearest);
            } else {
                if (x.p.x() - p.x() < distance)
                    nearest = nearest(p, x.rt, distance, nearest);
            }
        } else {
            nearest = nearest(p, x.rt, distance, nearest);
            if (isEven(x)) {
                if (p.y() - x.p.y() < distance)
                    nearest = nearest(p, x.lb, distance, nearest);
            } else {
                if (p.x() - x.p.x() < distance)
                    nearest = nearest(p, x.lb, distance, nearest);
            }
        }

        return nearest;
    }

    private double distance(Point2D a, Point2D b) {
        return Math.pow(a.x()-b.x(),2) + Math.pow(a.y()-b.y(),2);
    }

    public static void main(String[] args) {

        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.show();

        // process range search queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // user starts to drag a rectangle
            if (StdDraw.isMousePressed() && !isDragging) {
                x0 = x1 = StdDraw.mouseX();
                y0 = y1 = StdDraw.mouseY();
                isDragging = true;
            }

            // user is dragging a rectangle
            else if (StdDraw.isMousePressed() && isDragging) {
                x1 = StdDraw.mouseX();
                y1 = StdDraw.mouseY();
            }

            // user stops dragging rectangle
            else if (!StdDraw.isMousePressed() && isDragging) {
                isDragging = false;
            }

            // draw the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw the rectangle
            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                    Math.max(x0, x1), Math.max(y0, y1));
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            rect.draw();

            // draw the range search results for brute-force data structure in red
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            for (Point2D p : brute.range(rect))
                p.draw();

            // draw the range search results for kd-tree in blue
            StdDraw.setPenRadius(.02);
            StdDraw.setPenColor(StdDraw.BLUE);
            for (Point2D p : kdtree.range(rect))
                p.draw();

            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}
