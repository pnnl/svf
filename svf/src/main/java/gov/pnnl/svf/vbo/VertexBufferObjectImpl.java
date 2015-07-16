package gov.pnnl.svf.vbo;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Default implementation of the vertex buffer object.
 *
 * @author Arthur Bleeker
 */
public class VertexBufferObjectImpl implements VertexBufferObject, Serializable {

    private static final long serialVersionUID = 1L;
    private final int size;
    private final int mode;
    private final int vertexDimension;
    private final int texCoordDimension;
    private final double[] vertices;
    private final double[] normals;
    private final double[] texCoords;
    private final float[] colors;

    /**
     * Constructor
     *
     * @param mode              the mode
     * @param vertexDimension   the vertex dimension
     * @param texCoordDimension the texture coordinate dimension
     * @param vertices          the list of vertices
     * @param normals           the list of normals
     * @param texCoords         the texture coordinates
     * @param colors            the colors
     *
     * @throws NullPointerException     if vertices is null
     * @throws IllegalArgumentException if any list lengths are incorrect,
     *                                  vertex dimension is outside [2,3], or
     *                                  tex coord dimension is outside [1,4]
     */
    public VertexBufferObjectImpl(final int mode,
                                  final int vertexDimension,
                                  final int texCoordDimension,
                                  final double[] vertices,
                                  final double[] normals,
                                  final double[] texCoords,
                                  final float[] colors) {
        if (vertices == null) {
            throw new NullPointerException("vertices");
        }
        if (vertexDimension < 2 || vertexDimension > 3) {
            throw new IllegalArgumentException("vertexDimension outside [2,3]");
        }
        if (vertices.length % vertexDimension != 0) {
            throw new IllegalArgumentException("vertices");
        }
        size = vertices.length / vertexDimension;
        if (normals != null && !(normals.length == 3 || normals.length == size * 3)) {
            throw new IllegalArgumentException("normals");
        }
        if (texCoords != null && texCoords.length % texCoordDimension != 0 && texCoords.length != size * texCoordDimension) {
            throw new IllegalArgumentException("texCoords");
        }
        if (texCoords != null && (texCoordDimension < 1 || texCoordDimension > 4)) {
            throw new IllegalArgumentException("texCoordDimension outside [1,4]");
        }
        if (colors != null && !(colors.length == 4 || colors.length == size * 4)) {
            throw new IllegalArgumentException("colors");
        }
        this.mode = mode;
        this.vertexDimension = vertexDimension;
        this.texCoordDimension = texCoordDimension;
        this.vertices = vertices;
        this.normals = normals;
        this.texCoords = texCoords;
        this.colors = colors;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public int getVertexDimension() {
        return vertexDimension;
    }

    @Override
    public int getTexCoordDimension() {
        return texCoordDimension;
    }

    @Override
    public VboDataType getNormalDataType() {
        if (normals == null) {
            return VboDataType.NONE;
        } else if (normals.length == 3) {
            return VboDataType.SINGLE;
        } else {
            return VboDataType.PER_VERTEX;
        }
    }

    @Override
    public VboDataType getColorDataType() {
        if (colors == null) {
            return VboDataType.NONE;
        } else if (colors.length == 4) {
            return VboDataType.SINGLE;
        } else {
            return VboDataType.PER_VERTEX;
        }
    }

    @Override
    public double[] getVertices() {
        return vertices;
    }

    @Override
    public double[] getNormals() {
        return normals;
    }

    @Override
    public double[] getTexCoords() {
        return texCoords;
    }

    @Override
    public float[] getColors() {
        return colors;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.size;
        hash = 97 * hash + this.mode;
        hash = 97 * hash + this.vertexDimension;
        hash = 97 * hash + this.texCoordDimension;
        hash = 97 * hash + Arrays.hashCode(this.vertices);
        hash = 97 * hash + Arrays.hashCode(this.normals);
        hash = 97 * hash + Arrays.hashCode(this.texCoords);
        hash = 97 * hash + Arrays.hashCode(this.colors);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VertexBufferObjectImpl other = (VertexBufferObjectImpl) obj;
        if (this.size != other.size) {
            return false;
        }
        if (this.mode != other.mode) {
            return false;
        }
        if (this.vertexDimension != other.vertexDimension) {
            return false;
        }
        if (this.texCoordDimension != other.texCoordDimension) {
            return false;
        }
        if (!Arrays.equals(this.vertices, other.vertices)) {
            return false;
        }
        if (!Arrays.equals(this.normals, other.normals)) {
            return false;
        }
        if (!Arrays.equals(this.texCoords, other.texCoords)) {
            return false;
        }
        if (!Arrays.equals(this.colors, other.colors)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VertexBufferObject{"
               + "size=" + size
               + ", mode=" + mode
               + ", vertexDimension=" + vertexDimension
               + ", texCoordDimension=" + texCoordDimension
               + ", vertices.length=" + vertices.length
               + ", normals.length=" + (normals != null ? normals.length : "null")
               + ", texCoords.length=" + (texCoords != null ? texCoords.length : "null")
               + ", colors.length=" + (colors != null ? colors.length : "null") + '}';
    }

}
