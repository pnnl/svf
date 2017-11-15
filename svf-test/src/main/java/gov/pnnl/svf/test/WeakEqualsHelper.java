/**
 * source url:
 * https://code.google.com/p/sunbeta/source/browse/trunk/src/main/java/net/sunbeta/test/util/WeakEqualsHelper.java?r=2
 * license url: http://www.apache.org/licenses/LICENSE-2.0
 */
package gov.pnnl.svf.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that help to test equals for two object. For test use
 * {@code weakEquals()} method.
 * <p>
 * date: 27.05.2008
 * <p>
 *
 * @author : Pokidov.Dmitry
 */
public class WeakEqualsHelper {

    private static final Logger logger = Logger.getLogger(WeakEqualsHelper.class.getName());
    private static final List<String> errorLog = Collections.synchronizedList(new ArrayList<String>());

    private enum CheckNullsResult {

        BOTH_NULLS, NOT_NULLS, ONE_NULL;
    }

    /**
     * Constructor private for static helper class.
     */
    private WeakEqualsHelper() {
        super();
    }

    /**
     * Recursively compare object by its class and all superclasses. To
     * standalone test collections, arrays and maps use methods:
     * {@code equalsCollection(), equalsArray(), equalsMap()}
     *
     * @param o1 First object for compare
     * @param o2 Second object for compare
     *
     * @return {@code true}, if objects are equals, else {@code false}
     */
    public static boolean weakEquals(final Object o1, final Object o2) {
        return WeakEqualsHelper.weakEquals(o1, o2, null, false);
    }

    /**
     * Recursively compare object by its class and all superclasses. To
     * standalone test collections, arrays and maps use methods:
     * {@code equalsCollection(), equalsArray(), equalsMap()}
     *
     * @param o1              First object for compare
     * @param o2              Second object for compare
     * @param ignoreTransient true to ignore transient fields
     *
     * @return {@code true}, if objects are equals, else {@code false}
     */
    public static boolean weakEquals(final Object o1, final Object o2, final boolean ignoreTransient) {
        return WeakEqualsHelper.weakEquals(o1, o2, null, ignoreTransient);
    }

    /**
     * Recursively compare object by its class and all superclasses. To
     * standalone test collections, arrays and maps use methods:
     * {@code equalsCollection(), equalsArray(), equalsMap()}
     *
     * @param o1              First object for compare
     * @param o2              Second object for compare
     * @param notEqualsFields fields that will be ignore. Key is name of class,
     *                        Value - list of field's name.
     *
     * @return {@code true}, if objects are equals, else {@code false}
     */
    public static boolean weakEquals(final Object o1, final Object o2, final Map<String, List<String>> notEqualsFields) {
        return WeakEqualsHelper.weakEquals(o1, o2, notEqualsFields, false);
    }

    /**
     * Recursively compare object by its class and all superclasses. To
     * standalone test collections, arrays and maps use methods:
     * {@code equalsCollection(), equalsArray(), equalsMap()}
     *
     * @param o1              First object for compare
     * @param o2              Second object for compare
     * @param notEqualsFields fields that will be ignore. Key is name of class,
     *                        Value - list of field's name.
     * @param ignoreTransient true to ignore transient fields
     *
     * @return {@code true}, if objects are equals, else {@code false}
     */
    public static boolean weakEquals(final Object o1, final Object o2, final Map<String, List<String>> notEqualsFields, final boolean ignoreTransient) {
        final Map<String, List<String>> fields = notEqualsFields;
        errorLog.clear();
        final boolean ret = WeakEqualsHelper.doEquals(o1, o2, fields, ignoreTransient);
        return ret;
    }

    /**
     * Get a copy of the list of errors generated since the last run. Notice
     * that this list will not be correct if testing occurs on multiple threads.
     *
     * @return the list of errors
     */
    public static List<String> getErrors() {
        synchronized (errorLog) {
            final List<String> copy = new ArrayList<>(errorLog);
            return copy;
        }
    }

