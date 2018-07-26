package gov.pnnl.svf.core.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Util class for constructing a hashmap.
 *
 * @author Amelia Bleeker
 */
public class MapUtil {

    /**
     *
     * Constructor private for util pattern
     *
     */
    private MapUtil() {

    }

    public static class Builder<K, V> {

        private final Map<K, V> with;

        private Builder() {
            with = new HashMap<>();
        }

        private Builder(final Map<K, V> map) {
            with = Objects.requireNonNull(map, "Map is required");
        }

        public static <K, V> Builder<K, V> construct() {
            return new Builder<>();
        }

        public static <K, V> Builder<K, V> construct(final Class<K> keyType, final Class<V> valueType) {
            return new Builder<>();
        }

        public static <K, V> Builder<K, V> construct(final Map<K, V> map) {
            final Builder<K, V> builder = new Builder<>();
            builder.with.putAll(map);
            return builder;
        }

        public static <K, V> Builder<K, V> construct(final Class<?> mapType) throws InstantiationException, IllegalAccessException {
            return new Builder<>((Map<K, V>) mapType.newInstance());
        }

        public static <K, V> Builder<K, V> construct(final Class<?> mapType, final Class<K> keyType, final Class<V> valueType) throws InstantiationException, IllegalAccessException {
            return new Builder<>((Map<K, V>) mapType.newInstance());
        }

        public Builder<K, V> with(final K key, final V value) {
            this.with.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            return with;
        }

    }

}
