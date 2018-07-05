package gov.pnnl.svf.core.collections;

import java.io.Serializable;

/**
 * A simple immutable key value pair.
 *
 * @author Amelia Bleeker
 * @param <K> the key type
 * @param <V> the value type
 */
public class KeyValuePair<K, V> implements Serializable {

    private static final long serialVersionUID = 1L;
    private final K key;
    private final V value;

    /**
     * Constructor
     *
     * @param key   the key
     * @param value the value
     *
     * @throws NullPointerException if the key is null
     */
    public KeyValuePair(final K key, final V value) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeyValuePair<?, ?> other = (KeyValuePair<?, ?>) obj;
        if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 61 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "KeyValuePair{" + "key=" + key + ", value=" + value + '}';
    }

    public static class Builder<K, V> {

        protected K key;
        protected V value;

        private Builder() {
        }

        public static <K, V> Builder<K, V> construct() {
            return new Builder<>();
        }

        public K key() {
            return this.key;
        }

        public V value() {
            return this.value;
        }

        public Builder<K, V> key(final K key) {
            this.key = key;
            return this;
        }

        public Builder<K, V> value(final V value) {
            this.value = value;
            return this;
        }

        public KeyValuePair<K, V> build() {
            return new KeyValuePair<>(key, value);
        }
    }

}
