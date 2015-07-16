package gov.pnnl.svf.shape;

/**
 * Shape that represents a volumetric area.
 *
 * @author Arthur Bleeker
 */
public interface VolumeShape {

    /**
     * String representation of a field in this object.
     */
    String SLICES = "slices";

    /**
     *
     * @return the number of slices to draw per unit
     */
    int getSlices();

    /**
     *
     * @param slices The number of slices to draw per unit
     */
    void setSlices(int slices);
}
