package gov.pnnl.svf.support;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Support for objects that can be transformed in a GLScene. This transform
 * subtype allows for the separation of orbit from orbit.
 *
 * @author Arthur Bleeker
 *
 */
public class OrbitTransformSupport extends TransformSupport {

    /**
     * String representation of a field in this object.
     */
    public static final String ORBIT_AXIS = "orbitAxis";
    /**
     * String representation of a field in this object.
     */
    public static final String ORBIT = "orbit";
    private Vector3D orbitAxis = Vector3D.ZERO;
    private double orbit = 0.0;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The actor that owns this transform support object.
     */
    protected OrbitTransformSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Constructor
     *
     * @param actor The actor that owns this transform support object.
     *
     * @return a reference to the support object
     */
    public static OrbitTransformSupport newInstance(final Actor actor) {
        final OrbitTransformSupport instance = new OrbitTransformSupport(actor);
        actor.add(instance);
        return instance;
    }

    /**
     * @return the orbit
     */
    public double getOrbit() {
        synchronized (this) {
            return orbit;
        }
    }

    /**
     * @return the orbitAxis
     */
    public Vector3D getOrbitAxis() {
        synchronized (this) {
            return orbitAxis;
        }
    }

    @Override
    public void pushTransform(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        gl.glPushMatrix();
        drawState.setMatrixModelview();
        synchronized (this) {
            gl.glRotated(orbit, orbitAxis.getX(), orbitAxis.getY(), orbitAxis.getZ());
            gl.glTranslated(translation.getX(), translation.getY(), translation.getZ());
            gl.glRotated(rotation, rotationAxis.getX(), rotationAxis.getY(), rotationAxis.getZ());
            gl.glScaled(scale.getX(), scale.getY(), scale.getZ());
        }
    }

    @Override
    public void popTransform(final GL2 gl, final GLUgl2 glu) {
        if (drawState.isMatrixModelview()) {
            gl.glPopMatrix();
        }
    }

    @Override
    public void pushTransformInt(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        gl.glPushMatrix();
        drawState.setMatrixModelview();
        synchronized (this) {
            gl.glRotated(Math.round(orbit), orbitAxis.getX(), orbitAxis.getY(), orbitAxis.getZ());
            gl.glTranslated(Math.round(translation.getX()), Math.round(translation.getY()), Math.round(translation.getZ()));
            gl.glRotated(Math.round(rotation), rotationAxis.getX(), rotationAxis.getY(), rotationAxis.getZ());
            gl.glScaled(Math.round(scale.getX()), Math.round(scale.getY()), Math.round(scale.getZ()));
        }
    }

    @Override
    public void popTransformInt(final GL2 gl, final GLUgl2 glu) {
        if (drawState.isMatrixModelview()) {
            gl.glPopMatrix();
        }
    }

    /**
     * @param orbit the orbit to set
     *
     * @return a reference to the support object
     */
    public OrbitTransformSupport setOrbit(double orbit) {
        // validate method parameter
        while (orbit < 0.0) {
            orbit += 360.0;
        }
        while (orbit >= 360.0) {
            orbit -= 360.0;
        }
        final double old;
        synchronized (this) {
            old = this.orbit;
            this.orbit = orbit;
        }
        getPropertyChangeSupport().firePropertyChange(ORBIT, old, orbit);
        return this;
    }

    /**
     * @param orbitAxis the orbitAxis to set
     *
     * @return a reference to the support object
     */
    public OrbitTransformSupport setOrbitAxis(Vector3D orbitAxis) {
        if (orbitAxis == null) {
            throw new NullPointerException("orbitAxis");
        }
        // validate method parameter
        if (Double.compare(orbitAxis.getNorm1(), 0.0) != 0) {
            orbitAxis = orbitAxis.normalize();
        }
        final Vector3D old;
        synchronized (this) {
            old = this.orbitAxis;
            this.orbitAxis = orbitAxis;
        }
        getPropertyChangeSupport().firePropertyChange(ORBIT_AXIS, old, orbitAxis);
        return this;
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "OrbitTransformSupport{" + "translation=" + translation + "rotationAxis=" + rotationAxis + "rotation=" + rotation + "orbitAxis=" + orbitAxis + "orbit=" + orbit + "scale=" + scale + '}';
        }
    }
}
