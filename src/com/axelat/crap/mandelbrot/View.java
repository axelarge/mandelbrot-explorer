package com.axelat.crap.mandelbrot;

class View {
    private static final double SCALE_X = 3.5;
    private static final int SCALE_Y = 2;
    private static final int MAX_ITERATIONS = 10000;

    private final int rows;
    private final int columns;

    private double zoom = 1;
    private double zoomLevel = 0;
    public double x = -.75;
    public double y = 0;

    public View(int rows, int columns) {
        this.columns = columns;
        this.rows = rows;
    }

    public View(int rows) {
        this(rows, (int) (rows / SCALE_Y * SCALE_X * 2));
    }

    public double[][] getSnapshot() {
        double[][] result = new double[this.rows][this.columns];
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                double xd = this.x + ((double) col / this.columns - .5) * zoom * SCALE_X;
                double yd = this.y + ((double) row / this.rows - .5) * zoom * SCALE_Y;
                result[row][col] = this.escapesIn(xd, yd);
            }
        }
        return result;
    }

    private double escapesIn(double x0, double y0) {
        double x = 0;
        double y = 0;
        int i;
        double mod = 0;
        for (i = 0; mod < 20 && i < MAX_ITERATIONS; i++) {
            mod = x * x + y * y;
            double tmp = x * x - y * y + x0;
            y = 2 * x * y + y0;
            x = tmp;
        }

        return i == MAX_ITERATIONS ? -1 : i - log(log(mod)) / log(2);
    }

    private double log(double x) {
        return Math.log(x);
    }

    public void zoomIn(double by) {
        setZoomLevel(getZoomLevel() + by);
    }

    public void zoomOut(double by) {
        setZoomLevel(getZoomLevel() - by);
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
        this.zoom = 1.0 / Math.pow(2, zoomLevel);
    }

    public void up(double by) {
        y -= by * SCALE_Y * zoom;
    }

    public void down(double by) {
        y += by * SCALE_Y * zoom;
    }

    public void left(double by) {
        x -= by * SCALE_X * zoom;
    }

    public void right(double by) {
        x += by * SCALE_X * zoom;
    }

    void goTo(Location location) {
        x = location.x;
        y = location.y;
        setZoomLevel(location.zoom);
    }
}