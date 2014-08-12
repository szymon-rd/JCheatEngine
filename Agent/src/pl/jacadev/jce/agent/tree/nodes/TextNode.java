package pl.jacadev.jce.agent.tree.nodes;

/**
 * @author JacaDev
 */
public class TextNode extends Node {
    private String text;

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
