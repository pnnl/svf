package gov.pnnl.svf.shape;

/**
 * Shape that allows points to be bundles.
 *
 * @author Amelia Bleeker
 */
public interface BundledShape {

    /**
     * String representation of a field in this object.
     */
    String BUNDLE_ON_LCA = "bundleOnLca";
    /**
     * String representation of a field in this object.
     */
    String BUNDLING_STRENGTH = "bundlingStrength";

    /**
     * A bundling strength of 1 will create a full arc, a bundling strength of 0
     * will create a straight line.
     *
     * @return the bundlingStrength
     */
    double getBundlingStrength();

    /**
     * Draw path through the least common ancestor (LCA).
     *
     * @return the bundleOnLca
     */
    boolean isBundleOnLca();

    /**
     * Draw path through the least common ancestor (LCA).
     *
     * @param bundleOnLca the bundleOnLca to set
     */
    void setBundleOnLca(boolean bundleOnLca);

    /**
     * A bundling strength of 1 will create a full arc, a bundling strength of 0
     * will create a straight line.
     *
     * @param bundlingStrength the bundlingStrength to set
     */
    void setBundlingStrength(double bundlingStrength);
}
