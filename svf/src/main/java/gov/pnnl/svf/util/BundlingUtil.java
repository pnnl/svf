package gov.pnnl.svf.util;

import gov.pnnl.svf.geometry.Point2D;
import gov.pnnl.svf.geometry.Point3D;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Utility for creating and working with hierarchical edge bundled points.
 *
 * @author Arthur Bleeker
 */
public class BundlingUtil {

    /**
     * Constructor is private to prevent instantiation of a static helper class.
     */
    private BundlingUtil() {
        super();
    }

    /**
     * Create a list of hierarchical edge bundled points.
     *
     * @param points           the series of points to bundle
     * @param bundlingStrength the strength of the bundling {x:0.0 &lte x &gte
     *                         1.0}
     * @param bundleOnLca      true to bundle, include, the least common
     *                         ancestor
     *
     * @return the list of bundled points
     */
    public static List<Point2D> createBundledPoint2D(final List<Point2D> points, final double bundlingStrength, final boolean bundleOnLca) {
        if (points == null) {
            throw new NullPointerException("points");
        }
        if (bundlingStrength < 0.0 || bundlingStrength > 1.0) {
            throw new IllegalArgumentException("bundlingStrength");
        }
        if (points.size() < 3) {
            // can't bundle with less than three points
            return points;
        }
        final List<Point2D> bundled;
        if (bundlingStrength == 1.0) {
            // there won't be any change
            bundled = new ArrayList<>(points);
        } else {
            bundled = new ArrayList<>(points.size());
            final Point2D start = points.get(0);
            final Point2D end = points.get(points.size() - 1);
            for (int i = 0; i < points.size(); i++) {
                final Point2D value = points.get(i);
                final double x = BundlingUtil.findBundledPoint(value.getX(), start.getX(), end.getX(), i, points.size(), bundlingStrength);
                final double y = BundlingUtil.findBundledPoint(value.getY(), start.getY(), end.getY(), i, points.size(), bundlingStrength);
                bundled.add(new Point2D(x, y));
            }
        }
        // find the shortest path and
        // remove the least common ancestor, and anything above it, if there is one
        BundlingUtil.removeLeastCommonAncestor(points, bundled, bundleOnLca);
        return bundled;
    }

    /**
     * Create a list of hierarchical edge bundled points.
     *
     * @param points           the series of points to bundle
     * @param bundlingStrength the strength of the bundling {x:0.0 &lte x &gte
     *                         1.0}
     * @param bundleOnLca      true to bundle, include, the least common
     *                         ancestor
     *
     * @return the list of bundled points
     */
    public static List<Point3D> createBundledPoint3D(final List<Point3D> points, final double bundlingStrength, final boolean bundleOnLca) {
        if (points == null) {
            throw new NullPointerException("points");
        }
        if (bundlingStrength < 0.0 || bundlingStrength > 1.0) {
            throw new IllegalArgumentException("bundlingStrength");
        }
        if (points.size() < 3) {
            // can't bundle with less than three points
            return points;
        }
        final List<Point3D> bundled;
        if (bundlingStrength == 1.0) {
            // there won't be any change
            bundled = new ArrayList<>(points);
        } else {
            bundled = new ArrayList<>(points.size());
            final Point3D start = points.get(0);
            final Point3D end = points.get(points.size() - 1);
            for (int i = 0; i < points.size(); i++) {
                final Point3D value = points.get(i);
                final double x = BundlingUtil.findBundledPoint(value.getX(), start.getX(), end.getX(), i, points.size(), bundlingStrength);
                final double y = BundlingUtil.findBundledPoint(value.getY(), start.getY(), end.getY(), i, points.size(), bundlingStrength);
                final double z = BundlingUtil.findBundledPoint(value.getZ(), start.getZ(), end.getZ(), i, points.size(), bundlingStrength);
                bundled.add(new Point3D(x, y, z));
            }
        }
        // find the shortest path and
        // remove the least common ancestor, and anything above it, if there is one
        BundlingUtil.removeLeastCommonAncestor(points, bundled, bundleOnLca);
        return bundled;
    }

    /**
     * Create a list of hierarchical edge bundled points.
     *
     * @param points           the series of points to bundle
     * @param bundlingStrength the strength of the bundling {x:0.0 &lte x &gte
     *                         1.0}
     * @param bundleOnLca      true to bundle, include, the least common
     *                         ancestor
     *
     * @return the list of bundled points
     */
    public static List<Vector3D> createBundledVector3D(final List<Vector3D> points, final double bundlingStrength, final boolean bundleOnLca) {
        if (points == null) {
            throw new NullPointerException("points");
        }
        if (bundlingStrength < 0.0 || bundlingStrength > 1.0) {
            throw new IllegalArgumentException("bundlingStrength");
        }
        if (points.size() < 3) {
            // can't bundle with less than three points
            return points;
        }
        final List<Vector3D> bundled;
        if (bundlingStrength == 1.0) {
            // there won't be any change
            bundled = new ArrayList<>(points);
        } else {
            bundled = new ArrayList<>(points.size());
            final Vector3D start = points.get(0);
            final Vector3D end = points.get(points.size() - 1);
            for (int i = 0; i < points.size(); i++) {
                final Vector3D value = points.get(i);
                final double x = BundlingUtil.findBundledPoint(value.getX(), start.getX(), end.getX(), i, points.size(), bundlingStrength);
                final double y = BundlingUtil.findBundledPoint(value.getY(), start.getY(), end.getY(), i, points.size(), bundlingStrength);
                final double z = BundlingUtil.findBundledPoint(value.getZ(), start.getZ(), end.getZ(), i, points.size(), bundlingStrength);
                bundled.add(new Vector3D(x, y, z));
            }
        }
        // find the shortest path and
        // remove the least common ancestor, and anything above it, if there is one
        BundlingUtil.removeLeastCommonAncestor(points, bundled, bundleOnLca);
        return bundled;
    }

    private static double findBundledPoint(final double value, final double start, final double end, final int step, final int size, final double strength) {
        return (strength * value) + (1.0 - strength) * (start + step / (size - 1.0) * (end - start));
    }

    private static void removeLeastCommonAncestor(final List<?> points, final List<?> bundled, final boolean bundleOnLca) {
        // can't have a LCA with less than three entries
        if (points.size() < 3) {
            return;
        }
        // three entries means the middle is the LCA
        if (points.size() == 3) {
            if (!bundleOnLca) {
                // remove the lca
                bundled.remove(1);
            }
            return;
        }
        // look for a common ancestor
        for (int i = 1; i < points.size() / 2; i++) {
            for (int j = points.size() - 2; j > points.size() / 2 - 1; j--) {
                // if two match then we found an LCA
                if (points.get(i).equals(points.get(j))) {
                    // loop through the first match to the second match
                    // (second match is not removed if bundling on the lca)
                    // and remove them as well as the points inbetween
                    for (int k = i; k < j + (bundleOnLca ? 0 : 1); k++) {
                        bundled.remove(i);
                    }
                    return;
                }
            }
        }
        // there is still an LCA if the size is odd
        if (!bundleOnLca && (points.size() & 1) == 1) {
            bundled.remove(points.size() / 2);
        }
    }
}
