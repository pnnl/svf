package gov.pnnl.svf.actor;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.Updatable;
import gov.pnnl.svf.service.AbstractDrawableService;
import gov.pnnl.svf.service.DrawableService;
import java.awt.Font;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Will draw the current fps of the scene.
 *
 * @author Arthur Bleeker
 */
public class FpsActor extends DynamicBorderedShapeActor implements DrawableService, Updatable {

    /**
     * The default type for this actor.
     */
    public static final String DEFAULT_TYPE = "fps";
    /**
     * String representation of a field in this object.
     */
    public static final String INTERVAL = "interval";
    private final DrawableService service = new DrawableServiceImpl(getScene(), 0);
    private final AtomicLong counter = new AtomicLong(0L);
    private final AtomicLong averageFps = new AtomicLong(0L);
    private long elapsed = 0L;
    private long interval = 1000L;

    /**
     * Constructor
     *
     * @param scene The parent scene for the actor.
     */
    public FpsActor(final Scene scene) {
        super(scene, DEFAULT_TYPE, scene.getFactory().newUuid(scene));
    }

    /**
     * Constructor
     *
     * @param scene The parent scene for the actor.
     * @param id    Unique ID for this actor
     */
    public FpsActor(final Scene scene, final String id) {
        super(scene, DEFAULT_TYPE, id);
    }

    /**
     * Constructor
     *
     * @param scene The parent scene for the actor.
     * @param type  The type of this actor.
     * @param id    Unique ID for this actor
     */
    public FpsActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    protected Set<String> getInitializeFields() {
        final Set<String> fields = super.getInitializeFields();
        return fields;
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (camera == null) {
            // we only want to count this when it's run as a service
            counter.incrementAndGet();
        } else {
            // draw for each camera in the scene
            super.draw(gl, glu, camera);
        }
    }

    /**
     * @return the amount of time in milliseconds to update the fps text
     */
    public long getInterval() {
        synchronized (this) {
            return interval;
        }
    }

    /**
     * @param interval The amount of time in milliseconds to update the fps
     *                 text.
     *
     * @return this instance
     */
    public FpsActor setInterval(final long interval) {
        final long old;
        synchronized (this) {
            old = this.interval;
            this.interval = interval;
        }
        getPropertyChangeSupport().firePropertyChange(INTERVAL, old, interval);
        return this;
    }

    /**
     * @return the average frames per second (FPS)
     */
    public long getAverageFps() {
        return averageFps.get();
    }

    @Override
    public void update(final long delta) {
        elapsed += delta;
        // only update the text every second or more
        if (elapsed >= getInterval()) {
            final long fps = (long) (counter.getAndSet(0) / (elapsed / 1000.0));
            averageFps.set(fps);
            elapsed = 0L;
            // set the new text
            final Shape s = getShape();
            final Font font = s instanceof Text2D ? ((Text2D) s).getFont() : null;
            if (font != null) {
                setShape(new Text2D(font, String.format("FPS: %04d", fps)));
            } else {
                setShape(new Text2D(String.format("FPS: %04d", fps)));
            }
        }
    }

    @Override
    public int getPriority() {
        return service.getPriority();
    }

    @Override
    public int compareTo(final DrawableService service) {
        return this.service.compareTo(service);
    }

    private static class DrawableServiceImpl extends AbstractDrawableService {

        private DrawableServiceImpl(final Scene scene, final int priority) {
            super(scene, priority);
        }

        @Override
        public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
            // no operation
        }

        @Override
        public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
            // no operation
        }
    }

    public static class Builder {

        // actor
        private Scene scene = null;
        private Camera camera = null;
        private String id = null;
        private DrawingPass drawingPass = DrawingPass.SCENE;
        private byte passNumber = 0;
        private float thickness = 1.0f;
        private String type = null;
        private boolean dirty = true;
        private boolean visible = true;
        private boolean wire = false;
        // shape actor
        private Shape shape = null;
        private Alignment origin = Alignment.CENTER;
        private Color color = null;
        private Color backgroundColor = null;
        // bordered shape actor
        private Border border = Border.ALL;
        private Color borderColor = null;
        private double borderThickness = 1.0;
        // fps actor
        private long interval = 1000L;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public Builder scene(final Scene scene) {
            this.scene = scene;
            return this;
        }

        public Builder camera(final Camera camera) {
            this.camera = camera;
            return this;
        }

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder drawingPass(final DrawingPass drawingPass) {
            this.drawingPass = drawingPass;
            return this;
        }

        public Builder passNumber(final byte passNumber) {
            this.passNumber = passNumber;
            return this;
        }

        public Builder thickness(final float thickness) {
            this.thickness = thickness;
            return this;
        }

        public Builder type(final String type) {
            this.type = type;
            return this;
        }

        public Builder dirty(final boolean dirty) {
            this.dirty = dirty;
            return this;
        }

        public Builder visible(final boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder wire(final boolean wire) {
            this.wire = wire;
            return this;
        }
        // shape actor

        public Builder shape(final Shape shape) {
            this.shape = shape;
            return this;
        }

        public Builder origin(final Alignment origin) {
            this.origin = origin;
            return this;
        }

        public Builder color(final Color color) {
            this.color = color;
            return this;
        }

        public Builder backgroundColor(final Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }
        // bordered shape actor

        public Builder border(final Border border) {
            this.border = border;
            return this;
        }

        public Builder borderColor(final Color borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder borderThickness(final double borderThickness) {
            this.borderThickness = borderThickness;
            return this;
        }

        // fps actor
        public Builder interval(final long interval) {
            this.interval = interval;
            return this;
        }

        public FpsActor build() {
            final FpsActor actor;
            if (type != null && id != null) {
                actor = new FpsActor(scene, type, id);
            } else if (id != null) {
                actor = new FpsActor(scene, id);
            } else {
                actor = new FpsActor(scene);
            }
            actor.setInterval(interval)
                    .setBorder(border)
                    .setBorderColor(borderColor)
                    .setBorderThickness(borderThickness)
                    .setBackgroundColor(backgroundColor)
                    .setColor(color)
                    .setOrigin(origin)
                    .setShape(shape)
                    .setCamera(camera)
                    .setDirty(dirty)
                    .setDrawingPass(drawingPass)
                    .setPassNumber(passNumber)
                    .setThickness(thickness)
                    .setVisible(visible)
                    .setWire(wire);
            if (type != null) {
                actor.setType(type);
            }
            return actor;
        }
    }
}
