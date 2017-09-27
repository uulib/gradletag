package org.uulib.gradletag.core

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.*

class GradleTagSpec extends Specification {
	
	@Rule TemporaryFolder dummyProjectDir = new TemporaryFolder()
	File buildFile
	
	def setup() {
		buildFile = dummyProjectDir.newFile('build.gradle')
		
		buildFile <<
"""
plugins {
	id 'org.uulib.gradletag'
}

import org.uulib.gradletag.core.VcsTagger

class DummyTagger implements VcsTagger {

	@Override
	void tag(String tag, String comment) {
		println '+++' + tag + '+++' + comment + '+++'
	}

}

gradletag {
	vcs = new DummyTagger()

	tags {
		dummy {
			tag = "Boo"
			comment = 'This is scary'
		}
	}
}

"""
		
	}
	
	@Unroll
	def "A tagging task is created when a tag is defined in Gradle #gradleVersion"(String gradleVersion) {

		when:
		def result = createRunner(gradleVersion).withArguments('tasks').build()
		
		then:
		result.output.contains('tagVcsWithDummy')
		
		where:
		gradleVersion << CompatibleGradleVersions.VERSIONS
	}
	
	@Unroll
	def "The tagging task invokes the vcs tagger in Gradle #gradleVersion"(String gradleVersion) {
		when:
		def result = createRunner(gradleVersion).withArguments('tagVcsWithDummy', '--stacktrace').build()
		
		then:
		result.output.contains('+++Boo+++This is scary+++')
		
		where:
		gradleVersion << CompatibleGradleVersions.VERSIONS
	}
	
	private GradleRunner createRunner(String gradleVersion) {
		return GradleRunner.create()
				.withPluginClasspath()
				.withGradleVersion(gradleVersion)
				.withProjectDir(dummyProjectDir.root)
	}

}
