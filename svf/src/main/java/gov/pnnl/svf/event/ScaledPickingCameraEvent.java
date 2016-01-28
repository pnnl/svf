/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.pnnl.svf.event;

import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.picking.PickingCamera;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Event pushed by a picking camera in a canvas that is not the same size as the
 * view.
 *
 * @author Arthur
 */
public class ScaledPickingCameraEvent extends PickingCameraEvent {

    private static final long serialVersionUID = 1L;

    private final Point2D scale;

    /**
     * Constructor
     *
     * @param source the source camera of the event
     * @param x      the x coord in screen space
     * @param y      the y coord in screen space
     * @param clicks the amount which can represent button or wheel clicks
     * @param types  the types of events
     * @param scale  the scale
     */
    public ScaledPickingCameraEvent(final PickingCamera source, final int x, final int y, final int clicks, final Set<CameraEventType> types, final Point2D scale) {
        super(source, x, y, clicks, types);
        if (scale == null) {
            throw new NullPointerException("scale");
        }
        if (scale.getWidth() <= 0.0 || scale.getHeight() <= 0.0) {
            throw new IllegalArgumentException("scale");
        }
        this.scale = scale;
    }

    /**
     * Constructor
     *
     * @param source the source camera of the event
     * @param x      the x coord center in screen space
     * @param y      the y coord center in screen space
     * @param width  the width in screen space
     * @param height the height in screen space
     * @param clicks the amount which can represent button or wheel clicks
     * @param types  the types of events
     * @param scale  the scale
     *
     * @throws IllegalArgumentException if the width or height is less than 1
     */
    public ScaledPickingCameraEvent(final PickingCamera source, final int x, final int y, final int width, final int height, final int clicks, final Set<CameraEventType> types, final Point2D scale) {
        super(source, x, y, width, height, clicks, types);
        if (scale == null) {
            throw new NullPointerException("scale");
        }
        if (scale.getWidth() <= 0.0 || scale.getHeight() <= 0.0) {
            throw new IllegalArgumentException("scale");
        }
        this.scale = scale;
    }

    /**
     * Constructor
     *
     * @param copy  the event to copy
     * @param scale the scale
     */
    public ScaledPickingCameraEvent(final PickingCameraEvent copy, final Point2D scale) {
        this(copy.getSource(), copy.getX(), copy.getY(), copy.getWidth(), copy.getHeight(), copy.getClicks(), copy.getTypes(), scale);
    }

    @Override
    public int getX() {
        return (int) (super.getX() * scale.getX());
    }

    @Override
    public int getY() {
        return (int) (super.getY() * scale.getY());
    }

    @Override
    public int getWidth() {
        return (int) (super.getWidth() * scale.getWidth());
    }

    @Override
    public int getHeight() {
        return (int) (super.getHeight() * scale.getHeight());
    }

    public static class Builder {

        private Set<CameraEventType> types;
        private PickingCamera source = null;
        private int x = 0;
        private int y = 0;
        private int width = 1;
        private int height = 1;
        private int clicks = 0;
        private Point2D scale = new Point2D(1.0, 1.0);

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public Builder types(final CameraEventType... types) {
            this.types = types != null ? EnumSet.copyOf(Arrays.asList(types)) : null;
            return this;
        }

        public Builder types(final Set<CameraEventType> types) {
            this.types = types;
            return this;
        }

        public Builder source(final PickingCamera source) {
            this.source = source;
            return this;
        }

        public Builder x(final int x) {
            this.x = x;
            return this;
        }

        public Builder y(final int y) {
            this.y = y;
            return this;
        }

        public Builder width(final int width) {
            this.width = width;
            return this;
        }

        public Builder height(final int height) {
            this.height = height;
            return this;
        }

        public Builder clicks(final int clicks) {
            this.clicks = clicks;
            return this;
        }

        public Builder scale(final Point2D scale) {
            this.scale = scale;
            return this;
        }

        public ScaledPickingCameraEvent build() {
            return new ScaledPickingCameraEvent(source, x, y, width, height, clicks, types, scale);
        }
    }

}
