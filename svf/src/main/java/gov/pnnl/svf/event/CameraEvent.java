package gov.pnnl.svf.event;

import gov.pnnl.svf.camera.Camera;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class CameraEvent extends AbstractCameraEvent<Camera> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     *
     * @param source the source camera of the event
     * @param x      the x coord in screen space
     * @param y      the y coord in screen space
     * @param clicks the amount which can represent button or wheel clicks
     * @param types  the types of events
     */
    public CameraEvent(final Camera source, final int x, final int y, final int clicks, final Set<CameraEventType> types) {
        super(source, x, y, clicks, types);
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
     *
     * @throws IllegalArgumentException if the width or height is less than 1
     */
    public CameraEvent(final Camera source, final int x, final int y, final int width, final int height, final int clicks, final Set<CameraEventType> types) {
        super(source, x, y, width, height, clicks, types);
    }

    public static class Builder {

        private Set<CameraEventType> types;
        private Camera source = null;
        private int x = 0;
        private int y = 0;
        private int width = 1;
        private int height = 1;
        private int context = 0;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public CameraEvent.Builder types(final CameraEventType... types) {
            this.types = types != null ? EnumSet.copyOf(Arrays.asList(types)) : null;
            return this;
        }

        public Builder types(final Set<CameraEventType> types) {
            this.types = types;
            return this;
        }

        public Builder source(final Camera source) {
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

        public Builder context(final int context) {
            this.context = context;
            return this;
        }

        public CameraEvent build() {
            return new CameraEvent(source, x, y, width, height, context, types);
        }
    }

}
