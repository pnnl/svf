package gov.pnnl.svf.core.lookup;

import java.util.Collection;
import java.util.Set;

/**
 * Interface used for providing a container for classes that are added and
 * looked up according to their class, superclass, and interface types. This can
 * be used as a form of inversion of control or more specifically a type of
 * dependency injection called service lookup. It can also be used for dynamic
 * polymorphism of objects.
 *
 * @author Amelia Bleeker
 *
 */
public interface LookupProvider {

    /**
     * Adds the object and its complete class and interface hierarchy to the
     * lookup provider. If any of the class or interface types exists already
     * then this will be the new object instance that gets returned by the
     * lookup method.
     *
     * @param <T>    the object type
     * @param object The object to add to this lookup provider.
     *
     * @throws NullPointerException     if the lookup parameter is null
     * @throws IllegalArgumentException if a {@code Class<?>} type is passed as
     *                                  the object argument
     */
    <T> void add(T object);

    /**
     * Lookup a class in the lookup provider. A single object instance will be
     * returned. If a lookup contains more than one instance of the class then
     * the last one to be added will be returned.
     *
     * @param <T>  the object type
     * @param type The object type to lookup in this provider.
     *
     * @return the object or null if the supplied class or interface type does
     *         not exist in the lookup
     *
     * @throws NullPointerException if the type parameter is null
     */
    <T> T lookup(Class<? extends T> type);

    /**
     * Removes this object instance from the lookup provider.
     *
     * @param <T>    the object type
     * @param object The object instance to remove from this lookup provider.
     *
     * @return true if the object instance was found and removed
     *
     * @throws NullPointerException     if the object parameter is null
     * @throws IllegalArgumentException if a {@code Class<?>} type is passed as
     *                                  the object argument
     */
    <T> boolean remove(T object);

    /**
     * Get a collection of all of the unique object instances contained in this
     * lookup. This method will return an empty set if the lookup is empty.
     *
     * @return a collection of all of the unique object instances
     */
    Set<Object> lookupAll();

    /**
     * Get a collection of all of the unique objects contained in this lookup.
     * The supplied collection will not get cleared prior to adding the unique
     * objects.
     *
     * @param out the output collection to add the items to
     *
     * @throws NullPointerException if the out argument is null
     */
    void lookupAll(Collection<Object> out);

    /**
     * Clears the lookup.
     */
    void clear();
}
