package com.antiless.plugin.inject

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.BaseExtension

class InjectPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create('injectExtension', InjectExtension)
        project.task('testPlugin') << {
            println "Start Inject"
            println "version " + project.injectExtension.versionLabel
        }
        registerInjectTransform(project)
    }

    static void registerInjectTransform(Project project) {
        BaseExtension android = project.extensions.getByType(BaseExtension)
        InjectTransform transform = new InjectTransform(project)
        android.registerTransform(transform)
    }
}