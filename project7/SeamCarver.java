import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    private int[][] colors;
    private double[][] energy;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
        colors = new int[width][height];

        for (int col = 0; col < width; col++)
            for (int row = 0; row < height; row++)
                colors[col][row] = picture.get(col, row).getRGB();

        initEnergy();
    }                // create a seam carver object based on the given picture
    public Picture picture() {
        return picture;
    }                          // current picture

    public int width() {
        return width;
    }                            // width of current picture

    public int height() {
        return height;
    }                           // height of current picture

    public double energy(int x, int y) {
        if (x < 0 || x > width-1 || y < 0|| y > height-1 )
            throw new IllegalArgumentException();

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
           return  1000;

        int top = colors[x][y - 1];
        int down = colors[x][y + 1];
        int left = colors[x - 1][y];
        int right = colors[x + 1][y];

        double deltaX2 = Math.pow(getRed(right) - getRed(left), 2) +
                Math.pow(getGreen(right) - getGreen(left), 2) +
                Math.pow(getBlue(right) - getBlue(left), 2);
        double deltaY2 = Math.pow(getRed(down) - getRed(top), 2) +
                Math.pow(getGreen(down) - getGreen(top), 2) +
                Math.pow(getBlue(down) - getBlue(top), 2);

        return   Math.sqrt(deltaX2 + deltaY2);
    }               // energy of pixel at column x and row y

    public   int[] findHorizontalSeam() {
        if (energy == null) {
           initEnergy();
        }

        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];
        resetDist(distTo);

        for (int row = 0; row < height; row++)
            distTo[0][row] = 1000;

        for (int row = 0;row < height; row++)
            for (int col = 0;col < width-1; col++) {
                relaxHor(col, row,distTo,edgeTo);
            }

        double minValue =  Double.POSITIVE_INFINITY;
        int minLocation = -1;
        for (int row = 0; row < height; row++) {
            if (distTo[width - 1][row] < minValue) {
                minLocation = row;
                minValue = distTo[width - 1][row];
            }
        }

        int[] path = new int[width];
        for (int col = width - 1, row = minLocation; col >= 0; col--,row = minLocation) {//for循环第一项是初始化,之后并不变动,故row需要在第三项变动
            path[col] = row;
            minLocation = edgeTo[col][row];
        }

        return path;
    }               // sequence of indices for horizontal seam

    public   int[] findVerticalSeam() {
        if (energy == null) {
            initEnergy();
        }

        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];
        resetDist(distTo);

        for (int col = 0; col < width; col++)
            distTo[col][0] = 1000;

        for (int col = 0;col < width; col++)
            for (int row = 0;row < height-1; row++) {
                relaxVer(col, row,distTo,edgeTo);
            }

        double minValue = Double.POSITIVE_INFINITY;
        int minLocation = -1;
        for (int col = 0; col < width; col++) {
            if (distTo[col][height - 1] < minValue) {
                minLocation = col;
                minValue = distTo[col][height - 1];
            }
        }

        int[] path = new int[height];
        for (int row = height - 1, col  = minLocation; row >= 0; row--, col  = minLocation) {
            path[row] = col;
            minLocation = edgeTo[col][row];
        }

        return path;
    }                 // sequence of indices for vertical seam

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != width) {
            throw new IllegalArgumentException();
        }

        for (int i : seam) {
            if (seam[i] < 0 || seam[i] >= height || i > 0 && Math.abs(seam[i] - seam[i-1]) > 1)
                throw new IllegalArgumentException();
        }

        for (int col = 0; col < width; col++)
            for (int row = seam[col]; row < height - 1; row++)
                colors[col][row] = colors[col][row+1];

        height--;
        renewHorEnergy(seam);
    }   // remove horizontal seam from current picture

    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != height) {
            throw new IllegalArgumentException();
        }

        for (int i : seam) {
            if (seam[i] < 0 || seam[i] >= width || i > 0 && Math.abs(seam[i] - seam[i-1]) > 1)
                throw new IllegalArgumentException();
        }

        for (int row = 0; row < height; row++)
            for (int col = seam[row]; col < width - 1; col++)
                colors[col][row] = colors[col+1][row];

        width--;
        renewVerEnergy(seam);
    }     // remove vertical seam from current picture

    private int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int getBlue(int rgb) {
        return rgb & 0xFF;
    }

    private void resetDist(double[][] distTo) {
        for (int i = 0; i < distTo.length; i++)
            for (int j = 0; j < distTo[i].length; j++)
                distTo[i][j] = Double.MAX_VALUE;
    }

    private void relaxHor(int col, int row,double[][] disTo,int[][] edgeTo) {
        for (int i = row - 1;i <= row + 1;i++) {
            if (i < 0 || i > height - 1) continue; //这样就不用再for循环中判断了
            if (disTo[col + 1][i] > disTo[col][row] + energy[col + 1][i]) {
                disTo[col + 1][i] = disTo[col][row] + energy[col + 1][i];
                edgeTo[col+1][i] = row;
            }
        }
    }

    private void renewHorEnergy(int[] seam) {
        for (int col = 0; col < width; col++)
            for (int row = seam[col]; row < height - 1; row++)
                energy[col][row] = energy[col][row+1];

        for (int col = 1; col < width - 1;col++) {
            energy[col][seam[col]] = energy(col, seam[col]);
            energy[col][seam[col]-1] = energy(col, seam[col]-1);
        }
    }

    private void relaxVer(int col, int row,double[][] disTo,int[][] edgeTo) {
        for (int i = col - 1;i <= col + 1;i++) {
            if (i < 0 || i > width - 1) continue; //这样就不用再for循环中判断了
            if (disTo[i][row+1] > disTo[col][row] + energy[i][row+1] ) {
                disTo[i][row+1] = disTo[col][row] + energy[i][row+1] ;
                edgeTo[i][row+1]  = col;
            }
        }
    }

    private void renewVerEnergy(int[] seam) {
        for (int row = 0; row < height; row++)
            for (int col = seam[row]; col < width - 1; col++)
                energy[col][row] = energy[col+1][row];

        for (int row = 1; row < height - 1;row++) {
            energy[seam[row]][row] = energy(seam[row],row);
            energy[seam[row]-1][row] = energy(seam[row]-1,row);
        }
    }

    private void initEnergy() {
        energy = new double[width][height];
        for (int col = 0; col < width; col++)
            for (int row = 0; row < height; row++)
                energy[col][row] = energy(col,row);
    }
}