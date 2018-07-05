package gov.pnnl.svf.core.color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A color gradient for specifying a series of colors that have a relative
 * position. This implementation will not be efficient for large sets of colors.
 *
 * @author Amelia Bleeker
 */
public class ColorGradient extends Color implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Comparator<ColorEntry> comparator = new ColorEntryComparator();
    private final SortedSet<ColorEntry> colors;

    /**
     * Constructor
     */
    public ColorGradient() {
        this(new ColorEntry(0.0, Color.WHITE));
    }

    /**
     * Constructor
     *
     * @param copy color gradient to copy
     */
    public ColorGradient(final ColorGradient copy) {
        if (copy == null) {
            throw new NullPointerException("copy");
        }
        this.colors = copy.colors;
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public ColorGradient(final byte r, final byte g, final byte b) {
        this(new ColorEntry(0.0, new Color(r, g, b)));
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     * @param a alpha component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public ColorGradient(final byte r, final byte g, final byte b, final byte a) {
        this(new ColorEntry(0.0, new Color(r, g, b, a)));
    }

    /**
     * Constructor
     *
     * @param color the color to use as a seed
     */
    public ColorGradient(final Color color) {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0.0 &le; x &le; 1.0}
     * @param g green component {x: 0.0 &le; x &le; 1.0}
     * @param b blue component {x: 0.0 &le; x &le; 1.0}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0.0
     *                                  to 1.0 inclusive
     */
    public ColorGradient(final float r, final float g, final float b) {
        this(new ColorEntry(0.0, new Color(r, g, b)));
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0.0 &le; x &le; 1.0}
     * @param g green component {x: 0.0 &le; x &le; 1.0}
     * @param b blue component {x: 0.0 &le; x &le; 1.0}
     * @param a alpha component {x: 0.0 &le; x &le; 1.0}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0.0
     *                                  to 1.0 inclusive
     */
    public ColorGradient(final float r, final float g, final float b, final float a) {
        this(new ColorEntry(0.0, new Color(r, g, b, a)));
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public ColorGradient(final int r, final int g, final int b) {
        this(new ColorEntry(0.0, new Color(r, g, b)));
    }

    /**
     * Constructor
     *
     * @param r red component {x: 0 &le; x &le; 255}
     * @param g green component {x: 0 &le; x &le; 255}
     * @param b blue component {x: 0 &le; x &le; 255}
     * @param a alpha component {x: 0 &le; x &le; 255}
     *
     * @throws IllegalArgumentException if r, g, b, or a is not in the range 0
     *                                  to 255 inclusive
     */
    public ColorGradient(final int r, final int g, final int b, final int a) {
        this(new ColorEntry(0.0, new Color(r, g, b, a)));
    }

    /**
     * Constructor
     *
     * @param hex a hex string in the form of RGB, RGBA, RRGGBB, or RRGGBBAA
     *            with or without a preceding #.
     *
     * @throws IllegalArgumentException  if r, g, b, or a is not in the range
     *                                   0.0 to 1.0 inclusive
     * @throws NumberFormatException     if the string is not a valid hex string
     * @throws IndexOutOfBoundsException if the hex string is not at least 6
     *                                   characters
     */
    public ColorGradient(final String hex) {
        this(new ColorEntry(0.0, new Color(hex)));
    }

    /**
     * Constructor
     *
     * @param colors colors
     */
    public ColorGradient(final ColorEntry... colors) {
        if (colors == null) {
            throw new NullPointerException("colors");
        }
        final SortedSet<ColorEntry> temp = new TreeSet<>(comparator);
        for (ColorEntry color : colors) {
            temp.add(color);
        }
        validate(temp);
        this.colors = Collections.unmodifiableSortedSet(temp);
    }

    /**
     * Constructor
     *
     * @param colors colors
     */
    public ColorGradient(final Collection<ColorEntry> colors) {
        if (colors == null) {
            throw new NullPointerException("colors");
        }
        final SortedSet<ColorEntry> temp = new TreeSet<>(comparator);
        for (final ColorEntry color : colors) {
            temp.add(color);
        }
        validate(temp);
        this.colors = Collections.unmodifiableSortedSet(temp);
    }

    /**
     * Get a reference to the underlying color gradient collection.
     *
     * @return the sorted set of color entries
     */
    public SortedSet<ColorEntry> getColors() {
        return colors;
    }

    /**
     * Get the color located along this gradient that is specified by the value.
     *
     * @param value is a relative location along the gradient
     *
     * @return the color
     *
     * @throws IllegalArgumentException if value is less than 0.0 or more than
     *                                  1.0
     */
    public Color getColor(final double value) {
        if (value < 0.0) {
            throw new IllegalArgumentException("value");
        }
        if (value > 1.0) {
            throw new IllegalArgumentException("value");
        }
        ColorEntry lower = null;
        ColorEntry upper = null;
        // locate the lower and upper bound
        for (final ColorEntry entry : colors) {
            if (Double.compare(value, entry.getValue()) == 0) {
                // special case when lower and upper are equal
                return entry.getColor();
            }
            if (value > entry.getValue()) {
                lower = entry;
            }
            if (value < entry.getValue()) {
                upper = entry;
            }
            // exit when lower and upper are located
            if ((lower != null) && (upper != null)) {
                break;
            }
        }
        // validate method and parameter checking should ensure that lower and
        // upper are never null
        assert (lower != null);
        assert (upper != null);
        // linear interpolation
        final double r = lower.getColor().getRed()
                         + ((((value - lower.getValue()) * upper.getColor().getRed()) - ((value - lower.getValue()) * lower.getColor().getRed()))
                            / (upper.getValue() - lower.getValue()));
        final double g = lower.getColor().getGreen()
                         + ((((value - lower.getValue()) * upper.getColor().getGreen()) - ((value - lower.getValue()) * lower.getColor().getGreen()))
                            / (upper.getValue() - lower.getValue()));
        final double b = lower.getColor().getBlue()
                         + ((((value - lower.getValue()) * upper.getColor().getBlue()) - ((value - lower.getValue()) * lower.getColor().getBlue()))
                            / (upper.getValue() - lower.getValue()));
        final double a = lower.getColor().getAlpha()
                         + ((((value - lower.getValue()) * upper.getColor().getAlpha()) - ((value - lower.getValue()) * lower.getColor().getAlpha()))
                            / (upper.getValue() - lower.getValue()));
        return new Color((float) r, (float) g, (float) b, (float) a);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (97 * hash) + (colors != null ? colors.hashCode() : 0);
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
        final ColorGradient other = (ColorGradient) obj;
        if ((colors != other.colors) && ((colors == null) || !colors.equals(other.colors))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ColorGradient{" + "colors=" + colors + '}';
    }

    private static boolean validate(final Collection<ColorEntry> colors) {
        // validate the color gradient
        // the color gradient must go from 0.0 to 1.0
        ColorEntry first = null;
        ColorEntry last = null;
        boolean modified = false;
        for (final ColorEntry entry : colors) {
            if (first == null) {
                first = entry;
            }
            last = entry;
        }
        if (first == null) {
            first = new ColorEntry(0.0, Color.WHITE);
            colors.add(first);
            modified = true;
        }
        if (first.getValue() != 0.0) {
            colors.add(new ColorEntry(0.0, first.getColor()));
            modified = true;
        }
        if (last == null) {
            last = new ColorEntry(1.0, Color.WHITE);
            colors.add(last);
            modified = true;
        }
        if (last.getValue() != 1.0) {
            colors.add(new ColorEntry(1.0, last.getColor()));
            modified = true;
        }
        return modified;
    }

    private static class ColorEntryComparator implements Comparator<ColorEntry>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(final ColorEntry o1, final ColorEntry o2) {
            return Double.compare(o1.getValue(), o2.getValue());
        }
    }

    public static class Builder {

        protected List<ColorEntry> colors = new ArrayList<>();

        protected Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public List<ColorEntry> colors() {
            return this.colors;
        }

        public Builder colors(final Collection<ColorEntry> colors) {
            this.colors.addAll(colors);
            return this;
        }

        public Builder color(final ColorEntry color) {
            this.colors.add(color);
            return this;
        }

        public Builder color(final double value, final Color color) {
            this.colors.add(new ColorEntry(value, color));
            return this;
        }

        public ColorGradient build() {
            return new ColorGradient(this.colors);
        }
    }

}
