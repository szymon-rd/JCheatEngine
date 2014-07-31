package pl.jacadev.jinjector.attacher;

/**
 * @author JacaDev
 */


import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * @author JacaDev
 */
public class AttachUtil {

    public static void attachAgentToJVM(String pid, Class<?> agent, Class<?>[] classes, Resource[] resources) throws Exception {
        VirtualMachine vm = VirtualMachine.attach(pid);
        vm.loadAgent(generateAgentJar(agent, classes, resources).getAbsolutePath());
        vm.detach();
    }

    public static File generateAgentJar(Class<?> agent, Class<?>[] classes, Resource[] resources) throws IOException {
        File jarFile = File.createTempFile("agent", ".jar");
        jarFile.deleteOnExit();
        Manifest manifest = new Manifest();
        Attributes mainAttributes = manifest.getMainAttributes();
        mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        mainAttributes.put(new Attributes.Name("Agent-Class"), agent.getName());
        mainAttributes.put(new Attributes.Name("Can-Retransform-Classes"), "true");
        mainAttributes.put(new Attributes.Name("Can-Redefine-Classes"), "true");

        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile), manifest);

        for (Class clazz : classes) {
            putClass(jos, clazz);
        }

        for (Resource res : resources) {
            jos.putNextEntry(new JarEntry(res.getPath()));
            jos.write(res.getBytes());
            jos.closeEntry();
        }

        jos.close();
        return jarFile;
    }

    private static void putClass(JarOutputStream jos, Class clazz) throws IOException {
        for(Class inner : clazz.getDeclaredClasses()){
            putClass(jos, inner);
        }
        String name = unqualify(clazz);
        jos.putNextEntry(new JarEntry(name));
        jos.write(getBytes(clazz.getClassLoader().getResourceAsStream(name)));
        jos.closeEntry();
    }

    private static String unqualify(Class<?> clazz) {
        return clazz.getName().replace('.', '/') + ".class";
    }

    public static byte[] getBytesFromClass(Class<?> clazz) {
        return getBytes(clazz.getClassLoader().getResourceAsStream(clazz.getName().replace('.', '/') + ".class"));
    }

    public static byte[] getBytes(InputStream stream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    private static List<String> unattachableVMsPids = new ArrayList<>();

    static {
        unattachableVMsPids.add(getCurrentPid());
    }

    public static void makeUnattachable(String pid) {
        unattachableVMsPids.add(pid);
    }

    public static void makeAttachable(String pid) {
        unattachableVMsPids.remove(pid);
    }

    public static ObservableList<VM> getAttachableVMs() {
        ObservableList<VM> vms = FXCollections.observableArrayList();
        for (VirtualMachineDescriptor desc : VirtualMachine.list()) {
            if (!unattachableVMsPids.contains(desc.id()))
                vms.add(new VM(desc.displayName().split(" ")[0], desc.id()));
        }
        return vms;
    }

    public static String getCurrentPid() {
        String vm = ManagementFactory.getRuntimeMXBean().getName();
        return vm.substring(0, vm.indexOf('@'));
    }

}

