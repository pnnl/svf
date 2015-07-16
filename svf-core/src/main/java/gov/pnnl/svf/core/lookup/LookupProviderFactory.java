package gov.pnnl.svf.core.lookup;

/**
 * Static factory class for creating lookup providers.
 *
 * @author Arthur Bleeker
 */
public class LookupProviderFactory {

    private LookupProviderFactory() {
        super();
    }

    /**
     * @return a new lookup provider
     */
    public static LookupProvider newLookupProvider() {
        return new LookupProviderImpl();
    }

    /**
     * Create a copy of the supplied lookup provider.
     *
     * @param lookupProvider the lookup provider to copy
     *
     * @return a new lookup provider
     *
     * @throws NullPointerException if lookup provider is null
     */
    public static LookupProvider newLookupProvider(final LookupProvider lookupProvider) {
        return new LookupProviderImpl(lookupProvider);
    }

    /**
     * @return a new multi lookup provider
     */
    public static MultiLookupProvider newMultiLookupProvider() {
        return new MultiLookupProviderImpl();
    }

    /**
     * Create a copy of the supplied multi lookup provider.
     *
     * @param multiLookupProvider the multi lookup provider to copy
     *
     * @return a new multi lookup provider
     *
     * @throws NullPointerException if multi lookup provider is null
     */
    public static MultiLookupProvider newMultiLookupProvider(final MultiLookupProvider multiLookupProvider) {
        return new MultiLookupProviderImpl(multiLookupProvider);
    }
}
