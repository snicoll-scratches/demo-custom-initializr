package com.example.custominitializr.generator.contributor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildWriter;
import io.spring.initializr.generator.buildsystem.gradle.GroovyDslGradleBuildWriter;
import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.io.SimpleIndentStrategy;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

public class GradleBuildProjectContributor implements ProjectContributor {

	private final ResolvedProjectDescription projectDescription;

	public GradleBuildProjectContributor(ResolvedProjectDescription projectDescription) {
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
		build.setGroup(this.projectDescription.getGroupId());
		build.setArtifact(this.projectDescription.getArtifactId());
		build.setVersion(this.projectDescription.getVersion());
		build.setSourceCompatibility(this.projectDescription.getLanguage().jvmVersion());
		build.plugins().add("java");
		this.projectDescription.getRequestedDependencies()
				.forEach((id, dependency) -> build.dependencies().add(id, dependency));
		return build;
	}

}
