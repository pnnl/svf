package gov.pnnl.svf.core.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic tree node for implementing a basic tree. This class is not thread
 * safe.
 *
 * @author Arthur Bleeker
 * @param <T> The type of value contained in this node
 * @param <C> The type of collection for the children nodes
 * @param <N> The type of node
 */
public abstract class AbstractTreeNode<T, C extends Collection<N>, N extends AbstractTreeNode<T, C, N>> implements Serializable, Iterable<N> {

    private static final long serialVersionUID = 1L;

    protected N parent;
    protected C children;
    protected T value;

    /**
     * Constructor.
     *
     * @param value the value
     */
    protected AbstractTreeNode(final T value) {
        this.value = value;
    }

    /**
     * The current root of the tree for this node.
     *
     * @return the root
     */
    @SuppressWarnings("unchecked")
    public N getRoot() {
        if (parent != null) {
            return parent.getRoot();
        }
        return (N) this;
    }

    /**
     * The parent of this node is automatically set and unset when adding or
     * removing children.
     *
     * @return the parent
     */
    public N getParent() {
        return parent;
    }

    /**
     * The children for this node. Will not be null.
     *
     * @return the children
     */
    public C getChildren() {
        return children;
    }

    /**
     * The new children for this node. Existing children will have their parent
     * field unset. The new children will have their parent field set to this
     * node. The children collection should not be null or contain null entries.
     *
     * @param children the children
     */
    public void setChildren(final C children) {
        if (children == null) {
            throw new NullPointerException("children");
        }
        if (this.children != null) {
            for (final N child : this.children) {
                child.parent = null;
            }
        }
        this.children = children;
        for (final N child : children) {
            child.parent = (N) this;
        }
    }

    /**
     * The value for this node.
     *
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * The value for this node.
     *
     * @param value the value
     */
    public void setValue(final T value) {
        this.value = value;
    }

    /**
     * Tests whether this tree node has a parent.
     *
     * @return true if this tree node is a root
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Tests whether this tree node has children.
     *
     * @return true if this tree node is a leaf
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public Iterator<N> iterator() {
        return new IteratorImpl();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.children != null ? this.children.hashCode() : 0);
        hash = 31 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractTreeNode<?, ?, ?> other = (AbstractTreeNode<?, ?, ?>) obj;
        if (this.children != other.children && (this.children == null || !this.children.equals(other.children))) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AbstractNode{" + "value=" + value + '}';
    }

    protected class IteratorImpl implements Iterator<N> {

        private Iterator<N> children;
        private Iterator<N> child;

        protected IteratorImpl() {
        }

        @Override
        public boolean hasNext() {
            return (children == null && child == null) || (children != null && children.hasNext() || child != null && child.hasNext());
        }

        @Override
        @SuppressWarnings("unchecked")
        public N next() {
            // starting condition
            if (children == null && child == null) {
                children = AbstractTreeNode.this.children.iterator();
                child = children.hasNext() ? children.next().iterator() : Collections.<N>emptyList().iterator();
                // return this
                return (N) AbstractTreeNode.this;
            }
            // check for next step
            if (child.hasNext()) {
                return child.next();
            }
            // check for next
            if (children.hasNext()) {
                child = children.next().iterator();
                return child.next();
            }
            // no more
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

}
