import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;


public class BruteCollinearPoints {
   private int N;
   private Node line; //因为不知道有多少条线，所以最好用链表初始化以节约空间

    private class Node {
        LineSegment val;
        Node prev;
    }

    public BruteCollinearPoints(Point[] points) {
        if(points == null)
            throw new java.lang.IllegalArgumentException();

        N=0;
        int num = points.length;
        Point[] clone = new Point[num];

        for (int i = 0; i < num; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();

            for (int j = i + 1; j < num; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
            clone[i] = points[i];
        }

        Arrays.sort(clone);//避免线段起点选在中间点
    for(int i = 0; i < num -3; i++) {
        for (int j = i+1; j<num -2; j++) {
            for (int p = j+1; p<num -1; p++) {
                for(int q = p+1; q < num; q++) {
                    double k1 = clone[i].slopeTo(clone[j]);
                    double k2 = clone[j].slopeTo(clone[p]);
                    double k3 = clone[p].slopeTo(clone[q]);
                    if (k1 == k2 && k2 == k3) {
                        if (line != null) {
                            Node new_node = new Node();
                            new_node.prev = line;
                            new_node.val = new LineSegment(clone[i], clone[q]);
                            line = new_node; //由于随着增加线段，line 不再指向开头，所以用prev而不是next
                        }
                        else{
                            line = new Node();
                            line.val = new LineSegment(clone[i], clone[q]);
                        }
                        N++;
                    }
                }
            }
        }
    }
    }    // finds all line segments containing 4 points

    public int numberOfSegments() {
        return N;
    }        // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] lines = new LineSegment[N];
        Node copy = line; //不改变原数组
        for (int i = 0; i<N; i++) {
            lines[i] = copy.val;
            copy = copy.prev;
        }
        return lines;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (Point p : points) {
            p.draw();
        }

        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        StdDraw.show();
    }
}
