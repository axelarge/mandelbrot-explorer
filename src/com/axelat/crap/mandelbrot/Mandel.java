package com.axelat.crap.mandelbrot;

import java.io.*;

public class Mandel {

    private final PrintStream out;
    private final InputStream in;

    public static void main(String[] args) throws InterruptedException, IOException {
        final int rows = args.length > 0 ? Integer.parseInt(args[0]) : 16;
        new Mandel(new View(rows), new Drawer(Drawer.Scheme.DEFAULT)).run();
        System.out.println("Bye");
    }

    public static final Location[] LOCATIONS = new Location[]{
            new Location("Home", -0.75, 0, 1),
            new Location("Spiral zoom", -0.74329189085243D, -0.13124055230880D, 9.0),
            new Location("Self-similar ", 0.001643721971153D, 0.822467633298876D, 6),
            new Location("Liquid", -0.74957275390406, -0.03124618529707, 36),
            new Location("Black stuff", -0.2984758406, -0.6584177650, 26.0),
    };


    private final View view;
    private final Drawer drawer;

    private Mandel(View view, Drawer drawer) {
        this.view = view;
        this.drawer = drawer;
        out = System.out;
        in = System.in;
    }

    void run() throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        boolean redraw = true;
        while (true) {
            if (redraw) {
                out.print(drawer.draw(view.getSnapshot()));
                out.printf("X: %.14f  Y: %.14f  Zoom: %.1f  > ", view.x, view.y, view.getZoomLevel());
            }

            redraw = true;

            final String input = reader.readLine();
            if (input.equals("quit")) break;
                // Zooming
            else if (input.equals("+") || input.equals("e")) view.zoomIn(1);
            else if (input.equals("-") || input.equals("q")) view.zoomOut(1);
            else if (input.equals("E")) view.zoomIn(2);
            else if (input.equals("Q")) view.zoomOut(2);
            else if (input.startsWith("zoom")) redraw = zoomCommand(input);
                // Movement
            else if (input.equals("w")) view.up(0.25);
            else if (input.equals("W")) view.up(0.5);
            else if (input.equals("a")) view.left(0.25);
            else if (input.equals("A")) view.left(0.5);
            else if (input.equals("s")) view.down(0.25);
            else if (input.equals("S")) view.down(0.5);
            else if (input.equals("d")) view.right(0.25);
            else if (input.equals("D")) view.right(0.5);
            else if (input.startsWith("go")) redraw = goCommand(input);
                // Other
            else if (input.startsWith("nice")) redraw = listCommand(input);
            else {
                showHelp();
                redraw = false;
            }
        }
    }

    private boolean goCommand(String input) {
        try {
            final String[] parts = input.split(" +");
            final double x = Double.parseDouble(parts[1]);
            final double y = Double.parseDouble(parts[2]);
            view.x = x;
            view.y = y;
            return true;
        } catch (Exception e) {
            if (!(e instanceof NumberFormatException || e instanceof ArrayIndexOutOfBoundsException))
                throw new RuntimeException(e);
            complainAboutFormat("go 0.00164 0.82247");
            return false;
        }
    }

    private boolean zoomCommand(String input) {
        try {
            final String[] parts = input.split(" +");
            view.setZoomLevel(Double.parseDouble(parts[1]));
            return true;
        } catch (Exception e) {
            if (!(e instanceof NumberFormatException || e instanceof ArrayIndexOutOfBoundsException))
                throw new RuntimeException(e);
            complainAboutFormat("zoom 11");
            return false;
        }
    }

    private boolean listCommand(String input) {
        final String[] parts = input.split(" +");
        if (parts.length > 1) {
            try {
                Location location = LOCATIONS[Integer.parseInt(parts[1])];
                view.goTo(location);
                return true;
            } catch (NumberFormatException e) {
                complainAboutFormat("nice 3");
            } catch (ArrayIndexOutOfBoundsException e) {
                out.println("Location with that ID doesn't exist");
            }
        } else {
            showHeader("Check out some of these locations");
            for (int i = 0; i < LOCATIONS.length; i++) {
                Location location = LOCATIONS[i];
                out.printf(" %d) %-14s  %+.5f  %+.5f  %.1f\n", i, location.name, location.x, location.y, location.zoom);
            }
            showHeader("To go to a location type \"nice ID\"");
        }

        return false;
    }

    private void showHelp() {
        showHeader("Plx enter one of these commands");
        out.println(" w a s d | up down left right              ");
        out.println(" + e     | zoom in                         ");
        out.println(" - q     | zoom out                        ");
        out.println(" zoom Z  | set zoom level to Z             ");
        out.println(" go X Y  | go to coordinates               ");
        out.println(" nice    | list of some locations          ");
        out.println(" quit    | quit                            ");
        showHeader("Use capital letters to move/zoom faster");
        out.print("> ");
    }

    private void showHeader(String message) {
        out.println("--------------------------------------------");
        out.println(" " + message);
        out.println("--------------------------------------------");
    }

    private void complainAboutFormat(final String example) {
        out.println("Bad format, should be like: \"" + example + "\"");
    }
}