    private static boolean needEqual(final Field f, final Map<String, List<String>> fields, final boolean ignoreTransient) {
        if (ignoreTransient && Modifier.isTransient(f.getModifiers())) {
            System.out.println("Ignored Transient Field: " + f);
            return false;
        }
        if (fields != null) {
            final List<String> classFields = fields.get(f.getDeclaringClass().getName());
            return !(classFields != null && classFields.contains(f.getName()));
        }
        return true;
    }

    private static boolean doEquals(Object obj1, Object obj2, final Map<String, List<String>> fields, final boolean ignoreTransient) {
        if (obj1 == obj2) {
            return true;
        }
        Class<?> c1 = obj1.getClass();
        Class<?> c2 = obj2.getClass();
        if (c1.getName().compareTo(c2.getName()) == 0) {

            while (!c1.getName().equals(Object.class.getName())) {
                for (final Field f1 : c1.getDeclaredFields()) {
                    if (WeakEqualsHelper.needEqual(f1, fields, ignoreTransient)) {
                        final String errorMessage = "ERROR: Class " + obj1.getClass().getName() + " are not equal in field -->" + f1.getName() + "<--";
                        if (!f1.getName().contains("this$")) {
                            try {
                                final Field f2 = c2.getDeclaredField(f1.getName());

                                f1.setAccessible(true);
                                f2.setAccessible(true);

                                errorLog.add("Current field-->" + f1.getName() + "<--");

                                if (f1.isEnumConstant()) {
                                    if (!f1.get(obj1).equals(f2.get(obj2))) {
                                        return false;
                                    }
                                } else if (!WeakEqualsHelper.doObjectEquals(f1.get(obj1), f2.get(obj2), fields, ignoreTransient)) {
                                    errorLog.add(errorMessage + "[" + (f1.get(obj1) != null && f1.get(obj1).equals("null") ? "NULL" : f1.get(obj1)) + ":"
                                                 + (f2.get(obj2) != null && f2.get(obj2).equals("null") ? "NULL" : f2.get(obj2)) + "]");
                                    return false;
                                }
                            } catch (final NoSuchFieldException nsme) {
                                errorLog.add("ERROR: No such field " + f1.getName());
                                return false;
                            } catch (final IllegalAccessException e) {
                                logger.log(Level.WARNING, "", e);
                                return false;
                            }
                        } else {
                            errorLog.add("INFO: ignore field -->" + f1.getName() + "<--");
                        }
                    } else {
                        logger.log(Level.INFO, "INFO: ignore field -->{0}<--", f1.getName());
                    }
                }

                c1 = c1.getSuperclass();
                c2 = c2.getSuperclass();
                obj1 = c1.cast(obj1);
                obj2 = c2.cast(obj2);
            }
        } else {
            errorLog.add("Class names are not equals in field[" + c1.getName() + "," + c2.getName() + "]");
            return false;
        }
        return true;
    }

