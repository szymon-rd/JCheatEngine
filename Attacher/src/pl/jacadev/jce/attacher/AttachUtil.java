package pl.jacadev.jce.attacher;

/**
 * @author JacaDev
 */


import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.tools.attach.WindowsAttachProvider;

import java.io.File;
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
    public static ObservableList<VM> getAttachableVMs(boolean onlyProvided) {
        if(onlyProvided){
            return FXCollections.observableArrayList(VirtualMachine.list().stream()
                    .map(desc -> new VM(desc.displayName(), desc.id()))
                    .filter(vm -> !unattachableVMsPids.contains(vm.getPid()))
                    .toArray(VM[]::new));
        }else{
        ObservableList<VM> vms = FXCollections.observableArrayList();
        for (VirtualMachineDescriptor desc : listVMs()) {
            if (!unattachableVMsPids.contains(desc.id()))
                vms.add(new VM(desc.displayName().split(" ")[0], desc.id()));
        }
        return vms;
        }
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
     * @return pid of current jvm.
     */
    public static String getCurrentPid() {
        String vm = ManagementFactory.getRuntimeMXBean().getName();
        return vm.substring(0, vm.indexOf('@'));
    }

}

