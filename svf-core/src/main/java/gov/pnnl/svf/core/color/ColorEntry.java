package gov.pnnl.svf.core.color;

import java.io.Serializable;

public class ColorEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry BLACK = new ColorEntry(0.0, Color.BLACK);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry BLUE = new ColorEntry(0.0, Color.BLUE);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry BROWN = new ColorEntry(0.0, Color.BROWN);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry DARK_GRAY = new ColorEntry(0.0, Color.DARK_GRAY);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry GRAY = new ColorEntry(0.0, Color.GRAY);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry GREEN = new ColorEntry(0.0, Color.GREEN);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry INDIGO = new ColorEntry(0.0, Color.INDIGO);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry LIGHT_GRAY = new ColorEntry(0.0, Color.LIGHT_GRAY);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry ORANGE = new ColorEntry(0.0, Color.ORANGE);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry RED = new ColorEntry(0.0, Color.RED);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry TRANSPARENT = new ColorEntry(0.0, Color.TRANSPARENT);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry VIOLET = new ColorEntry(0.0, Color.VIOLET);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry WHITE = new ColorEntry(0.0, Color.WHITE);
    /**
     * Color entry that represents a solid color.
     */
    public static final ColorEntry YELLOW = new ColorEntry(0.0, Color.YELLOW);
    private final double value;
    private final Color color;

    /**
     * Constructor
     */
    public ColorEntry() {
        this(0.0, Color.WHITE);
    }

    /**
     * Constructor
     *
     * @param value the relative position value for this entry {x: 0.0 &le x &le
     *              1.0}
     * @param color color for this entry
     */
    public ColorEntry(final double value, final Color color) {
        if ((value < 0.0) || (value > 1.0)) {
            throw new IllegalArgumentException("value");
        }
        if (color == null) {
            throw new NullPointerException("color");
        }
        this.value = value;
        this.color = color;
    }

    /**
     * The color for this entry.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * The relative position value for this entry.
     *
     * @return the relative position value {x: 0.0 &le x &le 1.0}
     */
    public double getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (Double.doubleToLongBits(value) ^ (Double.doubleToLongBits(value) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColorEntry other = (ColorEntry) obj;
        if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ColorEntry{" + "value=" + value + "color=" + color + '}';
    }

    public static class Builder {

        private double value;
        private Color color;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public Builder value(final double value) {
            this.value = value;
            return this;
        }

        public Builder color(final Color color) {
            this.color = color;
            return this;
        }

        public ColorEntry build() {
            return new ColorEntry(value, color);
        }
    }

}
