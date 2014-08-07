package pl.jacadev.jce.agent.bytecode.mnemonics;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

/**
 * @author JacaDev
 */
public class Mnemonics {

    public String getClassMnemonics(ClassNode node) {
        StringWriter stringWriter = new StringWriter();
        node.accept(new TraceClassVisitor(new PrintWriter(stringWriter)));
        return stringWriter.toString();
    }

    public String getMethodMnemonics(ClassNode classNode, String method) {
        String[] classMnemonics = getClassMnemonics(classNode).split("\n");
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
