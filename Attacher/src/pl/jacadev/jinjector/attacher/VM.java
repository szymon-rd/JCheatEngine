package pl.jacadev.jinjector.attacher;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author JacaDev
 */
public class VM {
    private final SimpleStringProperty name;
    private final SimpleStringProperty pid;

    public VM(String name, String pid) {
        this.name = new SimpleStringProperty(name);
        this.pid = new SimpleStringProperty(pid);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPid(String pid) {
        this.pid.set(pid);
    }

    public String getName() {
        return name.get();
    }

    public String getPid() {
        return pid.get();
    }

}