    private static boolean doObjectEquals(final Object o1, final Object o2, final Map<String, List<String>> fields, final boolean ignoreTransient) {
        if (WeakEqualsHelper.checkNulls(o1, o2) == CheckNullsResult.NOT_NULLS) {
            if (o1 instanceof Map.Entry) {
                final Map.Entry<?, ?> e1 = (Map.Entry<?, ?>) o1;
                final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>) o2;
                return WeakEqualsHelper.doObjectEquals(e1.getKey(), e2.getKey(), fields, ignoreTransient)
                       && WeakEqualsHelper.doObjectEquals(e1.getValue(), e2.getValue(), fields, ignoreTransient);
            } else if (o1 instanceof Map) {
                return WeakEqualsHelper.equalsMap((Map<?, ?>) o1, (Map<?, ?>) o2, fields, ignoreTransient);
            } else if (o1 instanceof Collection) {
                return WeakEqualsHelper.equalsCollection((Collection<?>) o1, (Collection<?>) o2, fields, ignoreTransient);
            } else if (o1.getClass().isArray()) {
                try {
                    return WeakEqualsHelper.equalsArray((Object[]) o1, (Object[]) o2, fields, ignoreTransient);
                } catch (final ClassCastException ex) {
                    // ignore
                }
                try {
                    return Arrays.equals((boolean[]) o1, (boolean[]) o2);
                } catch (final ClassCastException ex) {
                    // ignore
                }
                try {
                    return Arrays.equals((byte[]) o1, (byte[]) o2);
                } catch (final ClassCastException ex) {
                    // ignore
                }
                try {
                    return Arrays.equals((char[]) o1, (char[]) o2);
                } catch (final ClassCastException ex) {
                    // ignore
                }
                try {
                    return Arrays.equals((double[]) o1, (double[]) o2);
                } catch (final ClassCastException ex) {
                    // ignore
                }
                try {
                    return Arrays.equals((float[]) o1, (float[]) o2);
                } catch (final ClassCastException ex) {
                    // ignore
                }
                try {
                    return Arrays.equals((int[]) o1, (int[]) o2);
                } catch (final ClassCastException ex) {
                    // ignore
                }
                try {
                    return Arrays.equals((long[]) o1, (long[]) o2);
                } catch (final ClassCastException ex) {
                    // ignore
                }
                try {
                    return Arrays.equals((short[]) o1, (short[]) o2);
                } catch (final ClassCastException ex) {
                    // ignore
                }
            } else if (!o1.getClass().getName().equals(Object.class.getName())) {
                if (WeakEqualsHelper.hasEquals(o1.getClass())) {
                    if (!o1.equals(o2)) {
                        if (o1 instanceof String
                            && o2 instanceof String) {
                            final String s1 = ((String) o1).replaceAll("[\n\t\r ]", "");
                            final String s2 = ((String) o2).replaceAll("[\n\t\r ]", "");
                            if (!s1.equals(s2)) {
                                return false;
                            } else {
                                logger.log(Level.WARNING, "WARNING: Strings are not equals with escape symbols and whitespaces");
                                return true;
                            }
                        }
                        logger.log(Level.INFO, "Equals return false");
                        return false;
                    }

                    return true;
                } else {
                    return WeakEqualsHelper.doEquals(o1, o2, fields, ignoreTransient);
                }
            }
            logger.log(Level.WARNING, "UNKNOWN TYPE: {0}", o1.getClass().getName());
        }

        return WeakEqualsHelper.equalsForNull(o1, o2);
    }

    private static CheckNullsResult checkNulls(final Object object, final Object otherObject) {
        if (object == null && otherObject == null) {
            return CheckNullsResult.BOTH_NULLS;
        }
        if (object != null && otherObject != null) {
            return CheckNullsResult.NOT_NULLS;
        }

        return CheckNullsResult.ONE_NULL;
    }

    private static boolean equalsForNull(final Object object, final Object otherObject) {
        return object == null && otherObject == null;
    }

    private static boolean equalsMap(final Map<?, ?> map, final Map<?, ?> otherMap, final Map<String, List<String>> fields, final boolean ignoreTransient) throws NullPointerException {
        return WeakEqualsHelper.equalsCollection(map.entrySet(), otherMap.entrySet(), fields, ignoreTransient);
    }

    private static boolean equalsCollection(final Collection<?> collection, final Collection<?> otherCollection, final Map<String, List<String>> fields, final boolean ignoreTransient)
            throws NullPointerException {
        return WeakEqualsHelper.equalsArray(collection.toArray(), otherCollection.toArray(), fields, ignoreTransient);
    }

    private static boolean equalsArray(final Object[] array, final Object[] otherArray, final Map<String, List<String>> fields, final boolean ignoreTransient)
            throws NullPointerException {
        if (array.length != otherArray.length) {
            errorLog.add("Array or Collection length are not equals[" + array.length + "," + otherArray.length + "]");
            return false;
        }

        final ArrayList<Integer> usedIndexes = new ArrayList<>();
        for (Object currObject : array) {
            boolean found = false;
            for (int k = 0; k < otherArray.length; k++) {
                if (!usedIndexes.contains(k)) {
                    found = WeakEqualsHelper.doObjectEquals(currObject, otherArray[k], fields, ignoreTransient);
                    if (found) {
                        usedIndexes.add(k);
                        break;
                    }
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }

    private static boolean hasEquals(final Class<?> c) {
        final Method[] methods = c.getDeclaredMethods();
        for (final Method m : methods) {
            if (m.getName().equals("equals")
                && m.getParameterTypes().length == 1) {
                return true;
            }
        }
        return false;
    }
}
