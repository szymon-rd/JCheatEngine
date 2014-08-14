package pl.jacadev.jce.agent.bytecode;

import org.objectweb.asm.tree.ClassNode;
import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.bytecode.mnemonics.Mnemonics;
import pl.jacadev.jce.agent.bytecode.transformations.ClassBytecodeGetter;

import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by oem on 12.08.14.
 */
public class MethodBytecodeGetter {
    private static Map<Class<?>, Character> descriptors = new HashMap<>();
    static{
        descriptors.put(void.class, 'V');
        descriptors.put(long.class, 'J');
        descriptors.put(int.class, 'I');
        descriptors.put(short.class, 'S');
        descriptors.put(byte.class, 'B');
        descriptors.put(double.class, 'D');
        descriptors.put(float.class, 'F');
        descriptors.put(boolean.class, 'Z');
        descriptors.put(char.class, 'C');
    }

    private static String getDesc(Class<?> type){
        String desc = String.valueOf(descriptors.getOrDefault(type, null));
        if(desc == null){
            desc = "";
            while (type.isArray()){
                type = type.getComponentType();
                desc = "[" + desc;
            }
            desc += descriptors.getOrDefault(type, 'L');
            if(desc.endsWith("L")){
                desc += String.format("%s;", type.getCanonicalName());
            }
        }
        return desc;
    }

    public static String getDesc(Method m){
        StringBuilder desc = new StringBuilder();
        for(Class<?> type : m.getParameterTypes()){
            desc.append(getDesc(type));
        }
        return String.format("(%s)%s", desc, getDesc(m.getReturnType()));
    }
    public static String getMethodMnemonics(Method method){
        try {
            ClassNode classNode = ClassBytecodeGetter.toASMNode(method.getDeclaringClass());
            String m = method.getName() + getDesc(method);
            return getMethodMnemonics(classNode, m);
        } catch (UnmodifiableClassException | ClassNotFoundException e) {
            Agent.showError(e.toString());
        }
        return null;
    }
    private static String getMethodMnemonics(ClassNode classNode, String method) {
        String[] classMnemonics = Mnemonics.getClassMnemonics(classNode).split("\n");
        Pattern methodPattern = Pattern.compile('*' + method);

        boolean isInsideMethod = false;
        StringBuilder methodMnemonics = new StringBuilder();
        for (String mnemonic : classMnemonics) {
            if (!isInsideMethod
                    && methodPattern.matcher(mnemonic).matches()) {
                isInsideMethod = true;
            }
            if (isInsideMethod) {
                if (mnemonic.length() > 0 && !Character.isUpperCase(mnemonic.charAt(0)))
                    break;
                methodMnemonics.append(mnemonic);
            }
        }
        return methodMnemonics.toString();
    }
}
