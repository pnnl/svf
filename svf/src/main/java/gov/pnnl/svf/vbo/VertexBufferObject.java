package gov.pnnl.svf.vbo;

import java.io.Serializable;

/**
 * Container for a vertex buffer object (VBO). This container can also be used
 * to render in immediate mode. Individual array values can be changed after
 * creation. However, VBO object data types, which is affected by array sizes,
 * cannot.
 *
 * @author Arthur Bleeker
 */
public interface VertexBufferObject extends Serializable {

    /**
     * The GL render mode. E.g. <code>GL2.GL_TRIANGLES</code>,
     * <code>GL2.GL_TRIANGLE_FAN</code>, <code>GL2.GL_QUAD_STRIP</code>,
     * <code>GL.GL_POINTS</code>, etc.
     *
     * @return the GL render mode
     */
    int getMode();

    /**
     * The number of vertices contained in this VBO.
     *
     * @return the size
     */
    int getSize();

    /**
     * The dimension of the vertices. E.g. <code>DimensionConst.TWO_D</code> for
     * two dimensional space.
     *
     * @return the vertex dimension
     */
    int getVertexDimension();

    /**
     * The dimension of the texture coordinates. E.g.
     * <code>DimensionConst.THREE_D</code> for three dimensional space.
     *
     * @return the texture coordinate dimension
     */
    int getTexCoordDimension();

    /**
     * Check what type of normal data is specified if any.
     *
     * @return the normal data type
     */
    VboDataType getNormalDataType();

    /**
     * Check what type of color data is specified if any.
     *
     * @return the color data type
     */
    VboDataType getColorDataType();

    /**
     * A list of vertices in order according to the specified vertex dimension.
     * This will never be null. I.e. <code>{X, Y, Z, X, Y, Z, X, Y, Z}</code>
     *
     * @return the list of vertices
     */
    double[] getVertices();

    /**
     * A list of normals in order. It be <code>null</code> for no normal
     * specified, a length of <code>3</code> for a single normal, or
     * <code>i * 3</code> for a normal specified for each vertex. I.e.
     * <code>null</code>, or<code>{X, Y, Z}</code>, or
     * <code>{X, Y, Z, X, Y, Z, X, Y, Z}</code>
     *
     * @return the list of normals
     */
    double[] getNormals();

    /**
     * A list of texture coordinates in order according to the specified texture
     * coordinate dimension. It will either be <code>null</code> for no texture
     * coordinates specified or <code>i * getTexCoordDimension()</code>. I.e.
     * <code>{X, Y, Z, X, Y, Z, X, Y, Z}</code>
     *
     * @return the list of texture coordinates
     */
    double[] getTexCoords();

    /**
     * A list of colors in order. It will either be <code>null</code> for no
     * color information specified, <code>4</code> for a single color for the
     * whole object, or <code>i * 4</code>. I.e.
     * <code>{R, G, B, A, R, G, B, A, R, G, B, A}</code>
     *
     * @return the colors
     */
    float[] getColors();

    public static class Builder {

        private int mode;
        private int vertexDimension;
        private int texCoordDimension;
        private double[] vertices;
        private double[] normals;
        private double[] texCoords;
        private float[] colors;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public Builder mode(final int mode) {
            this.mode = mode;
            return this;
        }

        public Builder vertexDimension(final int vertexDimension) {
            this.vertexDimension = vertexDimension;
            return this;
        }

        public Builder texCoordDimension(final int texCoordDimension) {
            this.texCoordDimension = texCoordDimension;
            return this;
        }

        public Builder vertices(final double[] vertices) {
            this.vertices = vertices;
            return this;
        }

        public Builder normals(final double[] normals) {
            this.normals = normals;
            return this;
        }

        public Builder texCoords(final double[] texCoords) {
            this.texCoords = texCoords;
            return this;
        }

        public Builder colors(final float[] colors) {
            this.colors = colors;
            return this;
        }

        public VertexBufferObject build() {
            return new VertexBufferObjectImpl(mode, vertexDimension, texCoordDimension, vertices, normals, texCoords, colors);
        }
    }

}
