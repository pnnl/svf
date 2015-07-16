package gov.pnnl.svf.core.service;

/**
 * Token used with the busy service. This object encapsulates state for busy
 * method calls.
 *
 * @author Arthur Bleeker
 */
public class BusyTokenImpl implements BusyToken {

    private final long uuid;
    private final long timestamp;

    /**
     * Constructor
     *
     * @param uuid the uuid for this BusyToken
     */
    protected BusyTokenImpl(final long uuid) {
        this.uuid = uuid;
        timestamp = System.currentTimeMillis();
    }

    /**
     * Copy Constructor
     *
     * @param busyToken busyToken to copy
     */
    protected BusyTokenImpl(final BusyTokenImpl busyToken) {
        if (busyToken == null) {
            throw new NullPointerException("busyToken");
        }
        uuid = busyToken.getUuid();
        timestamp = busyToken.getTimestamp();
    }

    /**
     * @return the uuid
     */
    public long getUuid() {
        return uuid;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (uuid ^ (uuid >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BusyTokenImpl other = (BusyTokenImpl) obj;
        if (uuid != other.uuid) {
            return false;
        }
        return true;
    }
}
