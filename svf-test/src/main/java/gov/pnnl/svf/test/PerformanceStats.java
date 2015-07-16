/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.pnnl.svf.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        FileInputStream input = null;
        try {
            input = new FileInputStream(FILE_NAME);
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
        // build message
        final String message = String.format(
                "Performance of function %s with %,d iterations took %.2f seconds.",
                function, iterations, milliseconds / 1000.0);
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
    }

}
