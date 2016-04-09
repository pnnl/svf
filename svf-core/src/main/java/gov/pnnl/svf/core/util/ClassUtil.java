package gov.pnnl.svf.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Utilities for working with java classes, interfaces, etc.
 *
 * @author Arthur Bleeker
 */
public class ClassUtil {

    private static final Logger logger = Logger.getLogger(ClassUtil.class.getName());

    /**
     * Constructor private for static service
     */
    private ClassUtil() {
        super();
    }

    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     *
     * @param packageName the base package
     *
     * @return the classes
     *
     * @throws IllegalArgumentException if the package can't be located
     */
    public static List<Class<?>> getClasses(final String packageName) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return ClassUtil.getClasses(classLoader, packageName);
    }

    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     *
     * @param classLoader the class loader
     * @param packageName the base package
     *
     * @return the classes
     *
     * @throws IllegalArgumentException if the package can't be located
     */
    public static List<Class<?>> getClasses(final ClassLoader classLoader, final String packageName) {
        if (classLoader == null) {
            throw new NullPointerException("classLoader");
        }
        if (packageName == null) {
            throw new NullPointerException("packageName");
        }
        try {
            // turn into url
            final String path = packageName.replace('.', '/');
            final Enumeration<URL> resources = classLoader.getResources(path);
            // get a list of directories
            final List<File> dirs = new ArrayList<>();
            while (resources.hasMoreElements()) {
                // works for class files
                URL resource = resources.nextElement();
                try {
                    final String filePath = cleanPath(resource);
                    final File file = new File(filePath);
                    dirs.add(file);
                } catch (final RuntimeException ex) {
                    logger.log(Level.WARNING, "Unable to search for resources in {0}", resource.getPath());
                }
                // necessary for finding jars
                int index = resource.toExternalForm().lastIndexOf(resourceName(packageName));
                if (index != -1) {
                    resource = new URL(resource.toExternalForm().substring(0, index));
                }
                try {
                    final String filePath = cleanPath(resource);
                    final File file = new File(filePath);
                    dirs.add(file);
                } catch (final RuntimeException ex) {
                    logger.log(Level.WARNING, "Unable to search for resources in {0}", resource.getPath());
                }
            }
            // build the list of classes
            final List<Class<?>> classes = new ArrayList<>();
            for (final File directory : dirs) {
                try {
                    classes.addAll(getClasses(classLoader, directory, packageName));
                } catch (final RuntimeException ex) {
                    logger.log(Level.FINE, "Unable to load from {0}", directory.getName());
                } catch (final NoClassDefFoundError ex) {
                    logger.log(Level.FINE, "Unable to load from {0}", directory.getName());
                }
            }
            return classes;
        } catch (final IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base
     *                    directory
     *
     * @return The classes
     */
    public static List<Class<?>> getClasses(final File directory, final String packageName) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return getClasses(classLoader, directory, packageName);
    }

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs.
     *
     * @param classLoader the class loader
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base
     *                    directory
     *
     * @return The classes
     */
    public static List<Class<?>> getClasses(final ClassLoader classLoader, final File directory, final String packageName) {
        final List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        if (directory.isDirectory()) {
            // directory
            final File[] files = directory.listFiles();
            if(files == null) {
            	logger.log(Level.INFO, "Unable to read dir {0}", directory);
            	return classes;
            }
            for (final File file : files) {
                final String fileName = file.getName();
                if (file.isDirectory()) {
                    classes.addAll(getClasses(classLoader, file, packageName));
                } else if (fileName.endsWith(".class")) {
                    final String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                    try {
                        classes.add(Class.forName(className, true, classLoader));
                    } catch (final NoClassDefFoundError ex) {
                        logger.log(Level.FINE, "Unable to load class {0}", className);
                    } catch (final ClassNotFoundException ex) {
                        logger.log(Level.FINE, "Unable to find class {0}", className);
                    }
                }
            }
        } else if (directory.isFile()) {
            // jar file
            final String filePackageName = packageName.replaceAll("\\.", "/");
            JarInputStream inputStream = null;
            try {
                inputStream = new JarInputStream(new FileInputStream(directory));
                JarEntry entry;
                while ((entry = inputStream.getNextJarEntry()) != null) {
                    if (entry.getName().startsWith(filePackageName) && entry.getName().endsWith(".class")) {
                        final String className = entry.getName().replaceAll("/", "\\.").substring(0, entry.getName().length() - 6);
                        try {
                            classes.add(Class.forName(className, true, classLoader));
                        } catch (final NoClassDefFoundError ex) {
                            logger.log(Level.FINE, "Unable to load class {0}", className);
                        } catch (final ClassNotFoundException ex) {
                            logger.log(Level.FINE, "Unable to find class {0}", className);
                        }
                    }
                }
            } catch (final IOException ex) {
                logger.log(Level.INFO, "Unable to load jar {0}", directory);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (final IOException ex) {
                        // ignore
                    }
                }
            }
        }
        return classes;
    }

    private static String cleanPath(final URL url) {
        String path = url.getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (final UnsupportedEncodingException ex) {
            // ignore
        }
        if (path.startsWith("jar:")) {
            path = path.substring("jar:".length());
        }
        if (path.startsWith("file:")) {
            path = path.substring("file:".length());
        }
        if (path.endsWith("!/")) {
            path = path.substring(0, path.lastIndexOf("!/")) + "/";
        }
        return path;
    }

    private static String resourceName(final String name) {
        if (name != null) {
            String resourceName = name.replace(".", "/");
            resourceName = resourceName.replace("\\", "/");
            if (resourceName.startsWith("/")) {
                resourceName = resourceName.substring(1);
            }
            return resourceName;
        } else {
            return name;
        }
    }
}
