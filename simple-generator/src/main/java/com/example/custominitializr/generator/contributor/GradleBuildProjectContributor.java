package com.example.custominitializr.generator.contributor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildWriter;
import io.spring.initializr.generator.buildsystem.gradle.GroovyDslGradleBuildWriter;
import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.io.SimpleIndentStrategy;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

public class GradleBuildProjectContributor implements ProjectContributor {

	private final ProjectDescription projectDescription;

	public GradleBuildProjectContributor(ProjectDescription projectDescription) {
		this.projectDescription = projectDescription;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path pomFile = Files.createFile(projectRoot.resolve("build.gradle"));
		GradleBuildWriter buildWriter = new GroovyDslGradleBuildWriter();
		try (IndentingWriter writer = new IndentingWriter(Files.newBufferedWriter(pomFile),
				new SimpleIndentStrategy("    "))) {
			buildWriter.writeTo(writer, createMavenBuild());
		}
	}

	private GradleBuild createMavenBuild() {
		GradleBuild build = new GradleBuild();
		build.settings().group(this.projectDescription.getGroupId()).artifact(this.projectDescription.getArtifactId())
				.version(this.projectDescription.getVersion())
				.sourceCompatibility(this.projectDescription.getLanguage().jvmVersion());
		build.plugins().add("java");
		this.projectDescription.getRequestedDependencies()
				.forEach((id, dependency) -> build.dependencies().add(id, dependency));
		return build;
	}

}
