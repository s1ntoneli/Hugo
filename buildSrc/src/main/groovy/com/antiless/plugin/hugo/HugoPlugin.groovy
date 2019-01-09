package com.antiless.plugin.hugo

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class HugoPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("project " + project.name)
        def isApp = project.plugins.withType(AppPlugin)
        def isLibrary = project.plugins.withType(LibraryPlugin)

        if (!isApp && !isLibrary) {
            throw new IllegalStateException("'android' and 'android-library' is required.")
        }

        final def log = project.logger
        final def variants
        if (isApp) {
            variants = project.android.applicationVariants
        } else {
            variants = project.android.libraryVariants
        }

        println("variants " + variants)

        // TODO got how these projects work
        project.dependencies {
//            debugImplementation project(':hugo-runtime')
//            debugCompile 'com.jakewharton.hugo:hugo-runtime:1.2.2-SNAPSHOT'
            // TODO this should come transitively
            debugImplementation 'org.aspectj:aspectjrt:1.8.6'
//            implementation project(':hugo-annotations')
//            compile 'com.jakewharton.hugo:hugo-annotations:1.2.2-SNAPSHOT'
        }
        project.extensions.create('hugo', HugoExtension)

        variants.all { variant ->
            if (!variant.buildType.isDebuggable()) {
                println("Skipping non-debuggable build type '${variant.buildType.name}'.")
                return
            } else if (!project.hugo.enabled) {
                println("Hugo is not disabled.")
                return
            }

            println("Hugo start working.")
            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                String[] args = [
                        "-showWeaveInfo",
                        "-1.5",
                        "-inpath", javaCompile.destinationDir.toString(),
                        "-aspectpath", javaCompile.classpath.asPath,
                        "-d", javaCompile.destinationDir.toString(),
                        "-classpath", javaCompile.classpath.asPath,
                        "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)
                ]
                println("jcompile args " + Arrays.toString(args))

                MessageHandler handler = new MessageHandler(true)
                new Main().run(args, handler)
                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            break;
                        case IMessage.WARNING:
                            log.warn message.message, message.thrown
                            break;
                        case IMessage.INFO:
                            log.info message.message, message.thrown
                            break;
                        case IMessage.DEBUG:
                            log.debug message.message, message.thrown
                            break
                    }
                }
            }
        }
    }
}