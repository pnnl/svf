package gov.pnnl.svf.actor;

import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.core.lookup.LookupProvider;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Volume3D;
import gov.pnnl.svf.scene.Disposable;
import gov.pnnl.svf.scene.Drawable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Observable;
import gov.pnnl.svf.scene.Scene;

/**
 * Interface that represents an object that can be drawn in a scene.
 *
 * @author Arthur Bleeker
 *
 */
public interface Actor extends LookupProvider, Drawable, Disposable, Observable {

    /**
     * String representation of a field in this object. A lookup changed event
     * is fired when the lookup is changed because of an add or remove
     * operation. The object will be the old value if an object was removed and
     * will be the new value when an object is added.
     */
    String LOOKUP = "lookup";
    /**
     * String representation of a field in this object.
     */
    String TYPE = "type";
    /**
     * String representation of a field in this object.
     */
    String DRAWING_PASS = "drawingPass";
    /**
     * String representation of a field in this object.
     */
    String VISIBLE = "visible";
    /**
     * String representation of a field in this object.
     */
    String DIRTY = "dirty";
    /**
     * String representation of a field in this object.
     */
    String WIRE = "wire";
    /**
     * String representation of a field in this object.
     */
    String THICKNESS = "thickness";
    /**
     * String representation of a field in this object.
     */
    String CAMERA = "camera";
    /**
     * String representation of a field in this object.
     */
    String PASS_NUMBER = "passNumber";
    /**
     * String representation of a field in this object.
     */
    String ROOT = "root";

    /**
     * Dispose of this actor. This method will clean up the actor by removing
     * all support objects and removing all registered property change
     * listeners. This method should be called after the actor has been removed
     * from the scene to prevent items from getting reinitialized after they are
     * uninitialized.
     */
    @Override
    void dispose();

    /**
     * @return the id
     */
    String getId();

    /**
     * @return the pass number that this actor will draw in
     */
    byte getPassNumber();

    /**
     * @return the thickness of the lines for this actor
     */
    float getThickness();

    /**
     * @return the type
     */
    String getType();

    /**
     * @return true if this actor needs to be drawn
     */
    boolean isDirty();

    /**
     * Root actors are actors with no parent. Root actors may or may not have
     * children.
     *
     * @return true if this actor is a root actor in the scene
     */
    boolean isRoot();

    /**
     * @return true if this actor should be drawn as a wire frame
     */
    boolean isWire();

    /**
     * Check whether this actor is visible to the specified camera.
     *
     * @param camera the camera to check
     *
     * @return true if visible
     */
    boolean isCamera(final Camera camera);

    /**
     * A camera that this actor is visible in. This is useful for GUI
     * components.
     *
     * @param camera a scene camera
     *
     * @return this instance
     */
    Actor addCamera(final Camera camera);

    /**
     * A camera that this actor is not visible in. This is useful for GUI
     * components.
     *
     * @param camera a scene camera
     *
     * @return this instance
     */
    Actor removeCamera(final Camera camera);

    /**
     * Make this actor visible to all cameras.
     *
     * @return this instance
     */
    Actor clearCameras();

    /**
     * @param dirty true if this actor needs to be redrawn
     *
     * @return this instance
     */
    Actor setDirty(boolean dirty);

    /**
     * @param drawingPass the drawing pass for this actor
     *
     * @return this instance
     */
    Actor setDrawingPass(DrawingPass drawingPass);

    /**
     * @param passNumber The pass number that this actor will draw in
     *
     * @return this instance
     */
    Actor setPassNumber(byte passNumber);

    /**
     * Convenience setter for setPassNumber(byte). The pass number must be
     * between 0 and 127 inclusive.
     *
     * @param passNumber The pass number that this actor will draw in
     *
     * @return this instance
     */
    Actor setPassNumber(int passNumber);

    /**
     * @param thickness the thickness of the lines for this actor
     *
     * @return this instance
     */
    Actor setThickness(float thickness);

    /**
     * @param type the type to set
     *
     * @return this instance
     */
    Actor setType(String type);

    /**
     * @param visible Set to true to enable drawing of this actor.
     *
     * @return this instance
     */
    Actor setVisible(boolean visible);

    /**
     * @param wire Set to true to draw this actor as a wire frame.
     *
     * @return this instance
     */
    Actor setWire(boolean wire);

    public static class Builder implements BuilderActor, BuilderShape {

        /**
         * type of actor being built
         */
        private static enum ActorType {

            INVISIBLE,
            NODE,
            SHAPE,
            BORDERED_SHAPE,
            VOLUME;
        }
        private ActorType actorType = ActorType.SHAPE;
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

        @Override
        public Builder scene(final Scene scene) {
            this.scene = scene;
            return this;
        }

        @Override
        public Builder camera(final Camera camera) {
            this.camera = camera;
            return this;
        }

        @Override
        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder drawingPass(final DrawingPass drawingPass) {
            this.drawingPass = drawingPass;
            return this;
        }

        @Override
        public Builder passNumber(final byte passNumber) {
            this.passNumber = passNumber;
            return this;
        }

        @Override
        public Builder thickness(final float thickness) {
            this.thickness = thickness;
            return this;
        }

        @Override
        public Builder type(final String type) {
            this.type = type;
            return this;
        }

        @Override
        public Builder dirty(final boolean dirty) {
            this.dirty = dirty;
            return this;
        }

