package com.antiless.plugin.inject

import com.android.build.api.transform.*
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project
import org.gradle.internal.impldep.org.eclipse.jgit.annotations.NonNull
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class InjectTransform extends Transform {
    private Project project

    public InjectTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "InjectTransform"
    }

    @Override
    Set<ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(
            @NonNull TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        println("transforming")
        transformInvocation.getInputs().each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                println("directory " + directoryInput.file.absolutePath)
                injectDirectory(directoryInput.file)

                def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                    directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            input.jarInputs.each { JarInput jarInput ->
                println("jar " + jarInput.name)
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5(jarInput.file.absolutePath)
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }

                def dest =  transformInvocation.outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
    }

    void injectDirectory(File directoryFile) {
        println("inject Directory " + directoryFile.name)
        if (!directoryFile.isDirectory()) return

        println("inject ")
        directoryFile.eachFileRecurse { File file ->
            String name = file.name
            println("---")
            println("recurse " + file.name)
            println("recurse " + file.parentFile.absolutePath)
            if (name.endsWith(".class") && !name.startsWith("R.\$") &&
                !"R.class".equals(name) && !"BuildConfig.class".equals(name)
                && "MainActivity.class".equals(name)) {
                println("Handling " + name)

                ClassReader cr = new ClassReader(file.bytes)
                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
                ClassVisitor cv = new InjectClassVisitor(cw)

                cr.accept(cv, ClassReader.EXPAND_FRAMES)

                byte[] code = cw.toByteArray()

                FileOutputStream fos = new FileOutputStream(
                        file.parentFile.absolutePath + File.separator + name)
                fos.write(code)
                fos.close()
            }
        }

    }
}