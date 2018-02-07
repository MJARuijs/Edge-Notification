package mjaruijs.edge_notification.color_picker;

public enum Color {

    RED(246,  64,  44),
    PINK(235,  20,  96),
    PURPLE(156,  26, 177),
    DEEP_PURPLE(102,  51, 185),
    INDIGO(61,  77, 183),
    BLUE(16, 147, 245),
    LIGHT_BLUE(0, 166, 246),
    CYAN(0, 187, 213),
    TEAL(0, 150, 135),
    GREEN(70, 175,  74),
    LIGHT_GREEN(136, 196,  64),
    LIME(205, 221,  30),
    YELLOW(255, 236,  22),
    AMBER(255, 192,   0),
    ORANGE(255, 152,   0),
    DEEP_ORANGE(255,  85,   5),
    BROWN(122,  85,  71),
    GREY(157, 157, 157),
    BLUE_GREY(94, 124, 139),
    WHITE(255, 255, 255);

    private int r;
    private int g;
    private int b;

    Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int r() {
        return r;
    }

    public int g() {
        return g;
    }

    public int b() {
        return b;
    }
}
