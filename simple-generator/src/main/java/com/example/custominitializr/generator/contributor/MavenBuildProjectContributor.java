package com.example.custominitializr.generator.contributor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildWriter;
import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.io.SimpleIndentStrategy;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

public class MavenBuildProjectContributor implements ProjectContributor {

	private final ResolvedProjectDescription projectDescription;

	public MavenBuildProjectContributor(ResolvedProjectDescription projectDescription) {
		this.projectDescription = projectDescription;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path pomFile = Files.createFile(projectRoot.resolve("pom.xml"));
		MavenBuildWriter buildWriter = new MavenBuildWriter();
		try (IndentingWriter writer = new IndentingWriter(Files.newBufferedWriter(pomFile),
				new SimpleIndentStrategy("    "))) {
			buildWriter.writeTo(writer, createMavenBuild());
		}
	}

	private MavenBuild createMavenBuild() {
		MavenBuild build = new MavenBuild();
		build.setGroup(this.projectDescription.getGroupId());
		build.setArtifact(this.projectDescription.getArtifactId());
		build.setVersion(this.projectDescription.getVersion());
		build.setName(this.projectDescription.getName());
		build.setDescription(this.projectDescription.getDescription());
		build.setProperty("java.version", this.projectDescription.getLanguage().jvmVersion());
		this.projectDescription.getRequestedDependencies()
				.forEach((id, dependency) -> build.dependencies().add(id, dependency));
		return build;
	}

}
