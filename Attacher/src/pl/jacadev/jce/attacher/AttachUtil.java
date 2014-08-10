package pl.jacadev.jce.attacher;

/**
 * @author JacaDev
 */


import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.tools.attach.HotSpotAttachProvider;
import sun.tools.attach.WindowsAttachProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JacaDev
 */
public class AttachUtil {

    public static void attachAgentToJVM(String pid, File agent) throws Exception {
        VirtualMachine vm = VirtualMachine.attach(pid);
        vm.loadAgent(agent.getAbsolutePath());
        vm.detach();
    }

    /**
     * @return bytes of stream
     */
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

    /**
     * @return running jvms excluding this and already attached ones.
     */
    public static ObservableList<VM> getAttachableVMs() {
        ObservableList<VM> vms = FXCollections.observableArrayList();
        for (VirtualMachineDescriptor desc : listVMs()) {
            if (!unattachableVMsPids.contains(desc.id()))
                vms.add(new VM(desc.displayName().split(" ")[0], desc.id()));
        }
        return vms;
    }

    public static List<VirtualMachineDescriptor> listVMs(){
        try {
            WindowsAttachProvider provider = (WindowsAttachProvider) WindowsAttachProvider.providers().get(0);
            Method getProcesses = WindowsAttachProvider.class.getDeclaredMethod("listJavaProcesses");
            getProcesses.setAccessible(true);
            return (List<VirtualMachineDescriptor>) getProcesses.invoke(provider);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return pid of this jvm.
     */
    public static String getCurrentPid() {
        String vm = ManagementFactory.getRuntimeMXBean().getName();
        return vm.substring(0, vm.indexOf('@'));
    }

}

