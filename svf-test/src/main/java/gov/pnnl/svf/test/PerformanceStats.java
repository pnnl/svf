/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.pnnl.svf.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerformanceStats {

    private static final Logger logger = Logger.getLogger(PerformanceStats.class.getName());
    private static final String FILE_NAME = "performance.properties";

    /**
     * Constructor private for static util class
     */
    private PerformanceStats() {
    }

    /**
     * Write the performance statistic to file.
     *
     * @param function     the class and method name to be used as a key
     * @param iterations   the number of iterations
     * @param milliseconds the amount of time it took
     */
    public static synchronized void write(final String function, final int iterations, final long milliseconds) {
        final Properties properties = new Properties();
        // load existing
        final File file = new File(FILE_NAME);
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            properties.load(input);
        } catch (final IOException ex) {
            logger.log(Level.WARNING, "Unable to read performance statistics from file.", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException ex) {
                    logger.log(Level.WARNING, "Unable to close input stream.", ex);
                }
            }
        }
        // determine min and max
        final String r = ".*\\(([\\d.]+), ([\\d.]+)\\).*";
        final Pattern p = Pattern.compile(r);
        Matcher m = null;
        boolean extract = false;
        if (file.exists()) {
            try {
                m = p.matcher(properties.getProperty(function, ""));
                extract = m.matches();
            } catch (final IllegalStateException ex) {
                logger.log(Level.WARNING, "Unable to retrieve the previous time.", ex);
            }
        }
        final double time = milliseconds / 1000.0;
        Double pMin = time;
        if (file.exists() && extract) {
            try {
                pMin = Double.valueOf(m.group(1));
            } catch (final NullPointerException | IllegalStateException | NumberFormatException ex) {
                logger.log(Level.WARNING, "Unable to parse the previous minimum time.", ex);
            }
        }
        final double min = Math.min(time, pMin);
        Double pMax = time;
        if (file.exists() && extract) {
            try {
                pMax = Double.valueOf(m.group(2));
            } catch (final NullPointerException | IllegalStateException | NumberFormatException ex) {
                logger.log(Level.WARNING, "Unable to parse the previous maximum time.", ex);
            }
        }
        final double max = Math.max(time, pMax);
        // build message
        final String message = String.format(
                "Performance of function %s with %,d iterations took %.2f (%.2f, %.2f) seconds.",
                function, iterations, time, min, max);
        properties.setProperty(function, message);
        // save
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(FILE_NAME);
            properties.store(output, "Performance Statistics");
        } catch (final IOException ex) {
            logger.log(Level.WARNING, "Unable to write performance statistics to file.", ex);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (final IOException ex) {
                    logger.log(Level.WARNING, "Unable to close input stream.", ex);
                }
            }
        }
        // report changes
        if (time < pMin) {
            logger.log(Level.INFO, "New minimum time recorded is less than previous value of {0}. {1}", new Object[]{pMin, message});
        }
        if (time > pMax) {
            logger.log(Level.INFO, "New maximum time recorded is greater than previous value of {0}. {1}", new Object[]{pMax, message});
        }
    }

}
