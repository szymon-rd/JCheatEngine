package pl.jacadev.jce.agent.bytecode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    public static String[] getMethodBytecodeMnemonics(String methodName, String methodDesc, Class<?> declaringClass){
        //TODO
        return null;
    }
}
