package gov.pnnl.svf.actor;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Shape;
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
 * Draws a geometry shape with a border.
 *
 * @author Amelia Bleeker
 *
 */
public class DynamicBorderedShapeActor extends DynamicShapeActor {

    /**
     * The default type for this actor.
     */
    public static final String DEFAULT_TYPE = "bordered-shape";
    /**
     * String representation of a field in this object.
     */
    public static final String BORDER = "border";
    /**
     * String representation of a field in this object.
     */
    public static final String BORDER_COLOR = "borderColor";
    /**
     * String representation of a field in this object.
     */
    public static final String BORDER_THICKNESS = "borderThickness";
    private Border border = Border.ALL;
    private Color borderColor;
    private double borderThickness = 1.0;

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     */
    public DynamicBorderedShapeActor(final Scene scene) {
        super(scene, DEFAULT_TYPE, scene.getFactory().newUuid(scene));
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     * @param id    Unique ID for this actor
     */
    public DynamicBorderedShapeActor(final Scene scene, final String id) {
        super(scene, DEFAULT_TYPE, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     * @param type  Type for this actor
     * @param id    Unique ID for this actor
     */
    public DynamicBorderedShapeActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    protected Set<String> getInitializeFields() {
        final Set<String> fields = super.getInitializeFields();
        fields.add(BORDER);
        fields.add(BORDER_COLOR);
        fields.add(BORDER_THICKNESS);
        return fields;
    }

    /**
     * @return the border
     */
    public Border getBorder() {
        synchronized (this) {
            return border;
        }
    }

    /**
     * @param border the border to set
     *
     * @return this instance
     */
    public DynamicBorderedShapeActor setBorder(final Border border) {
        if (border == null) {
            throw new NullPointerException("border");
        }
        final Border oldBorder;
        synchronized (this) {
            oldBorder = this.border;
            this.border = border;
        }
        getPropertyChangeSupport().firePropertyChange(BORDER, oldBorder, border);
        return this;
    }

    /**
     * If no border color is specified then the primary color will be used.
     *
     * @return the border color
     */
    public Color getBorderColor() {
        synchronized (this) {
            return borderColor;
        }
    }

    /**
     * If no border color is specified then the primary color will be used.
     *
     * @param borderColor the border color
     *
     * @return this instance
     */
    public DynamicBorderedShapeActor setBorderColor(final Color borderColor) {
        final Color oldBorderColor;
        synchronized (this) {
            oldBorderColor = this.borderColor;
            this.borderColor = borderColor;
        }
        getPropertyChangeSupport().firePropertyChange(BORDER_COLOR, oldBorderColor, borderColor);
        return this;
    }

    /**
     * @return the border thickness
     */
    public double getBorderThickness() {
        synchronized (this) {
            return borderThickness;
        }
    }

    /**
     * @param borderThickness the border thickness
     *
     * @return this instance
     */
    public DynamicBorderedShapeActor setBorderThickness(final double borderThickness) {
        if (borderThickness < 0.0) {
            throw new IllegalArgumentException("borderThickness");
        }
        final double oldBorderThickness;
        synchronized (this) {
            oldBorderThickness = this.borderThickness;
            this.borderThickness = borderThickness;
        }
        getPropertyChangeSupport().firePropertyChange(BORDER_THICKNESS, oldBorderThickness, borderThickness);
        return this;
    }

    @Override
    public List<VertexBufferObject> createVbos() {
        // gather variables
        final Shape shape;
        final Alignment origin;
        final Color color;
        final Color backgroundColor;
        final Border border;
        final Color borderColor;
        final double borderThickness;
        synchronized (this) {
            shape = this.shape;
            origin = this.origin;
            color = this.color;
            backgroundColor = this.backgroundColor;
            border = this.border;
            borderColor = this.borderColor;
            borderThickness = this.borderThickness;
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
            vbos.addAll(vboShapeFactory.createBorderVbos(shape, border, borderThickness, borderColor));
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
        final Border border;
        final double borderThickness;
        synchronized (this) {
            shape = this.shape;
            origin = this.origin;
            border = this.border;
            borderThickness = this.borderThickness;
        }
        // find text offset from origin
        final Point2D offset = shape != null ? LayoutUtil.findOrigin(shape, origin) : Point2D.ZERO;
        final VboShapeService vboShapeService = getScene().lookup(VboShapeService.class);
        final VboShapeFactory vboShapeFactory = shape == null ? null : vboShapeService.getVboShapeFactory(shape.getClass());
        // build vbos
        if (vboShapeFactory != null && shape != null) {
            final List<VertexBufferObject> vbos = new ArrayList<>();
            vbos.addAll(vboShapeFactory.createPickingVbos(shape));
            vbos.addAll(vboShapeFactory.createBorderVbos(shape, border, borderThickness, null));
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
        final Border border;
        final double borderThickness;
        synchronized (this) {
            shape = this.shape;
            origin = this.origin;
            border = this.border;
            borderThickness = this.borderThickness;
        }
        // find text offset from origin
        final Point2D offset = shape != null ? LayoutUtil.findOrigin(shape, origin) : Point2D.ZERO;
        final VboShapeService vboShapeService = getScene().lookup(VboShapeService.class);
        final VboShapeFactory vboShapeFactory = shape == null ? null : vboShapeService.getVboShapeFactory(shape.getClass());
        // build vbos
        if (vboShapeFactory != null && shape != null) {
            final List<VertexBufferObject> vbos = vboShapeFactory.createColorPickingVbos(shape, border, borderThickness, support);
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
        final Border border;
        final Color borderColor;
        final double borderThickness;
        synchronized (this) {
            shape = this.shape;
            color = this.color;
            backgroundColor = this.backgroundColor;
            border = this.border;
            borderColor = this.borderColor;
            borderThickness = this.borderThickness;
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
            // draw the border
            ShapeUtil.pushColor(gl, borderColor);
            v += shapeRenderer.drawBorder(gl, shape, border, borderThickness);
            ShapeUtil.popColor(gl, borderColor);
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
        final Border border;
        final double borderThickness;
        synchronized (this) {
            shape = this.shape;
            border = this.border;
            borderThickness = this.borderThickness;
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
            int v = shapeRenderer.pickingDrawShape(gl, shape);
            v += shapeRenderer.drawBorder(gl, shape, border, borderThickness);
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
        final Border border;
        final double borderThickness;
        synchronized (this) {
            shape = this.shape;
            border = this.border;
            borderThickness = this.borderThickness;
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
            final int v = shapeRenderer.colorPickingDrawShape(gl, shape, border, borderThickness, support);
            // pop offset
            if (!offset.equals(Point2D.ZERO)) {
                gl.glPopMatrix();
            }
            camera.getExtended().incrementVerticesCounter(v);
        }
    }

    @Override
    public String toString() {
        return "DynamicBorderedShapeActor{" + "id=" + getId() + ",type=" + getType() + ",visible=" + isVisible() + ",shape=" + getShape() + ",border=" + getBorder()
               + '}';
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

        public DynamicBorderedShapeActor build() {
            final DynamicBorderedShapeActor actor;
            if (type != null && id != null) {
                actor = new DynamicBorderedShapeActor(scene, type, id);
            } else if (id != null) {
                actor = new DynamicBorderedShapeActor(scene, id);
            } else {
                actor = new DynamicBorderedShapeActor(scene);
            }
            actor.setBorder(border)
                    .setBorderColor(borderColor)
                    .setBorderThickness(borderThickness)
                    .setBackgroundColor(backgroundColor)
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
