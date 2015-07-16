package gov.pnnl.svf.vbo;

/**
 * The vertex buffer object (VBO) data type for supporting vertex data such as
 * normals and colors.
 *
 * @author Arthur Bleeker
 */
public enum VboDataType {

    /**
     * No data specified.
     */
    NONE,
    /**
     * A single data value specified for the whole VBO.
     */
    SINGLE,
    /**
     * A data value specified for each vertex in the VBO.
     */
    PER_VERTEX;
}
