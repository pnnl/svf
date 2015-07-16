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
 * @author Arthur Bleeker
 *
 */
public interface MultiLookupProvider extends LookupProvider {

    /*
     * overridden for additional comments
     */
    /**
     * Adds the object and its complete class and interface hierarchy to the
     * lookup provider. If any of the class or interface types exists already
     * then this will be the new object instance that gets returned by the
     * lookup method. The add method in a multi-lookup provider will add the
     * object instance to each lookup set.
     */
    @Override
    <T> void add(T object);

    /**
     * Adds the object and its complete class and interface hierarchy to the
     * lookup provider. If any of the class or interface types exists already
     * then this will be the new object instance that gets returned by the
     * lookup method. The add method in a multi-lookup provider will add all of
     * the object instances to each lookup set.
     *
     * @param <T>     the object type
     * @param objects The objects to add to this lookup provider.
     *
     * @throws NullPointerException     if the lookups parameter is null
     * @throws IllegalArgumentException if a Class<?> type is passed as an
     *                                  objects collection entry
     */
    <T> void addAll(Collection<T> objects);

    /**
     * Removes all of the objects from the lookup provider.
     *
     * @param <T>     the object type
     * @param objects The collection of objects to remove from this lookup
     *                provider.
     *
     * @return true if any of the objects were found and removed
     *
     * @throws NullPointerException     if the lookup parameter is null
     * @throws IllegalArgumentException if a Class<?> type is passed as an
     *                                  objects collection entry
     */
    <T> boolean removeAll(Collection<T> objects);

    /**
     * Lookup all of the object instances for a class or interface in the lookup
     * provider. All instances of the class or interface type will be returned
     * in a single collection. If no items exist then an empty collection will
     * be returned.
     * <p/>
     * The returned set is a copy and unmodifiable.
     *
     * @param <T>  the object type
     * @param type The object type to lookup in this provider.
     *
     * @return a set of the objects in the lookup that match the key or an empty
     *         set if there are no objects that match
     *
     * @throws NullPointerException if the type argument is null
     */
    <T> Set<T> lookupAll(Class<T> type);

    /**
     * Lookup all of the object instances for a class or interface in the lookup
     * provider. All instances of the class or interface type will be returned
     * in a single collection. If no items exist then an empty collection will
     * be returned. The supplied collection will get cleared prior to adding the
     * unique objects.
     *
     * @param <T>  the object type
     * @param type The object type to lookup in this provider.
     * @param out  the output collection to add the items to
     *
     * @throws NullPointerException if the lookup or out parameter is null
     */
    <T> void lookupAll(Class<T> type, Collection<T> out);
}
