package gov.pnnl.svf.core.collections;

@SuppressWarnings("unchecked")
public class Tuple2<A, B> extends AbstractTuple {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor, sets up null pair
     */
    public Tuple2() {
        this(null, null);
    }

    /**
     * Constructor, accept first, second, and third values
     *
     * @param first  A first value of the tuple
     * @param second B second value of the tuple
     */
    public Tuple2(final A first, final B second) {
        super(first, second);
    }

    /**
     * @return the first object in the tuple
     */
    public A getFirst() {
        return (A) values[0];
    }

    /**
     * @return the second object in the tuple
     */
    public B getSecond() {
        return (B) values[1];
    }

    public static class Builder<A, B> {

        private A first;
        private B second;

        private Builder() {
        }

        public static <A, B> Builder<A, B> construct() {
            return new Builder<>();
        }

        public static <A, B> Builder<A, B> construct(final Class<A> aType, final Class<B> bType) {
            return new Builder<>();
        }

        public A first() {
            return this.first;
        }

        public B second() {
            return this.second;
        }

        public Builder<A, B> first(final A first) {
            this.first = first;
            return this;
        }

        public Builder<A, B> second(final B second) {
            this.second = second;
            return this;
        }

        public Tuple2<A, B> build() {
            return new Tuple2<>(first, second);
        }
    }

}
