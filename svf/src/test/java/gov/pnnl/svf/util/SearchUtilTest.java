package gov.pnnl.svf.util;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class SearchUtilTest {

    public SearchUtilTest() {
    }

    /**
     * Test of search method, of class SearchUtil.
     */
    @Test
    public void testSearch() {
        Assert.assertEquals(A.EIGHT, SearchUtil.search(EnumSet.allOf(A.class), "EIGHT"));
        Assert.assertEquals(A.EIGHT, SearchUtil.search(EnumSet.allOf(A.class), "eight"));
        Assert.assertEquals(A.EIGHT, SearchUtil.search(EnumSet.allOf(A.class), "Eight"));
        Assert.assertEquals(A.EIGHT, SearchUtil.search(EnumSet.allOf(A.class), "eigh"));
        Assert.assertEquals(A.EIGHT, SearchUtil.search(EnumSet.allOf(A.class), "ei"));
        Assert.assertEquals(A.ONE, SearchUtil.search(EnumSet.allOf(A.class), "One"));

        Assert.assertNull(SearchUtil.search(EnumSet.allOf(A.class), "1"));
        Assert.assertNull(SearchUtil.search(EnumSet.allOf(A.class), "eg"));
        Assert.assertNull(SearchUtil.search(EnumSet.allOf(A.class), "oe"));
    }

    /**
     * Test of search method, of class SearchUtil.
     */
    @Test
    public void testSearchableEnum() {
        Assert.assertEquals(B.EIGHT, SearchUtil.search(EnumSet.allOf(B.class), "EIGHT"));
        Assert.assertEquals(B.EIGHT, SearchUtil.search(EnumSet.allOf(B.class), "eight"));
        Assert.assertEquals(B.EIGHT, SearchUtil.search(EnumSet.allOf(B.class), "Eight"));
        Assert.assertEquals(B.EIGHT, SearchUtil.search(EnumSet.allOf(B.class), "eigh"));
        Assert.assertEquals(B.EIGHT, SearchUtil.search(EnumSet.allOf(B.class), "ei"));
        Assert.assertEquals(B.ONE, SearchUtil.search(EnumSet.allOf(B.class), "One"));

        Assert.assertNull(SearchUtil.search(EnumSet.allOf(B.class), "0"));
        Assert.assertNull(SearchUtil.search(EnumSet.allOf(B.class), "eg"));
        Assert.assertNull(SearchUtil.search(EnumSet.allOf(B.class), "oe"));

        Assert.assertEquals(B.ONE, SearchUtil.search(EnumSet.allOf(B.class), "1"));
        Assert.assertEquals(B.EIGHT, SearchUtil.search(EnumSet.allOf(B.class), "8"));
    }

    @Test
    public void testStatistic() {
        Assert.assertEquals(StatisticType.MINIMUM, SearchUtil.search(EnumSet.allOf(StatisticType.class), "min"));
        Assert.assertEquals(StatisticType.COUNT_DISTINCT, SearchUtil.search(EnumSet.allOf(StatisticType.class), "cnt distngtf"));
    }

    public static enum A {

        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE
    }

    public static enum B implements Searchable {

        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

        @Override
        public String getLabel() {
            final String label = name().substring(0, 1) + name().substring(1, name().length()).toLowerCase(Locale.US);
            return label;
        }

        @Override
        public Set<String> getKeywords() {
            return Collections.singleton(String.valueOf(ordinal() + 1));
        }
    }

    /**
     * Used for a specific test case that failed in Clique.
     *
     * @author Arthur Bleeker
     */
    public static enum StatisticType implements Searchable {

        /**
         * Average: The mean or average of the set. Represented as the same type
         * as the input set.
         */
        /**
         * Average: The mean or average of the set. Represented as the same type
         * as the input set.
         */
        AVERAGE("Average", "Avg", ValueType.INPUT, "mean"),
        /**
         * Median: The median which is the middle value in an odd set or mean of
         * the two middle values in an even set. Represented as the same type as
         * the input set.
         */
        MEDIAN("Median", "Med", ValueType.INPUT),
        /**
         * Minimum: The minimum value (can be negative). Represented as the same
         * type as the input set.
         */
        MINIMUM("Minimum", "Min", ValueType.INPUT),
        /**
         * Maximum: The maximum value (can be negative). Represented as the same
         * type as the input set.
         */
        MAXIMUM("Maximum", "Max", ValueType.INPUT),
        /**
         * Sum: The sum of all values in the set. Represented as the same type
         * as the input set.
         */
        SUM("Sum", "Sum", ValueType.INPUT),
        /**
         * Variance: The variance of all the values in the set. Represented as
         * the same type as the input set.
         */
        VARIANCE("Variance", "Var", ValueType.INPUT),
        /**
         * Standard Deviation: The standard deviation of all the values in the
         * set. Represented as the same type as the input set.
         */
        STANDARD_DEVIATION("Standard Deviation", "Std Dev", ValueType.INPUT),
        /**
         * Distinct: The number of distinct values in the set. Represented as an
         * integer.
         */
        COUNT_DISTINCT("Count Distinct", "Cnt Dist", ValueType.INTEGER, "distinct"),
        /**
         * Size: The total number of values in the set. Represented as an
         * integer.
         */
        SIZE("Size", "Size", ValueType.INTEGER, "count", "number");

        private static final EnumSet<StatisticType> ALL = EnumSet.allOf(StatisticType.class);
        private static final Map<String, StatisticType> LABEL_MAP = new HashMap<>();

        static {
            for (final StatisticType type : StatisticType.values()) {
                LABEL_MAP.put(type.label.toLowerCase(Locale.US), type);
                LABEL_MAP.put(type.abbreviation.toLowerCase(Locale.US), type);
            }
        }
        private final String label;
        private final String abbreviation;
        private final ValueType valueType;
        private final Set<String> keywords;

        private StatisticType(final String label, final String abbreviation, final ValueType valueType, final String... keywords) {
            this.label = label;
            this.abbreviation = abbreviation;
            this.valueType = valueType;
            final Set<String> temp = new HashSet<>();
            temp.add(abbreviation);
            if (keywords != null && keywords.length > 0) {
                for (int i = 0; i < keywords.length; i++) {
                    temp.add(keywords[i]);
                }
            }
            this.keywords = Collections.unmodifiableSet(temp);
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public Set<String> getKeywords() {
            return keywords; // immutable set
        }

        /**
         * The abbreviation for this statistic.
         *
         * @return the abbreviation
         */
        public String getAbbreviation() {
            return abbreviation;
        }

        /**
         * The value type for this statistic.
         *
         * @return the value type
         */
        public ValueType getValueType() {
            return valueType;
        }

        /**
         * Get the type by label or abbreviation.
         *
         * @param label the label or abbreviation of the type (not case
         *              sensitive)
         *
         * @return the type or null if not found
         */
        public static StatisticType getTypeByLabel(final String label) {
            return LABEL_MAP.get(label.toLowerCase(Locale.US));
        }

        /**
         * Create a comma delimited string of the supplied statistic type
         * labels.
         *
         * @param statisticTypes the statistic types
         *
         * @return a comma delimited string
         *
         * @throws NullPointerException if statistic types is null
         */
        public static String toLabels(final Collection<StatisticType> statisticTypes) {
            if (statisticTypes == null) {
                throw new NullPointerException("statisticTypes");
            }
            if (statisticTypes.isEmpty()) {
                return "";
            }
            final StringBuilder sb = new StringBuilder();
            for (final StatisticType statisticType : EnumSet.copyOf(statisticTypes)) {
                sb.append(statisticType.getLabel());
                sb.append(", ");
            }
            sb.replace(sb.length() - 2, sb.length(), "");
            return sb.toString();
        }

        /**
         * Create a comma delimited string of the supplied statistic type
         * abbreviations.
         *
         * @param statisticTypes the statistic types
         *
         * @return a comma delimited string
         *
         * @throws NullPointerException if statistic types is null
         */
        public static String toAbbreviations(final Collection<StatisticType> statisticTypes) {
            if (statisticTypes == null) {
                throw new NullPointerException("statisticTypes");
            }
            if (statisticTypes.isEmpty()) {
                return "";
            }
            final StringBuilder sb = new StringBuilder();
            for (final StatisticType statisticType : EnumSet.copyOf(statisticTypes)) {
                sb.append(statisticType.getAbbreviation());
                sb.append(", ");
            }
            sb.replace(sb.length() - 2, sb.length(), "");
            return sb.toString();
        }

        /**
         * Create a set of statistic types from a comma delimited set of labels,
         * abbreviations, or keywords. They will also be fuzzy matched.
         *
         * @param text the comma delimited set of statistic types
         *
         * @return the set of statistic types
         *
         * @throws NullPointerException if labels is null
         */
        public static Set<StatisticType> parse(final String text) {
            if (text == null) {
                throw new NullPointerException("labels");
            }
            if (text.trim().isEmpty()) {
                return Collections.emptySet();
            }
            final Set<StatisticType> statisticTypes = EnumSet.noneOf(StatisticType.class);
            final String[] parts = text.split(",");
            for (final String part : parts) {
                final StatisticType statisticType = SearchUtil.search(ALL, part.trim());
                if (statisticType != null) {
                    statisticTypes.add(statisticType);
                }
            }
            return statisticTypes;
        }

        /**
         * The type of value that is represented by a particular statistic.
         */
        public static enum ValueType {

            /**
             * Represents a value type that is the same as the input.
             */
            INPUT,
            /**
             * Represents a value type that is an integer.
             */
            INTEGER;
        };
    }

}