        @Override
        public Builder visible(final boolean visible) {
            this.visible = visible;
            return this;
        }

        @Override
        public Builder wire(final boolean wire) {
            this.wire = wire;
            return this;
        }

        // invisible actor
        public BuilderActor invisible() {
            this.actorType = ActorType.INVISIBLE;
            return this;
        }

        // node actor
        public BuilderActor node() {
            this.actorType = ActorType.NODE;
            return this;
        }
        // volume actor

        public BuilderShape volume(final Volume3D shape) {
            this.actorType = ActorType.VOLUME;
            this.shape = shape;
            return this;
        }
        // shape actor

        @Override
        public BuilderShape shape(final Shape shape) {
            if (shape instanceof Volume3D) {
                this.actorType = ActorType.VOLUME;
            }
            this.shape = shape;
            return this;
        }

        @Override
        public BuilderShape origin(final Alignment origin) {
            this.origin = origin;
            return this;
        }

        @Override
        public BuilderShape color(final Color color) {
            this.color = color;
            return this;
        }

        @Override
        public BuilderShape backgroundColor(final Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }
        // bordered shape actor

        @Override
        public BuilderShape border(final Border border) {
            this.actorType = ActorType.BORDERED_SHAPE;
            this.border = border;
            return this;
        }

        @Override
        public BuilderShape borderColor(final Color borderColor) {
            this.actorType = ActorType.BORDERED_SHAPE;
            this.borderColor = borderColor;
            return this;
        }

        @Override
        public BuilderShape borderThickness(final double borderThickness) {
            this.actorType = ActorType.BORDERED_SHAPE;
            this.borderThickness = borderThickness;
            return this;
        }

        @Override
        public ActorExt build() {
            final ActorExt actor;
            switch (actorType) {
                case INVISIBLE: {
                    if (type != null && id != null) {
                        actor = new InvisibleActor(scene, type, id);
                    } else if (id != null) {
                        actor = new InvisibleActor(scene, id);
                    } else {
                        actor = new InvisibleActor(scene);
                    }
                    break;
                }
                case NODE: {
                    if (type != null && id != null) {
                        actor = new NodeActor(scene, type, id);
                    } else if (id != null) {
                        actor = new NodeActor(scene, id);
                    } else {
                        actor = new NodeActor(scene);
                    }
                    break;
                }
                case SHAPE: {
                    final DynamicShapeActor temp;
                    if (type != null && id != null) {
                        temp = new DynamicShapeActor(scene, type, id);
                    } else if (id != null) {
                        temp = new DynamicShapeActor(scene, id);
                    } else {
                        temp = new DynamicShapeActor(scene);
                    }
                    temp.setBackgroundColor(backgroundColor)
                            .setColor(color)
                            .setOrigin(origin)
                            .setShape(shape);
                    actor = temp;
                    break;
                }
                case VOLUME: {
                    final VolumeActor temp;
                    if (type != null && id != null) {
                        temp = new VolumeActor(scene, type, id);
                    } else if (id != null) {
                        temp = new VolumeActor(scene, id);
                    } else {
                        temp = new VolumeActor(scene);
                    }
                    temp.setBackgroundColor(backgroundColor)
                            .setColor(color)
                            .setOrigin(origin)
                            .setShape(shape);
                    actor = temp;
                    break;
                }
                case BORDERED_SHAPE: {
                    final DynamicBorderedShapeActor temp;
                    if (type != null && id != null) {
                        temp = new DynamicBorderedShapeActor(scene, type, id);
                    } else if (id != null) {
                        temp = new DynamicBorderedShapeActor(scene, id);
                    } else {
                        temp = new DynamicBorderedShapeActor(scene);
                    }
                    temp.setBorder(border)
                            .setBorderColor(borderColor)
                            .setBorderThickness(borderThickness)
                            .setBackgroundColor(backgroundColor)
                            .setColor(color)
                            .setOrigin(origin)
                            .setShape(shape);
                    actor = temp;
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown actor type passed to control statement: " + actorType);
            }
            actor.setDirty(dirty)
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

    public static interface BuilderActor {

        BuilderActor scene(Scene scene);

        BuilderActor camera(Camera camera);

        BuilderActor id(String id);

        BuilderActor drawingPass(DrawingPass drawingPass);

        BuilderActor passNumber(byte passNumber);

        BuilderActor thickness(float thickness);

        BuilderActor type(String type);

        BuilderActor dirty(boolean dirty);

        BuilderActor visible(boolean visible);

        BuilderActor wire(boolean wire);

        ActorExt build();
    }

    public static interface BuilderShape {

        BuilderShape scene(Scene scene);

        BuilderShape camera(Camera camera);

        BuilderShape id(String id);

        BuilderShape drawingPass(DrawingPass drawingPass);

        BuilderShape passNumber(byte passNumber);

        BuilderShape thickness(float thickness);

        BuilderShape type(String type);

        BuilderShape dirty(boolean dirty);

        BuilderShape visible(boolean visible);

        BuilderShape wire(boolean wire);

        BuilderShape shape(Shape shape);

        BuilderShape origin(Alignment origin);

        BuilderShape color(Color color);

        BuilderShape backgroundColor(Color backgroundColor);

        BuilderShape border(Border border);

        BuilderShape borderColor(Color borderColor);

        BuilderShape borderThickness(double borderThickness);

        ActorExt build();
    }
}
