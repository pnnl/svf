package gov.pnnl.svf.util;

import java.util.Set;

/**
 * Interface type for objects that can be searched based on label and/or
 * keywords.
 *
 * @author Arthur Bleeker
 */
public interface Searchable {

    /**
     * The human readable label for this object. Should never be null.
     *
     * @return the label
     */
    String getLabel();

    /**
     * A set of keywords for this object. Should never be null.
     *
     * @return the keywords set
     */
    Set<String> getKeywords();
}
