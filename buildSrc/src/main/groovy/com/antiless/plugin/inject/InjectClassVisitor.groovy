package com.antiless.plugin.inject

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.Opcodes

class InjectClassVisitor extends ClassVisitor {

    InjectClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions)
        mv = new AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {
            @Override
            void visitMethodInsn(int opcode, String owner, String n, String d, boolean itf) {
                println("visiting method " + n)
                super.visitMethodInsn(opcode, owner, n, d, itf)
            }

            @Override
            protected void onMethodEnter() {
                println("onMethodEnter " + name)
//                if ("i" == name) {
//                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
//                    mv.visitLdcInsn("visiting-method")
//                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
//                            "(Ljava/lang/String;)V", false)
//                }
            }

            @Override
            protected void onMethodExit(int opcode) {
                println("onMethodExit " + name)
                if ("yes" == name) {
                    println("entered yes")
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                    mv.visitLdcInsn("visiting-method")
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                            "(Ljava/lang/String;)V", false)
                }
            }
        }
        return mv
    }
}