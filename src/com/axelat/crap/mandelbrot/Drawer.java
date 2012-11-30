package com.axelat.crap.mandelbrot;

class Drawer {
    public static class Scheme {
        public final String chars;
        public final double stretch;

        private Scheme(String chars, double stretch) {
            this.chars = chars;
            this.stretch = stretch;
        }

        public static final Scheme OLD = new Scheme(" .,-~+=*&#@XO☻░▒▓█", .3);
        public static final Scheme DEFAULT = new Scheme(" .·◦•©☻░▒▓■██", .15);
    }


    private String chars;
    private char finisher;
    private double stretch;

    public Drawer(Scheme scheme) {
        setScheme(scheme);
    }

    void setScheme(Scheme scheme) {
        this.stretch = scheme.stretch;
        final String chars = scheme.chars;
        finisher = chars.charAt(chars.length() - 1);

        final StringBuilder buffer = new StringBuilder(chars.substring(0, chars.length() - 2));
        this.chars = buffer + buffer.reverse().substring(1, buffer.length() - 2);
    }


    public String draw(double[][] snapshot) {
        final int rows = snapshot.length;
        final int columns = snapshot[0].length;

        StringBuilder builder = new StringBuilder(rows * (columns + 1));
        for (double[] row : snapshot) {
            for (double point : row) {
                if (point == -1) {
                    builder.append(this.finisher);
                } else {
                    final int index = (int) (point * stretch) % chars.length();
                    builder.append(chars.charAt(index));
                }
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}