package gov.pnnl.svf.core.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A generic tree node for implementing a basic list tree.
 *
 * @author Amelia Bleeker
 * @param <T> The type of value contained in this node
 */
public class ListTreeNode<T> extends AbstractTreeNode<T, List<ListTreeNode<T>>, ListTreeNode<T>> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     *
     * @param value the value
     */
    public ListTreeNode(final T value) {
        super(value);
        children = Collections.emptyList();
    }

    /**
     * Constructor used to copy this node and all children. This will copy the
     * tree structure but reuse references for values. The copy will become a
     * root note regardless of whether the original node is or not.
     *
     * @param copy the node to copy
     */
    public ListTreeNode(final ListTreeNode<T> copy) {
        super(copy.getValue());
        parent = copy.parent;
        final List<ListTreeNode<T>> children = new ArrayList<>(copy.getChildren().size());
        for (final ListTreeNode<T> child : copy.getChildren()) {
            children.add(new ListTreeNode<>(child));
        }
        this.children = children;
    }

}
