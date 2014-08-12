package pl.jacadev.jce.agent.tree.nodes;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;

/**
 * @author JacaDev
 */
public abstract class Node extends TreeItem<Node> {

    public Node() {
        addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> handleBranchExpansion());
        setValue(this);
    }

    /**
     * The fastest way to refresh values without access to TreeCell.
     */
    void refresh() {
        boolean expanded = isExpanded();
        setExpanded(!expanded);
        setExpanded(expanded);
    }

    void handleClick(){}
    void handleBranchExpansion(){}
    void setupMenu(ContextMenu menu){}
}
