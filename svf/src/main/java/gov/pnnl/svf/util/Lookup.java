package gov.pnnl.svf.util;

import gov.pnnl.svf.core.lookup.LookupProviderFactory;
import gov.pnnl.svf.core.lookup.MultiLookupProvider;

/**
 * The global lookup. This can be used to locate scenes that have been started
 * within the current class loader.
 *
 * @author Arthur Bleeker
 */
public class Lookup {

    private static final MultiLookupProvider lookup = LookupProviderFactory.newMultiLookupProvider();

    /**
     * Constructor kept private for static utility class
     */
    private Lookup() {
    }

    /**
     * The global lookup for this class loader.
     *
     * @return the global lookup
     */
    public static MultiLookupProvider getLookup() {
        return lookup;
    }
}
