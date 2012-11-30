package com.axelat.crap.mandelbrot;

class Location {
    public final String name;
    public final double x;
    public final double y;
    public final double zoom;

    Location(String name, double x, double y, double zoom) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }
}
