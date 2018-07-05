package gov.pnnl.svf.pool;

/**
 * This interface manages all of the actor pools. The actor pool manager should
 * be added to the scene so that it gets managed and cleaned up.
 *
 * @author Amelia Bleeker
 */
public interface ActorPoolManager extends PoolManager<ActorPool<?>> {
}
