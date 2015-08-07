package gov.pnnl.svf.actor;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.ShapeRenderer;
import gov.pnnl.svf.geometry.ShapeService;
import gov.pnnl.svf.geometry.Volume3D;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.LayoutUtil;
import gov.pnnl.svf.util.ShapeUtil;
import java.util.Collections;
import java.util.Set;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;

/**
 * This actor will render volumetric data by drawing a series of slices that are
 * aligned with a Euler axis. The direction of draw will be determined by the
 * viewing camera.
 *
 * @author Arthur Bleeker
 */
public class VolumeActor extends ShapeActor {

    /**
     * The default type for this actor.
     */
    public static final String DEFAULT_TYPE = "volume";

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     */
    public VolumeActor(final Scene scene) {
        super(scene, DEFAULT_TYPE, scene.getFactory().newUuid(scene));
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     * @param id    Unique ID for this actor
     */
    public VolumeActor(final Scene scene, final String id) {
        super(scene, DEFAULT_TYPE, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public VolumeActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    protected Set<String> getInitializeFields() {
        // volume actor doesn't use display lists
        return Collections.emptySet();
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        int v = 0;
        // gather variables
        final Volume3D shape;
        final Alignment origin;
        final Color color;
        synchronized (this) {
            shape = (Volume3D) this.shape;
            origin = this.origin;
            color = this.color;
        }
        // primary drawing
        if (shape != null) {
            // find offset from origin
            final Point2D offset = LayoutUtil.findOrigin(shape, origin);
            final ShapeService shapeService = getScene().lookup(ShapeService.class);
            final ShapeRenderer shapeRenderer = shapeService.getShapeRenderer(shape.getClass());
            // offset
            if (!offset.equals(Point2D.ZERO)) {
                gl.glPushMatrix();
                gl.glTranslated(offset.getX(), offset.getY(), 0.0);
            }
            // draw the shape
            ShapeUtil.pushColor(gl, color);
            gl.glPushAttrib(GL2.GL_ENABLE_BIT);
            gl.glDisable(GL.GL_CULL_FACE);
            v += shapeRenderer.drawShape(gl, shape);
            gl.glPopAttrib();
            ShapeUtil.popColor(gl, color);
            if (!offset.equals(Point2D.ZERO)) {
                gl.glPopMatrix();
            }
        }
        // return vertices rendered
        vertices = v;
    }

    @Override
    public void pickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event) {
        gl.glPushAttrib(GL2.GL_ENABLE_BIT);
        gl.glDisable(GL.GL_CULL_FACE);
        super.pickingDraw(gl, glu, camera, event);
        gl.glPopAttrib();
    }

    @Override
    public void itemPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event, final Object item) {
        gl.glPushAttrib(GL2.GL_ENABLE_BIT);
        gl.glDisable(GL.GL_CULL_FACE);
        super.itemPickingDraw(gl, glu, camera, event, item);
        gl.glPopAttrib();
    }

    @Override
    public void colorPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final ColorPickingSupport support) {
        gl.glPushAttrib(GL2.GL_ENABLE_BIT);
        gl.glDisable(GL.GL_CULL_FACE);
        super.colorPickingDraw(gl, glu, camera, support);
        gl.glPopAttrib();
    }

    @Override
    public void initializeLists(final GL2 gl, final GLUgl2 glu) {
        count = 0;
        list = INITIALIZED;
    }

    @Override
    public Volume3D getShape() {
        return (Volume3D) super.getShape();
    }

    @Override
    public VolumeActor setShape(final Shape shape) {
        if (shape != null && !(shape instanceof Volume3D)) {
            throw new IllegalArgumentException("shape");
        }
        return (VolumeActor) super.setShape(shape);
    }

    @Override
    public String toString() {
        return "VolumeActor{" + "id=" + getId() + ",type=" + getType() + ",visible=" + isVisible() + ",shape=" + getShape() + '}';
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
        private Volume3D shape = null;
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

        public Builder shape(final Volume3D shape) {
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

        public VolumeActor build() {
            final VolumeActor actor;
            if (type != null && id != null) {
                actor = new VolumeActor(scene, type, id);
            } else if (id != null) {
                actor = new VolumeActor(scene, id);
            } else {
                actor = new VolumeActor(scene);
            }
            actor.setBackgroundColor(backgroundColor)
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
