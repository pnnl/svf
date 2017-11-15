package gov.pnnl.svf.core.collections;

/**
 * Simple immutable tuple class.
 *
 * @param <A> first type
 * @param <B> second type
 * @param <C> third type
 * @param <D> fourth type
 *
 * @author Arthur Bleeker
 */
@SuppressWarnings("unchecked")
public class Tuple4<A, B, C, D> extends AbstractTuple {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor, sets up null pair
     */
    public Tuple4() {
        this(null, null, null, null);
    }

    /**
     * Constructor, accept first, second, and third values
     *
     * @param first  A first value of the tuple
     * @param second B second value of the tuple
     * @param third  C third value of the tuple
     * @param fourth C fourth value of the tuple
     */
    public Tuple4(final A first, final B second, final C third, final D fourth) {
        super(first, second, third, fourth);
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

    /**
     * @return the fourth object in the tuple
     */
    public D getFourth() {
        return (D) values[3];
    }

    public static class Builder<A, B, C, D> {

        private A first;
        private B second;
        private C third;
        private D fourth;

        private Builder() {
        }

        public static <A, B, C, D> Builder<A, B, C, D> construct() {
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

        public D fourth() {
            return this.fourth;
        }

        public Builder<A, B, C, D> first(final A first) {
            this.first = first;
            return this;
        }

        public Builder<A, B, C, D> second(final B second) {
            this.second = second;
            return this;
        }

        public Builder<A, B, C, D> third(final C third) {
            this.third = third;
            return this;
        }

        public Builder<A, B, C, D> fourth(final D fourth) {
            this.fourth = fourth;
            return this;
        }

        public Tuple4<A, B, C, D> build() {
            return new Tuple4<>(first, second, third, fourth);
        }
    }
}
