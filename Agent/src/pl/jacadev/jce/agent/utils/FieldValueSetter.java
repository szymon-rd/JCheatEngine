package pl.jacadev.jce.agent.utils;

import pl.jacadev.jce.agent.Agent;
import sun.reflect.FieldAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author JacaDev
 */
public class FieldValueSetter {
    private static Map<Class<?>, Function<String, ?>> parsers = new HashMap<>();

    static {
        parsers.put(long.class, Long::valueOf);
        parsers.put(int.class, Integer::valueOf);
        parsers.put(short.class, Short::valueOf);
        parsers.put(byte.class, Byte::valueOf);
        parsers.put(double.class, Double::valueOf);
        parsers.put(float.class, Float::valueOf);
        parsers.put(boolean.class, Boolean::valueOf);
        parsers.put(char.class, a -> a.charAt(0));
        parsers.put(String.class, String::valueOf);
        parsers.put(Long.class, Long::valueOf);
        parsers.put(Integer.class, Integer::valueOf);
        parsers.put(Short.class, Short::valueOf);
        parsers.put(Byte.class, Byte::valueOf);
        parsers.put(Double.class, Double::valueOf);
        parsers.put(Float.class, Float::valueOf);
        parsers.put(Boolean.class, Boolean::valueOf);
        parsers.put(Character.class, a -> a.charAt(0));
    }

    public static void setField(Field field, String value) throws NoSuchFieldException {
        if ((field.getModifiers() & Modifier.STATIC) != 0)
            setField(null, field, value);
        else throw new Error("field needs to be static");
    }

    public static void setField(Object owner, Field field, String value) throws NoSuchFieldException {
        if (isParsable(field.getType()))
            setField(owner, field, parse(value, field.getType()));
        else throw new UnsupportedOperationException("field needs to be primitive or String");
    }

    public static void setField(Object owner, Field field, Object value) throws NoSuchFieldException {
        field.setAccessible(true);
        if ((field.getModifiers() & Modifier.FINAL) != 0) accessFinal(field);
        try {
            field.set(owner, value);
        } catch (IllegalAccessException e) {
            Agent.handleException(e);
        }
    }

    private static void accessFinal(Field field) {
        try {
            Field f = Field.class.getDeclaredField("modifiers");
            f.setAccessible(true);
            f.set(field, field.getModifiers() & ~Modifier.FINAL);
            Method getacc = Field.class.getDeclaredMethod("getFieldAccessor", Object.class);
            getacc.setAccessible(true);
            FieldAccessor accessor = (FieldAccessor) getacc.invoke(field, field);
            Field readOnly = accessor.getClass().getSuperclass().getDeclaredField("isReadOnly");
            readOnly.setAccessible(true);
            readOnly.set(accessor, false);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean isParsable(Class type) {
        return parsers.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public static Object parse(String value, Class type) {
        try {
            return parsers.getOrDefault(type,
                    string -> {
                        throw new Error("Unparsable type: " + type.getSimpleName());
                    }).apply(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


}
