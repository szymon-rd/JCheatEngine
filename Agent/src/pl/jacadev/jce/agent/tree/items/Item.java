package pl.jacadev.jce.agent.tree.items;

import javafx.scene.control.TreeItem;

/**
 * @author JacaDev
 */
public abstract class Item extends TreeItem<Item> {

    public Item() {
        addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> handleBranchExpansion());
        setValue(this);
    }

    abstract void handleClick();
    abstract void handleBranchExpansion();
}
