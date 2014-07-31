package pl.jacadev.jinjector.agent.tree.items;

import javafx.scene.control.TreeItem;

/**
 * @author JacaDev
 */
public abstract class Item extends TreeItem<Item> {

    public Item() {
        addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> handleBranchExpansion());
        setValue(this);
    }
    public abstract void handleClick();
    public abstract void handleBranchExpansion();
}
