package org.uulib.gittag

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.uulib.gradletag.core.GradleTagPlugin
import org.uulib.gradletag.core.GradleTagExtension
import org.gradle.api.plugins.ExtensionAware

class GitTagPlugin : Plugin<Project> {
	
	override fun apply(project: Project) {
		project.pluginManager.apply(GradleTagPlugin::class.java)
		
		val gradletag = project.extensions.getByType(GradleTagExtension::class.java)
		val gitEx = (gradletag as ExtensionAware).extensions
				.create(GitExtension.EXTENSION_NAME, GitExtension::class.java, project)
		
		gradletag.vcs = gitEx.repositoryFromRootDir()
	}
	
}