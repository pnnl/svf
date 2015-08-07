package gov.pnnl.svf.actor;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;

/**
 * This is an actor that does not draw anything in the scene. It can still
 * contain support objects. Typical use is to have children that inherit from
 * it's transform. This is analogous to a node in other game style scene graphs.
 *
 * @author Arthur Bleeker
 */
public class NodeActor extends AbstractActor {

    /**
     * The default type for this actor.
     */
    public static final String DEFAULT_TYPE = "node";

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     */
    public NodeActor(final Scene scene) {
        super(scene, DEFAULT_TYPE, scene.getFactory().newUuid(scene));
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     * @param id    Unique ID for this actor
     */
    public NodeActor(final Scene scene, final String id) {
        super(scene, DEFAULT_TYPE, id);
    }

    /**
     * Constructor
     *
     * @param scene Reference to the scene for lookup.
     * @param type  The type of this actor.
     * @param id    The unique id for this actor.
     */
    public NodeActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
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

        public NodeActor build() {
            final NodeActor actor;
            if (type != null && id != null) {
                actor = new NodeActor(scene, type, id);
            } else if (id != null) {
                actor = new NodeActor(scene, id);
            } else {
                actor = new NodeActor(scene);
            }
            actor.setCamera(camera)
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
