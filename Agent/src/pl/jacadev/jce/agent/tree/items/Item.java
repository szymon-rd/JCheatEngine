package pl.jacadev.jce.agent.tree.items;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;

/**
 * @author JacaDev
 */
public abstract class Item extends TreeItem<Item> {

    public Item() {
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
