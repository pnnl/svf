package gov.pnnl.svf.util;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLUtessellatorCallbackAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of the GLUtessellatorCallback.
 *
 * @author Arthur Bleeker
 */
public class TessellatorCallbackGl extends GLUtessellatorCallbackAdapter {

    private static final Logger logger = Logger.getLogger(TessellatorCallbackGl.class.getName());
    private final GL2 gl;

    /**
     * Constructor
     *
     * @param gl reference to gl
     *
     * @throws NullPointerException
     */
    public TessellatorCallbackGl(final GL2 gl) {
        super();
        if (gl == null) {
            throw new NullPointerException("gl");
        }
        this.gl = gl;
    }

    @Override
    public void begin(final int type) {
        gl.glBegin(type);
    }

    @Override
    public void beginData(final int type, final Object polygonData) {
        begin(type);
    }

    @Override
    public void end() {
        gl.glEnd();
    }

    @Override
    public void endData(final Object polygonData) {
        end();
    }

    @Override
    public void edgeFlag(final boolean boundaryEdge) {
        gl.glEdgeFlag(boundaryEdge);
    }

    @Override
    public void edgeFlagData(final boolean boundaryEdge, final Object polygonData) {
        edgeFlag(boundaryEdge);
    }

    @Override
    public void vertex(final Object vertexData) {
        if (vertexData instanceof double[]) {
            gl.glVertex3dv((double[]) vertexData, 0);
        }
    }

    @Override
    public void vertexData(final Object vertexData, final Object polygonData) {
        vertex(vertexData);
    }

    /**
     * combineCallback is used to create a new vertex when edges intersect.
     * coordinate location is trivial to calculate, but weight[4] may be used to
     * average color, normal, or texture coordinate data. In this program, color
     * is weighted.
     * <br/>
     * double[3] coords
     * <br/>
     * Object[4] data
     * <br/>
     * float[4] weight
     * <br/>
     * Object[1] outData
     *
     * @param coords  Specifics the location of the new vertex.
     * @param data    Specifics the vertices used to create the new vertex.
     * @param weight  Specifics the weights used to create the new vertex.
     * @param outData Reference user the put the coordinates of the new vertex.
     */
    @Override
    public void combine(final double[] coords, final Object[] data, final float[] weight, final Object[] outData) {
        final double[] vertex = new double[6];
        System.arraycopy(coords, 0, vertex, 0, 3);
        outData[0] = vertex;
        super.combine(coords, data, weight, outData);
    }

    @Override
    public void error(final int errnum) {
        final String string = jogamp.opengl.glu.error.Error.gluErrorString(errnum);
        logger.log(Level.WARNING, string);
    }

    @Override
    public void errorData(final int errnum, final Object polygonData) {
        error(errnum);
    }
}
