package pl.jacadev.jce.attacher;

import java.io.InputStream;

/**
 * @author JacaDev
 */
public class Resource {
    private final String path;
    private final byte[] bytes;

    public Resource(String path, byte[] bytes) {
        this.path = path;
        this.bytes = bytes;
    }

    public Resource(String path, InputStream stream) {
        this.path = path;
        this.bytes = AttachUtil.getBytes(stream);
    }

    public String getPath() {
        return path;
    }

    public byte[] getBytes() {
        return bytes;
    }

}
