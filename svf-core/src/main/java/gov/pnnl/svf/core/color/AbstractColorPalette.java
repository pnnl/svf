package gov.pnnl.svf.core.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Color palette is an abstract container and repository for a set of colors (or
 * objects). The color object utilized should be immutable for correctness.
 *
 * @param <T> type of color object for the palette
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractColorPalette<T> {

    protected final Set<T> available;
    protected int index;
    protected final List<T> palette;

    /**
     * Constructor
     *
     * @param colors a non empty series of colors
     */
    public AbstractColorPalette(final T... colors) {
        this(Arrays.asList(colors));
    }

    /**
     * Constructor
     *
     * @param colors a non empty collection of colors
     */
    public AbstractColorPalette(final Collection<? extends T> colors) {
        if (colors == null) {
            throw new NullPointerException("colors");
        }
        if (colors.isEmpty()) {
            throw new IllegalArgumentException("colors");
        }
        // maintain the initial order and remove duplicates
        available = (Set<T>) new HashSet<>(colors);
        final List<T> temp = (List<T>) new ArrayList<>(colors);
        temp.retainAll(available);
        palette = Collections.unmodifiableList(temp);
        index = palette.size() - 1;
    }

    /**
     * @return an immutable and static view of the colors in this palette
     */
    public List<T> getColors() {
        return palette;
    }

    /**
     * @return the next color
     */
    public T next() {
        synchronized (this) {
            index++;
            if (index >= palette.size()) {
                index = 0;
            }
            return palette.get(index);
        }
    }

    /**
     * @return the current color
     */
    public T peek() {
        synchronized (this) {
            return palette.get(index);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.palette != null ? this.palette.hashCode() : 0);
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
        final AbstractColorPalette<?> other = (AbstractColorPalette<?>) obj;
        if (this.palette != other.palette && (this.palette == null || !this.palette.equals(other.palette))) {
            return false;
        }
        return true;
    }
}
