package gov.pnnl.svf.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class for searching objects.
 *
 * @author Arthur Bleeker
 */
public class SearchUtil {

    private static final Logger logger = Logger.getLogger(SearchUtil.class.getName());
    /**
     * The minimum characters required to make a meaningful match.
     */
    private static final int MEANINGFUL_DISTANCE = 3;

    /**
     * Constructor kept private for static util pattern
     */
    protected SearchUtil() {
    }

    /**
     * Attempt to find an object by matching the supplied string with the
     * <code>Searchable</code> label, <code>Searchable</code> keywords,
     * enumeration name, or finally <code>toString</code>. Matching is performed
     * by either exact match or using the Levenshtein Distance for comparison if
     * more than 3 characters match. All comparisons are case insensitive.
     * Enumeration method <code>name()</code> is utilized for enumerations.
     * Implementing the <code>Searchable</code> interface will often give more
     * desirable results.
     *
     * @param set    the set of searchable enumerations
     * @param string the string
     *
     * @return a field use type or null if no meaningful matches were found
     *
     * @param <T>    The enumeration type
     */
    public static <T extends Object> T search(final Set<T> set, final String string) {
        if (string == null) {
            throw new NullPointerException("string");
        }
        final List<T> list = new ArrayList<>(set);
        // populate a list with the Levenshtein distance
        final String source = string.toLowerCase(Locale.US);
        final Map<T, Integer> distances = new HashMap<>();
        final Map<T, Integer> scores = new HashMap<>();
        for (final T e : set) {
            // get the distance for the label
            String target;
            int distance;
            int length;
            int score;
            // check if the label or keywords are a shorter distance
            if (e instanceof Searchable) {
                // searchable interface
                final Searchable se = (Searchable) e;
                // label
                target = se.getLabel().toLowerCase(Locale.US);
                distance = StringUtils.getLevenshteinDistance(source, target);
                length = Math.max(source.length(), target.length());
                score = length - distance;
                // keywords
                for (String keyword : se.getKeywords()) {
                    keyword = keyword.toLowerCase(Locale.US);
                    final int ld = StringUtils.getLevenshteinDistance(source, keyword);
                    final int l = Math.max(source.length(), keyword.length());
                    if (l - ld >= score && ld <= distance) {
                        target = keyword;
                        distance = ld;
                        length = l;
                        score = l - ld;
                    }
                }
            } else if (e instanceof Enum) {
                // enumeration
                target = ((Enum) e).name().toLowerCase(Locale.US);
                distance = StringUtils.getLevenshteinDistance(source, target);
                length = Math.max(source.length(), target.length());
                score = length - distance;
            } else {
                // object
                target = e.toString();
                distance = StringUtils.getLevenshteinDistance(source, target);
                length = Math.max(source.length(), target.length());
                score = length - distance;
            }
            // place in the distances list using the shortest distance found
            distances.put(e, distance);
            scores.put(e, score);
            // remove from the master list if it's not a meaningful match
            // verify that this is a relevant field use type for the string
            if (!((e instanceof Searchable && ((Searchable) e).getKeywords().contains(source)) // it's a keyword
                  || target.contains(source) // the field use type contains the string
                  || source.contains(target) // the string contains the field use type
                  || score >= MEANINGFUL_DISTANCE)) { // the string is a fuzzy match
                list.remove(e);
            } else {
                final boolean kw = (e instanceof Searchable && ((Searchable) e).getKeywords().contains(source)); // it's a keyword
                if (kw) {
                    logger.log(Level.FINE, "Object {0} retained because \"{1}\" is a keyword.", new Object[]{e, target});
                } else {
                    final boolean tcs = target.contains(source); // the field use type contains the string
                    if (tcs) {
                        logger.log(Level.FINE, "Object {0} retained because \"{1}\" contains \"{2}\".", new Object[]{e, target, source});
                    } else {
                        final boolean sct = source.contains(target); // the string contains the field use type
                        if (sct) {
                            logger.log(Level.FINE, "Object {0} retained because \"{1}\" contains \"{2}\".", new Object[]{e, source, target});
                        }
                        final boolean md = length - distance >= MEANINGFUL_DISTANCE; // the string is a fuzzy match
                        if (md) {
                            logger.log(Level.FINE, "Object {0} retained because (length - distance >= {3}): {1} - {2} >= {3} .", new Object[]{e, length, distance, MEANINGFUL_DISTANCE});
                        }
                    }
                }
            }
        }
        Collections.sort(list, (final T o1, final T o2) -> scores.get(o2).compareTo(scores.get(o1)));
        logger.log(Level.INFO, "Map of scores for all objects for text \"{0}\": {1}", new Object[]{string, scores});
        logger.log(Level.INFO, "List of prioritized matching objects sorted by scores: {0}", list);
        Collections.sort(list, (final T o1, final T o2) -> distances.get(o1).compareTo(distances.get(o2)));
        logger.log(Level.INFO, "Map of distances for all objects for text \"{0}\": {1}", new Object[]{string, distances});
        logger.log(Level.INFO, "List of prioritized matching objects additionally sorted by distances: {0}", list);
        return list.isEmpty() ? null : list.get(0);
    }
}
