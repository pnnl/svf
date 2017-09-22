package gov.pnnl.svf.core.collections;

@SuppressWarnings("unchecked")
public class Tuple3<A, B, C> extends AbstractTuple {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor, sets up null pair
     */
    public Tuple3() {
        this(null, null, null);
    }

    /**
     * Constructor, accept first, second, and third values
     *
     * @param first  A first value of the tuple
     * @param second B second value of the tuple
     * @param third  C third value of the tuple
     */
    public Tuple3(final A first, final B second, final C third) {
        super(first, second, third);
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

    /**
     * @return the third object in the tuple
     */
    public C getThird() {
        return (C) values[2];
    }

    public static class Builder<A, B, C> {

        private A first;
        private B second;
        private C third;

        private Builder() {
        }

        public static <A, B, C> Builder<A, B, C> construct() {
            return new Builder<>();
        }

        public A first() {
            return this.first;
        }

        public B second() {
            return this.second;
        }

        public C third() {
            return this.third;
        }

        public Builder<A, B, C> first(final A first) {
            this.first = first;
            return this;
        }

        public Builder<A, B, C> second(final B second) {
            this.second = second;
            return this;
        }

        public Builder<A, B, C> third(final C third) {
            this.third = third;
            return this;
        }

        public Tuple3<A, B, C> build() {
            return new Tuple3<>(first, second, third);
        }
    }

}
