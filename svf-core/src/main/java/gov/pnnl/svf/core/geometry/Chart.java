package gov.pnnl.svf.core.geometry;

/**
 * Represents a chart style in a dimensional environment.
 *
 * @author Amelia Bleeker
 */
public enum Chart {

    /**
     * No style
     */
    NONE,
    /**
     * Draw points
     */
    SCATTER,
    /**
     * Draw a line chart
     */
    LINE,
    /**
     * Draw an area chart
     */
    AREA,
    /**
     * Draw a bar chart
     */
    BAR,
    /**
     * Draw a pie chart
     */
    PIE;
}
