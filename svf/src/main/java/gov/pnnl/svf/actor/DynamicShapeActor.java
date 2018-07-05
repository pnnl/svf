package gov.pnnl.svf.actor;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.Path;
import gov.pnnl.svf.geometry.Path2D;
import gov.pnnl.svf.geometry.Path3D;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.ShapeRenderer;
import gov.pnnl.svf.geometry.ShapeService;
import gov.pnnl.svf.geometry.Text;
import gov.pnnl.svf.hint.OpenGLHint;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.texture.TextureSupport;
import gov.pnnl.svf.util.LayoutUtil;
import gov.pnnl.svf.util.ShapeUtil;
import gov.pnnl.svf.vbo.VboShapeFactory;
import gov.pnnl.svf.vbo.VboShapeService;
import gov.pnnl.svf.vbo.VertexBufferObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Draws a geometry shape.
 *
 * @author Amelia Bleeker
 *
 */
public class DynamicShapeActor extends AbstractDynamicActor {

    /**
     * The default type for this actor.
     */
    public static final String DEFAULT_TYPE = "shape";
    /**
     * String representation of a field in this object.
     */
    public static final String SHAPE = "shape";
    /**
     * String representation of a field in this object.
     */
    public static final String ORIGIN = "origin";
    /**
     * String representation of a field in this object.
     */
    public static final String COLOR = "color";
    /**
     * String representation of a field in this object.
     */
    public static final String BACKGROUND_COLOR = "backgroundColor";
    protected Shape shape;
    protected Alignment origin = Alignment.CENTER;
    protected Color color;
    protected Color backgroundColor;
    protected Point2D offset;
    protected ShapeRenderer shapeRenderer;

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     */
    public DynamicShapeActor(final Scene scene) {
        super(scene, DEFAULT_TYPE, scene.getFactory().newUuid(scene));
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     * @param id    Unique ID for this actor
     */
    public DynamicShapeActor(final Scene scene, final String id) {
        super(scene, DEFAULT_TYPE, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     * @param type  Type for this actor
     * @param id    Unique ID for this actor
     */
    public DynamicShapeActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    protected Set<String> getInitializeFields() {
        final Set<String> fields = super.getInitializeFields();
        fields.add(SHAPE);
        fields.add(ORIGIN);
        fields.add(COLOR);
        fields.add(BACKGROUND_COLOR);
        return fields;
    }

    @Override
    protected OpenGLHint overrideOpenGLHint(final Set<OpenGLHint> openGLHints, final OpenGLHint openGLHint) {
        final Shape shape = getShape();
        if (shape == null) {
            throw new IllegalStateException("shape");
        }
        // text and NURBS needs to use display lists or immediate mode
        if (shape instanceof Text
            || (shape instanceof Path2D && ((Path2D) shape).getStyle() == Path.NURBS)
            || (shape instanceof Path3D && ((Path3D) shape).getStyle() == Path.NURBS)) {
            switch (openGLHint) {
                case ARRAY_DRAW:
                case VBO_DRAW:
                    if (openGLHints.contains(OpenGLHint.DISPLAY_LISTS)) {
                        return OpenGLHint.DISPLAY_LISTS;
                    }
                    return OpenGLHint.IMMEDIATE_DRAW;
                default:
                // no need to handle all cases
            }
        }
        return openGLHint;
    }

    /**
     * The shape can be null.
     *
     * @return the shape
     */
    public Shape getShape() {
        synchronized (this) {
            return shape;
        }
    }

    /**
     * The shape can be null.
     *
     * @param shape the shape to set
     *
     * @return this instance
     */
    public DynamicShapeActor setShape(final Shape shape) {
        final Shape oldShape;
        synchronized (this) {
            oldShape = this.shape;
            this.shape = shape;
        }
        getPropertyChangeSupport().firePropertyChange(SHAPE, oldShape, shape);
        return this;
    }

    /**
     * This will move the origin away from center towards the alignment
     * specified. Will not be null.
     *
     * @return the origin
     */
    public Alignment getOrigin() {
        synchronized (this) {
            return origin;
        }
    }

    /**
     * This will move the origin away from center towards the alignment
     * specified. Can't be null.
     *
     * @param origin the origin to set
     *
     * @return this instance
     */
    public DynamicShapeActor setOrigin(final Alignment origin) {
        if (origin == null) {
            throw new NullPointerException("origin");
        }
        final Alignment oldOrigin;
        synchronized (this) {
            oldOrigin = this.origin;
            this.origin = origin;
        }
        getPropertyChangeSupport().firePropertyChange(ORIGIN, oldOrigin, origin);
        return this;
    }

    /**
     * This color will override color support and requires reinitialization of
     * the actor when changed. The color can be null.
     *
     *
     * @return the color
     */
    public Color getColor() {
        synchronized (this) {
            return color;
        }
    }

    /**
     * This color will override color support and requires reinitialization of
     * the actor when changed. The color can be null.
     *
     * @param color the color to set
     *
     * @return this instance
     */
    public DynamicShapeActor setColor(final Color color) {
        final Color oldColor;
        synchronized (this) {
            oldColor = this.color;
            this.color = color;
        }
        getPropertyChangeSupport().firePropertyChange(COLOR, oldColor, color);
        return this;
    }

    /**
     * The background color will require reinitialization of the actor when
     * changed. This property is not applicable to all shapes. The background
     * color can be null.
     *
     * @return the backgroundColor
     */
    public Color getBackgroundColor() {
        synchronized (this) {
            return backgroundColor;
        }
    }

    /**
     * The background color will require reinitialization of the actor when
     * changed. This property is not applicable to all shapes. The background
     * color can be null.
     *
     * @param backgroundColor the backgroundColor to set
     *
     * @return this instance
     */
    public DynamicShapeActor setBackgroundColor(final Color backgroundColor) {
        final Color oldBackgroundColor;
        synchronized (this) {
            oldBackgroundColor = this.backgroundColor;
            this.backgroundColor = backgroundColor;
        }
        getPropertyChangeSupport().firePropertyChange(BACKGROUND_COLOR, oldBackgroundColor, backgroundColor);
        return this;
    }

    @Override
    public void prepare(final GL2 gl, final GLUgl2 glu) {
        // gather variables
        final Shape shape;
        final Alignment origin;
        synchronized (this) {
            origin = this.origin;
            shape = this.shape;
        }
        // find text offset from origin
        offset = shape != null ? LayoutUtil.findOrigin(shape, origin) : Point2D.ZERO;
        final ShapeService shapeService = getScene().lookup(ShapeService.class);
        shapeRenderer = shape == null ? null : shapeService.getShapeRenderer(shape.getClass());
        // prepare
        if (shapeRenderer != null && shape != null) {
            shapeRenderer.prepare(gl, shape);
        }
    }

    @Override
    public List<VertexBufferObject> createVbos() {
        // gather variables
        final Shape shape;
        final Alignment origin;
        final Color color;
        final Color backgroundColor;
        synchronized (this) {
            shape = this.shape;
            origin = this.origin;
            color = this.color;
            backgroundColor = this.backgroundColor;
        }
        // find text offset from origin
        final Point2D offset = shape != null ? LayoutUtil.findOrigin(shape, origin) : Point2D.ZERO;
        final VboShapeService vboShapeService = getScene().lookup(VboShapeService.class);
        final VboShapeFactory vboShapeFactory = shape == null ? null : vboShapeService.getVboShapeFactory(shape.getClass());
        final boolean texCoords = lookup(TextureSupport.class) != null;
        // build vbos
        if (vboShapeFactory != null && shape != null) {
            final List<VertexBufferObject> vbos = new ArrayList<>();
            if (backgroundColor != null) {
                vbos.addAll(vboShapeFactory.createBackgroundVbos(shape, backgroundColor));
            }
            vbos.addAll(vboShapeFactory.createShapeVbos(shape, color, texCoords));
            // add the offset
            for (int i = 0; i < vbos.size(); i++) {
                final VertexBufferObject vbo = vbos.get(i);
                for (int j = 0; j < vbo.getSize(); j++) {
                    vbo.getVertices()[j * vbo.getVertexDimension()] += offset.getX();
                    vbo.getVertices()[j * vbo.getVertexDimension() + 1] += offset.getY();
                }
            }
            return vbos;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<VertexBufferObject> createPickingVbos() {
        // gather variables
        final Shape shape;
        final Alignment origin;
        synchronized (this) {
            shape = this.shape;
            origin = this.origin;
        }
        // find text offset from origin
        final Point2D offset = shape != null ? LayoutUtil.findOrigin(shape, origin) : Point2D.ZERO;
        final VboShapeService vboShapeService = getScene().lookup(VboShapeService.class);
        final VboShapeFactory vboShapeFactory = shape == null ? null : vboShapeService.getVboShapeFactory(shape.getClass());
        // build vbos
        if (vboShapeFactory != null && shape != null) {
            final List<VertexBufferObject> vbos = vboShapeFactory.createPickingVbos(shape);
            // add the offset
            for (int i = 0; i < vbos.size(); i++) {
                final VertexBufferObject vbo = vbos.get(i);
                for (int j = 0; j < vbo.getSize(); j++) {
                    vbo.getVertices()[j * vbo.getVertexDimension()] += offset.getX();
                    vbo.getVertices()[j * vbo.getVertexDimension() + 1] += offset.getY();
                }
            }
            return vbos;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<VertexBufferObject> createColorPickingVbos(final ColorPickingSupport support) {
        // gather variables
        final Shape shape;
        final Alignment origin;
        synchronized (this) {
            shape = this.shape;
            origin = this.origin;
        }
        // find text offset from origin
        final Point2D offset = shape != null ? LayoutUtil.findOrigin(shape, origin) : Point2D.ZERO;
        final VboShapeService vboShapeService = getScene().lookup(VboShapeService.class);
        final VboShapeFactory vboShapeFactory = shape == null ? null : vboShapeService.getVboShapeFactory(shape.getClass());
        // build vbos
        if (vboShapeFactory != null && shape != null) {
            final List<VertexBufferObject> vbos = vboShapeFactory.createColorPickingVbos(shape, support);
            // add the offset
            for (int i = 0; i < vbos.size(); i++) {
                final VertexBufferObject vbo = vbos.get(i);
                for (int j = 0; j < vbo.getSize(); j++) {
                    vbo.getVertices()[j * vbo.getVertexDimension()] += offset.getX();
                    vbo.getVertices()[j * vbo.getVertexDimension() + 1] += offset.getY();
                }
            }
            return vbos;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void drawImmediate(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // gather variables
        final Shape shape;
        final Color color;
        final Color backgroundColor;
        synchronized (this) {
            shape = this.shape;
            color = this.color;
            backgroundColor = this.backgroundColor;
        }
        if (shape != null) {
            // not ready to draw yet
            if (offset == null || shapeRenderer == null) {
                setDirty(true);
                return;
            }
            int v = 0;
            // push offset
            if (!offset.equals(Point2D.ZERO)) {
                gl.glPushMatrix();
                gl.glTranslated(offset.getX(), offset.getY(), 0.0);
            }
            // draw the background
            if (backgroundColor != null) {
                ShapeUtil.pushColor(gl, backgroundColor);
                v += shapeRenderer.drawBackground(gl, shape);
                ShapeUtil.popColor(gl, backgroundColor);
            }
            // draw the shape
            ShapeUtil.pushColor(gl, color);
            v += shapeRenderer.drawShape(gl, shape);
            ShapeUtil.popColor(gl, color);
            // pop offset
            if (!offset.equals(Point2D.ZERO)) {
                gl.glPopMatrix();
            }
            camera.getExtended().incrementVerticesCounter(v);
        }
    }

    @Override
    public void pickingDrawImmediate(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // gather variables
        final Shape shape;
        synchronized (this) {
            shape = this.shape;
        }
        if (shape != null) {
            // not ready to draw yet
            if (offset == null || shapeRenderer == null) {
                setDirty(true);
                return;
            }
            // push offset
            if (!offset.equals(Point2D.ZERO)) {
                gl.glPushMatrix();
                gl.glTranslated(offset.getX(), offset.getY(), 0.0);
            }
            final int v = shapeRenderer.pickingDrawShape(gl, shape);
            // pop offset
            if (!offset.equals(Point2D.ZERO)) {
                gl.glPopMatrix();
            }
            camera.getExtended().incrementVerticesCounter(v);
        }
    }

    @Override
    public void colorPickingDrawImmediate(final GL2 gl, final GLUgl2 glu, final Camera camera, final ColorPickingSupport support) {
        // gather variables
        final Shape shape;
        synchronized (this) {
            shape = this.shape;
        }
        if (shape != null) {
            // not ready to draw yet
            if (offset == null || shapeRenderer == null) {
                setDirty(true);
                return;
            }
            // push offset
            if (!offset.equals(Point2D.ZERO)) {
                gl.glPushMatrix();
                gl.glTranslated(offset.getX(), offset.getY(), 0.0);
            }
            final int v = shapeRenderer.colorPickingDrawShape(gl, shape, support);
            // pop offset
            if (!offset.equals(Point2D.ZERO)) {
                gl.glPopMatrix();
            }
            camera.getExtended().incrementVerticesCounter(v);
        }
    }

    @Override
    public String toString() {
        return "DynamicShapeActor{" + "id=" + getId() + ",type=" + getType() + ",visible=" + isVisible() + ",shape=" + getShape() + '}';
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

        public DynamicShapeActor build() {
            final DynamicShapeActor actor;
            if (type != null && id != null) {
                actor = new DynamicShapeActor(scene, type, id);
            } else if (id != null) {
                actor = new DynamicShapeActor(scene, id);
            } else {
                actor = new DynamicShapeActor(scene);
            }
            actor.setBackgroundColor(backgroundColor)
                    .setColor(color)
                    .setOrigin(origin)
                    .setShape(shape)
                    .setDirty(dirty)
                    .setDrawingPass(drawingPass)
                    .setPassNumber(passNumber)
                    .setThickness(thickness)
                    .setVisible(visible)
                    .setWire(wire);
            if (camera != null) {
                actor.addCamera(camera);
            }
            if (type != null) {
                actor.setType(type);
            }
            return actor;
        }
    }
}
