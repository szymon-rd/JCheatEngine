package pl.jacadev.jinjector.agent.utils.mnemonics;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.function.Function;

/**
 * @author JacaDev
 */
public class Mnemonics {
    @SuppressWarnings("unchecked")
    static Function<AbstractInsnNode, String> mnemonics[][] =
            new Function[][]{
                    new Function[]{a -> "nop"},  /* 0*/
                    new Function[]{a -> "aconst_null"},  /* 1*/
                    new Function[]{a -> "iconst_m1"},    /* 2*/
                    new Function[]{a -> "iconst_0"},     /* 3*/
                    new Function[]{a -> "iconst_1"},     /* 4*/
                    new Function[]{a -> "iconst_2"},     /* 5*/
                    new Function[]{a -> "iconst_3"},     /* 6*/
                    new Function[]{a -> "iconst_4"},     /* 7*/
                    new Function[]{a -> "iconst_5"},     /* 8*/
                    new Function[]{a -> "lconst_0"},     /* 9*/
                    new Function[]{a -> "lconst_1"},     /* 10*/
                    new Function[]{a -> "fconst_0"},     /* 11*/
                    new Function[]{a -> "fconst_1"},     /* 12*/
                    new Function[]{a -> "fconst_2"},     /* 13*/
                    new Function[]{a -> "dconst_0"},     /* 14*/
                    new Function[]{a -> "dconst_1"},     /* 15*/
                    new Function[]{a -> "bipush"},       /* 16*/
                    new Function[]{a -> "sipush"},       /* 17*/
                    new Function[]{a -> "ldc"},          /* 18*/
                    new Function[]{a -> "ldc_w"},        /* 19*/
                    new Function[]{a -> "ldc2_w"},       /* 20*/
                    new Function[]{a -> "iload",},       /* 21*/
                    new Function[]{a -> "lload"},        /* 22*/
                    new Function[]{a -> "fload"},        /* 23*/
                    new Function[]{a -> "dload"},        /* 24*/
                    new Function[]{a -> "aload"},        /* 25*/
                    new Function[]{a -> "iload_0"},      /* 26*/
                    new Function[]{a -> "iload_1"},      /* 27*/
                    new Function[]{a -> "iload_2"},      /* 28*/
                    new Function[]{a -> "iload_3"},      /* 29*/
                    new Function[]{a -> "lload_0"},      /* 30*/
                    new Function[]{a -> "lload_1"},      /* 31*/
                    new Function[]{a -> "lload_2"},      /* 32*/
                    new Function[]{a -> "lload_3"},      /* 33*/
                    new Function[]{a -> "fload_0"},      /* 34*/
                    new Function[]{a -> "fload_1"},      /* 35*/
                    new Function[]{a -> "fload_2"},      /* 36*/
                    new Function[]{a -> "fload_3"},      /* 37*/
                    new Function[]{a -> "dload_0"},      /* 38*/
                    new Function[]{a -> "dload_1"},      /* 39*/
                    new Function[]{a -> "dload_2"},      /* 40*/
                    new Function[]{a -> "dload_3"},      /* 41*/
                    new Function[]{a -> "aload_0"},      /* 42*/
                    new Function[]{a -> "aload_1"},      /* 43*/
                    new Function[]{a -> "aload_2"},      /* 44*/
                    new Function[]{a -> "aload_3"},      /* 45*/
                    new Function[]{a -> "iaload"},       /* 46*/
                    new Function[]{a -> "laload"},       /* 47*/
                    new Function[]{a -> "faload"},       /* 48*/
                    new Function[]{a -> "daload"},       /* 49*/
                    new Function[]{a -> "aaload"},       /* 50*/
                    new Function[]{a -> "baload"},       /* 51*/
                    new Function[]{a -> "caload"},       /* 52*/
                    new Function[]{a -> "saload"},       /* 53*/
                    new Function[]{a -> "istore"},       /* 54*/
                    new Function[]{a -> "lstore"},       /* 55*/
                    new Function[]{a -> "fstore"},       /* 56*/
                    new Function[]{a -> "dstore"},       /* 57*/
                    new Function[]{a -> "astore"},       /* 58*/
                    new Function[]{a -> "istore_0"},     /* 59*/
                    new Function[]{a -> "istore_1"},     /* 60*/
                    new Function[]{a -> "istore_2"},     /* 61*/
                    new Function[]{a -> "istore_3"},     /* 62*/
                    new Function[]{a -> "lstore_0"},     /* 63*/
                    new Function[]{a -> "lstore_1"},     /* 64*/
                    new Function[]{a -> "lstore_2"},     /* 65*/
                    new Function[]{a -> "lstore_3"},     /* 66*/
                    new Function[]{a -> "fstore_0"},     /* 67*/
                    new Function[]{a -> "fstore_1"},     /* 68*/
                    new Function[]{a -> "fstore_2"},     /* 69*/
                    new Function[]{a -> "fstore_3"},     /* 70*/
                    new Function[]{a -> "dstore_0"},     /* 71*/
                    new Function[]{a -> "dstore_1"},     /* 72*/
                    new Function[]{a -> "dstore_2"},     /* 73*/
                    new Function[]{a -> "dstore_3"},     /* 74*/
                    new Function[]{a -> "astore_0"},     /* 75*/
                    new Function[]{a -> "astore_1"},     /* 76*/
                    new Function[]{a -> "astore_2"},     /* 77*/
                    new Function[]{a -> "astore_3"},     /* 78*/
                    new Function[]{a -> "iastore"},      /* 79*/
                    new Function[]{a -> "lastore"},      /* 80*/
                    new Function[]{a -> "fastore"},      /* 81*/
                    new Function[]{a -> "dastore"},      /* 82*/
                    new Function[]{a -> "aastore"},      /* 83*/
                    new Function[]{a -> "bastore"},      /* 84*/
                    new Function[]{a -> "castore"},      /* 85*/
                    new Function[]{a -> "sastore"},      /* 86*/
                    new Function[]{a -> "pop"},         /* 87*/
                    new Function[]{a -> "pop2"},        /* 88*/
                    new Function[]{a -> "dup"},         /* 89*/
                    new Function[]{a -> "dup_x1"},       /* 90*/
                    new Function[]{a -> "dup_x2"},       /* 91*/
                    new Function[]{a -> "dup2"},        /* 92*/
                    new Function[]{a -> "dup2_x1"},      /* 93*/
                    new Function[]{a -> "dup2_x2"},      /* 94*/
                    new Function[]{a -> "swap"},        /* 95*/
                    new Function[]{a -> "iput"}, /* 96*/
                    new Function[]{a -> "lput"}, /* 97*/
                    new Function[]{a -> "fput"}, /* 98*/
                    new Function[]{a -> "dput"}, /* 99*/
                    new Function[]{a -> "isub"}, /* 100*/
                    new Function[]{a -> "lsub"}, /* 101*/
                    new Function[]{a -> "fsub"}, /* 102*/
                    new Function[]{a -> "dsub"}, /* 103*/
                    new Function[]{a -> "imul"}, /* 104*/
                    new Function[]{a -> "lmul"}, /* 105*/
                    new Function[]{a -> "fmul"}, /* 106*/
                    new Function[]{a -> "dmul"}, /* 107*/
                    new Function[]{a -> "idiv"}, /* 108*/
                    new Function[]{a -> "ldiv"}, /* 109*/
                    new Function[]{a -> "fdiv"}, /* 110*/
                    new Function[]{a -> "ddiv"}, /* 111*/
                    new Function[]{a -> "irem"}, /* 112*/
                    new Function[]{a -> "lrem"}, /* 113*/
                    new Function[]{a -> "frem"}, /* 114*/
                    new Function[]{a -> "drem"}, /* 115*/
                    new Function[]{a -> "ineg"}, /* 116*/
                    new Function[]{a -> "lneg"}, /* 117*/
                    new Function[]{a -> "fneg"}, /* 118*/
                    new Function[]{a -> "dneg"}, /* 119*/
                    new Function[]{a -> "ishl"}, /* 120*/
                    new Function[]{a -> "lshl"}, /* 121*/
                    new Function[]{a -> "ishr"}, /* 122*/
                    new Function[]{a -> "lshr"}, /* 123*/
                    new Function[]{a -> "iushr"},/* 124*/
                    new Function[]{a -> "lushr"},/* 125*/
                    new Function[]{a -> "iand"}, /* 126*/
                    new Function[]{a -> "land"}, /* 127*/
                    new Function[]{a -> "ior"},  /* 128*/
                    new Function[]{a -> "lor"},  /* 129*/
                    new Function[]{a -> "ixor"}, /* 130*/
                    new Function[]{a -> "lxor"}, /* 131*/
                    new Function[]{a -> "iinc"}, /* 132*/
                    new Function[]{a -> "i2l"},  /* 133*/
                    new Function[]{a -> "i2f"},  /* 134*/
                    new Function[]{a -> "i2d"},  /* 135*/
                    new Function[]{a -> "l2i"},  /* 136*/
                    new Function[]{a -> "l2f"},  /* 137*/
                    new Function[]{a -> "l2d"},  /* 138*/
                    new Function[]{a -> "f2i"},  /* 139*/
                    new Function[]{a -> "f2l"},  /* 140*/
                    new Function[]{a -> "f2d"},  /* 141*/
                    new Function[]{a -> "d2i"},  /* 142*/
                    new Function[]{a -> "d2l"},  /* 143*/
                    new Function[]{a -> "d2f"},  /* 144*/
                    new Function[]{a -> "i2b"},  /* 145*/
                    new Function[]{a -> "i2c"},  /* 146*/
                    new Function[]{a -> "i2s"},  /* 147*/
                    new Function[]{a -> "lcmp"}, /* 148*/
                    new Function[]{a -> "fcmpl"},        /* 149*/
                    new Function[]{a -> "fcmpg"},        /* 150*/
                    new Function[]{a -> "dcmpl"},        /* 151*/
                    new Function[]{a -> "dcmpg"},        /* 152*/
                    new Function[]{a -> "ifeq"}, /* 153*/
                    new Function[]{a -> "ifne"}, /* 154*/
                    new Function[]{a -> "iflt"}, /* 155*/
                    new Function[]{a -> "ifge"}, /* 156*/
                    new Function[]{a -> "ifgt"}, /* 157*/
                    new Function[]{a -> "ifle"}, /* 158*/
                    new Function[]{a -> "if_icmpeq"},    /* 159*/
                    new Function[]{a -> "if_icmpne"},    /* 160*/
                    new Function[]{a -> "if_icmplt"},    /* 161*/
                    new Function[]{a -> "if_icmpge"},    /* 162*/
                    new Function[]{a -> "if_icmpgt"},    /* 163*/
                    new Function[]{a -> "if_icmple"},    /* 164*/
                    new Function[]{a -> "if_acmpeq"},    /* 165*/
                    new Function[]{a -> "if_acmpne"},    /* 166*/
                    new Function[]{a -> "goto"}, /* 167*/
                    new Function[]{a -> "jsr"},  /* 168*/
                    new Function[]{a -> "ret"},  /* 169*/
                    new Function[]{a -> "tableswitch"},  /* 170*/
                    new Function[]{a -> "lookupswitch"}, /* 171*/
                    new Function[]{a -> "ireturn"},      /* 172*/
                    new Function[]{a -> "lreturn"},      /* 173*/
                    new Function[]{a -> "freturn"},      /* 174*/
                    new Function[]{a -> "dreturn"},      /* 175*/
                    new Function[]{a -> "areturn"},      /* 176*/
                    new Function[]{a -> "return"},       /* 177*/
                    new Function[]{a -> "getstatic"},    /* 178*/
                    new Function[]{a -> "putstatic"},    /* 179*/
                    new Function[]{a -> "getfield"},     /* 180*/
                    new Function[]{a -> "putfield"},     /* 181*/
                    new Function[]{a -> "invokevirtual"},        /* 182*/
                    new Function[]{a -> "invokespecial"},        /* 183*/
                    new Function[]{a -> "invokestatic"}, /* 184*/
                    new Function[]{a -> "invokeinterface"},      /* 185*/
                    null,
                    new Function[]{a -> "new"},  /* 187*/
                    new Function[]{a -> "newarray"},     /* 188*/
                    new Function[]{a -> "anewarray"},    /* 189*/
                    new Function[]{a -> "arraylength"},  /* 190*/
                    new Function[]{a -> "athrow"},       /* 191*/
                    new Function[]{a -> "checkcast"},    /* 192*/
                    new Function[]{a -> "instanceof"},   /* 193*/
                    new Function[]{a -> "monitorenter"}, /* 194*/
                    new Function[]{a -> "monitorexit"},  /* 195*/
                    new Function[]{a -> "wide"},         /* 196*/
                    new Function[]{a -> "multianewarray"},/* 197*/
                    new Function[]{a -> "ifnull"},       /* 198*/
                    new Function[]{a -> "ifnonnull"},    /* 199*/
                    new Function[]{a -> "goto_w"},       /* 200*/
                    new Function[]{a -> "jsr_w"},         /* 201*/
            };

    public static String getMnemonics(InsnList instructions) {
        StringBuilder builder = new StringBuilder();
        for (AbstractInsnNode insn : instructions.toArray()) {
            builder.append(getMnemonic(insn) + "\n");
        }
        return null;
    }

    public static String getMnemonic(AbstractInsnNode instruction) {
        switch (instruction.getOpcode()) {
        }
        return null;
    }

}
