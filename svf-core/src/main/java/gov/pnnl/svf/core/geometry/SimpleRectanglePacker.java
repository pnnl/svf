package gov.pnnl.svf.core.geometry;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Objects;

/**
 * Simple implementation of a rectangle packer. This packer will simply lay
 * rectangles in a row left to right and top to bottom. What this packer lacks
 * in complexity it makes up for in speed. This packer should be used when speed
 * is paramount or all rectangles tend to be relatively the same height (such is
 * the case in characters.) This packer is not thread safe.
 *
 * @author Arthur Bleeker
 */
public class SimpleRectanglePacker implements RectanglePacker {

    private final Rectangle maximumArea;
    private Rectangle actualArea;
    private Rectangle availableArea;

    /**
     * Constructor
     *
     * @param width  the maximum width
     * @param height the maximum height
     */
    public SimpleRectanglePacker(final int width, final int height) {
        this(new Rectangle(width, height));
    }

    /**
     * Constructor
     *
     * @param area the maximum area
     */
    public SimpleRectanglePacker(final Rectangle area) {
        this.maximumArea = new Rectangle(Objects.requireNonNull(area, "area"));
        this.actualArea = new Rectangle(this.maximumArea.x, this.maximumArea.y, 0, 0);
        this.availableArea = new Rectangle(this.maximumArea);
    }

    @Override
    public Point pack(final int width, final int height) {
        if (width < 0) {
            throw new IllegalArgumentException("width");
        }
        if (height < 0) {
            throw new IllegalArgumentException("height");
        }
        // check to see if this pack uses any space
        if (width == 0 || height == 0) {
            return new Point(this.availableArea.x, this.availableArea.y);
        }
        // move up a row if possible
        if (this.availableArea.width < width && this.actualArea.height + height <= this.maximumArea.height) {
            this.availableArea = new Rectangle(this.maximumArea.x,
                                               this.actualArea.y + this.actualArea.height,
                                               this.maximumArea.width,
                                               this.maximumArea.height - this.actualArea.height);
        }
        // see if this pack will fit on this row
        if (this.availableArea.width >= width && this.availableArea.height >= height) {
            final Point point = new Point(this.availableArea.x,
                                          this.availableArea.y);
            this.actualArea = new Rectangle(this.actualArea.x,
                                            this.actualArea.y,
                                            Math.max(this.actualArea.width, this.maximumArea.width - this.availableArea.width + width),
                                            Math.max(this.actualArea.height, this.maximumArea.height - this.availableArea.height + height));
            this.availableArea = new Rectangle(this.availableArea.x + width,
                                               this.availableArea.y,
                                               this.availableArea.width - width,
                                               this.availableArea.height);
            return point;
        }
        // won't fit
        return null;
    }

    @Override
    public Rectangle getMaximumArea() {
        return new Rectangle(maximumArea);
    }

    @Override
    public Rectangle getActualArea() {
        return new Rectangle(actualArea);
    }

    @Override
    public Rectangle getAvailableArea() {
        return new Rectangle(availableArea);
    }

}
