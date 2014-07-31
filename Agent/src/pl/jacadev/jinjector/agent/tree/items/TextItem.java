package pl.jacadev.jinjector.agent.tree.items;

/**
 * @author JacaDev
 */
public class TextItem extends Item {
    private String text;

    public TextItem(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public void handleClick() {


    }

    @Override
    public void handleBranchExpansion() {

    }
}
