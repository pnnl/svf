package gov.pnnl.svf.support;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.Drawable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Support for adding clipping planes to an actor. Clipping planes are used to
 * prevent sections of an actor from drawing.
 *
 * @author Arthur Bleeker
 */
public class ClippingPlaneSupport extends AbstractSupport<Object> implements Drawable {

    private static final Logger logger = Logger.getLogger(ClippingPlaneSupport.class.getName());
    private static final int MAX_CLIPPING_PLANES = 6;
    private final double[][] planes = new double[MAX_CLIPPING_PLANES][4];
    private int size = 0;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The owning actor.
     */
    protected ClippingPlaneSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor The actor that owns this support instance.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static ClippingPlaneSupport newInstance(final Actor actor) {
        final ClippingPlaneSupport instance = new ClippingPlaneSupport(actor);
        actor.add(instance);
        return instance;
    }

    @Override
    public Scene getScene() {
        return getActor().getScene();
    }

    @Override
    public boolean isVisible() {
        return getActor().isVisible();
    }

    @Override
    public DrawingPass getDrawingPass() {
        return getActor().getDrawingPass();
    }

    /**
     * Add a new clipping plane to this support object. You can have a maximum
     * of 6 clipping planes. Adding additional clipping planes beyond 6 will
     * throw an {@link IllegalArgumentException}. Parameters are for the plane
     * equation Ax + By + Cz + D = 0
     *
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public void addClippingPlane(final double a, final double b, final double c, final double d) {
        synchronized (this) {
            if (size == MAX_CLIPPING_PLANES) {
                throw new IllegalArgumentException("plane");
            }
            if ((Double.compare(0.0, a) == 0) && (Double.compare(0.0, b) == 0) && (Double.compare(0.0, c) == 0) && (Double.compare(0.0, d) == 0)) {
                throw new IllegalArgumentException("invalid clipping plane equation");
            }
            planes[size] = new double[]{a, b, c, d};
            // logger.log(Level.INFO,
            // "Created clipping plane {0}x + {1}y + {2}z + {3} = 0", new
            // Double[]{planes[size][0], planes[size][1], planes[size][2],
            // planes[size][3]});
            size++;
        }
        getActor().setDirty(true);
    }

    /**
     * Add a new clipping plane to this support object. You can have a maximum
     * of 6 clipping planes. Adding additional clipping planes beyond 6 will
     * throw an {@link IllegalArgumentException}. Parameters are the normal of
     * the plane in the direction of the non-clip geometry and the scalar or d
     * part of the equation.
     *
     * @param normal the normal vector
     * @param d      scalar
     */
    public void addClippingPlane(final Vector3D normal, final double d) {
        if (normal == null) {
            throw new NullPointerException("normal");
        }
        final Vector3D normalized = normal.normalize();
        addClippingPlane(normalized.getX(), normalized.getY(), normalized.getZ(), d);
    }

    /**
     * Add a new clipping plane to this support object. You can have a maximum
     * of 6 clipping planes. Adding additional clipping planes beyond 6 will
     * throw an {@link IllegalArgumentException}. Parameters are three points
     * that lie on the plane.
     *
     * @param one   1st point
     * @param two   2nd point
     * @param three 3rd point
     */
    public void addClippingPlane(final Vector3D one, final Vector3D two, final Vector3D three) {
        if (one == null) {
            throw new NullPointerException("one");
        }
        if (two == null) {
            throw new NullPointerException("two");
        }
        if (three == null) {
            throw new NullPointerException("three");
        }
        final double a = one.getY() * (two.getZ() - three.getZ()) + two.getY() * (three.getZ() - one.getZ()) + three.getY() * (one.getZ() - two.getZ());
        final double b = one.getZ() * (two.getX() - three.getX()) + two.getZ() * (three.getX() - one.getX()) + three.getZ() * (one.getX() - two.getX());
        final double c = one.getX() * (two.getY() - three.getY()) + two.getX() * (three.getY() - one.getY()) + three.getX() * (one.getY() - two.getY());
        final double d = -1.0
                         * ((one.getX() * (two.getY()) * three.getZ() - three.getY() * two.getZ())
                            + (two.getX() * (three.getY() * one.getZ() - one.getY() * three.getZ())) + (three.getX() * (one.getY() * two.getZ() - two.getY()
                                                                                                                                                  * one.getZ())));
        addClippingPlane(a, b, c, d);
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    /**
     * Clears all of the clipping planes.
     */
    public void clearClippingPlanes() {
        synchronized (this) {
            size = 0;
        }
        getActor().setDirty(true);
    }

    @Override
    @SuppressWarnings("fallthrough")
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        gl.glPushAttrib(GL2.GL_ENABLE_BIT);
        drawState.setAttrib();
        synchronized (this) {
            switch (size) {
                case 6:
                    gl.glClipPlane(GL2ES1.GL_CLIP_PLANE5, planes[5], 0);
                    gl.glEnable(GL2ES1.GL_CLIP_PLANE5);
                // cascading switch case should fall through to the next case
                case 5:
                    gl.glClipPlane(GL2ES1.GL_CLIP_PLANE4, planes[4], 0);
                    gl.glEnable(GL2ES1.GL_CLIP_PLANE4);
                // cascading switch case should fall through to the next case
                case 4:
                    gl.glClipPlane(GL2ES1.GL_CLIP_PLANE3, planes[3], 0);
                    gl.glEnable(GL2ES1.GL_CLIP_PLANE3);
                // cascading switch case should fall through to the next case
                case 3:
                    gl.glClipPlane(GL2ES1.GL_CLIP_PLANE2, planes[2], 0);
                    gl.glEnable(GL2ES1.GL_CLIP_PLANE2);
                // cascading switch case should fall through to the next case
                case 2:
                    gl.glClipPlane(GL2ES1.GL_CLIP_PLANE1, planes[1], 0);
                    gl.glEnable(GL2ES1.GL_CLIP_PLANE1);
                // cascading switch case should fall through to the next case
                case 1:
                    gl.glClipPlane(GL2ES1.GL_CLIP_PLANE0, planes[0], 0);
                    gl.glEnable(GL2ES1.GL_CLIP_PLANE0);
                // cascading switch case should fall through to the next case
                case 0:
                    // don't enable
                    break;
                default:
                    if (getActor().getScene().getExtended().getSceneBuilder().isVerbose()) {
                        logger.log(Level.WARNING, "{0}: Unhandled case in switch statement: {1}", new Object[]{getScene(), size});
                    }
            }
        }
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (drawState.isAttrib()) {
            gl.glPopAttrib();
        }
        drawState.clearValues();
    }

    /**
     * Get a copy of a clipping plane equation.
     *
     * @param index the index of the clipping plane
     *
     * @return a copy of the clipping plane
     */
    public double[] getClippingPlane(final int index) {
        synchronized (this) {
            return Arrays.copyOf(planes[index], 4);
        }
    }

    /**
     * The number of active clipping planes.
     *
     * @return the number of clipping planes
     */
    public int getClippingPlanesSize() {
        synchronized (this) {
            return size;
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "ClippingPlaneSupport{" + "planes=" + Arrays.deepToString(planes) + '}';
        }
    }
}
