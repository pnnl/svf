package gov.pnnl.svf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility for working with strings.
 *
 * @author Arthur Bleeker
 */
public class StringUtil {

    /**
     * Constructor is private to prevent instantiation of a static helper class.
     */
    private StringUtil() {
        super();
    }

    /**
     * Get a human readable name for an object.
     *
     * @param object reference to the object
     *
     * @return a human readable name
     */
    public static String getHumanReadableName(final Object object) {
        if (object == null || object.getClass().getName() == null) {
            return "";
        }
        final String[] parts = object.getClass().getName().split("\\.");
        String name = parts[parts.length - 1];
        final int index = name.lastIndexOf('$');
        if (index != -1) {
            name = name.substring(0, index);
        }
        name = name.replaceAll(
                String.format("%s|%s|%s",
                              "(?<=[A-Z])(?=[A-Z][a-z])",
                              "(?<=[^A-Z])(?=[A-Z])",
                              "(?<=[A-Za-z])(?=[^A-Za-z])"),
                " ");
        return name;
    }

    /**
     * Converts an input stream to a string.
     *
     * @param in the input stream that contains UTF-8 text
     *
     * @return the string representation of the input stream
     *
     * @throws IllegalArgumentException if in argument is null
     * @throws IOException
     */
    public static String streamToString(final InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("in");
        }
        final StringBuilder sb = new StringBuilder();
        try (final InputStreamReader isReader = new InputStreamReader(in, "UTF-8");
             final BufferedReader reader = new BufferedReader(isReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
