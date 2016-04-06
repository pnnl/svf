package gov.pnnl.svf.util;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLUtessellatorCallbackAdapter;
import gov.pnnl.svf.core.color.Color;
import static gov.pnnl.svf.core.constant.DimensionConst.TWO_D;
import static gov.pnnl.svf.util.VboUtil.buildVbo;
import gov.pnnl.svf.vbo.VertexBufferObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * VBO implementation of the GLUtessellatorCallback.
 *
 * @author Arthur Bleeker
 */
public class TessellatorCallbackVbo extends GLUtessellatorCallbackAdapter {

    private static final Logger logger = Logger.getLogger(TessellatorCallbackVbo.class.getName());
    private static final double[] NORMAL = new double[]{0.0, 0.0, 1.0};

    private final Color color;
    private int mode = 0;
    private final DoubleList vertices = new ArrayDoubleList();
    private final List<VertexBufferObject> vbos = new ArrayList<>();

    /**
     * Constructor
     *
     * @param color optional color
     */
    public TessellatorCallbackVbo(final Color color) {
        super();
        this.color = color;
    }

    /**
     * Get the current list of VBOs.
     *
     * @return the vbo list
     */
    public List<VertexBufferObject> getVbos() {
        return vbos;
    }

    @Override
    public void begin(final int type) {
        switch (type) {
            case GL.GL_TRIANGLE_FAN:
            case GL.GL_TRIANGLE_STRIP:
            case GL.GL_TRIANGLES:
            case GL.GL_LINE_LOOP:
                this.mode = type;
                break;
            default:
                logger.log(Level.WARNING, "Unknown type specified: {0}", type);
        }
    }

    @Override
    public void beginData(final int type, final Object polygonData) {
        begin(type);
    }

    @Override
    public void end() {
        final VertexBufferObject.Builder vbo = VertexBufferObject.Builder.construct()
                .vertexDimension(TWO_D)
                .mode(mode)
                .vertices(vertices.toArray())
                .texCoordDimension(TWO_D);
        vbos.add(buildVbo(vbo, NORMAL, null, color));
        vertices.clear();
    }

    @Override
    public void endData(final Object polygonData) {
        end();
    }

    @Override
    public void edgeFlag(final boolean boundaryEdge) {
        // not used
    }

    @Override
    public void edgeFlagData(final boolean boundaryEdge, final Object polygonData) {
        edgeFlag(boundaryEdge);
    }

    @Override
    public void vertex(final Object vertexData) {
        if (vertexData instanceof double[]) {
            vertices.add(((double[]) vertexData)[0]);
            vertices.add(((double[]) vertexData)[1]);
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
     * <br>
     * double[3] coords
     * <br>
     * Object[4] data
     * <br>
     * float[4] weight
     * <br>
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
