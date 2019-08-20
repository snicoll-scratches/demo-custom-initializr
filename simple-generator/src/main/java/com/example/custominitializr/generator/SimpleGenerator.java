/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.custominitializr.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.custominitializr.generator.contributor.SampleContributor;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.DefaultProjectAssetGenerator;
import io.spring.initializr.generator.project.ProjectAssetGenerator;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerator;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataBuilder;

import org.springframework.core.io.ClassPathResource;

class SimpleGenerator {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");

	Path generateProject(ProjectDescription description) {
		ProjectGenerator projectGenerator = new ProjectGenerator((context) -> {
			context.registerBean(SampleContributor.class, SampleContributor::new);
			context.registerBean(InitializrMetadata.class, () -> InitializrMetadataBuilder.create()
					.withInitializrMetadata(new ClassPathResource("sample-metadata.json")).build());
			context.registerBean(IndentingWriterFactory.class, IndentingWriterFactory::withDefaultSettings);
			context.registerBean(MustacheTemplateRenderer.class,
					() -> new MustacheTemplateRenderer("classpath:/templates"));

		});
		ProjectAssetGenerator<Path> projectAssetGenerator = new DefaultProjectAssetGenerator(
				(resolvedDescription) -> Paths.get("target/projects",
						"project-" + formatter.format(LocalDateTime.now())));
		return projectGenerator.generate(description, projectAssetGenerator);
	}

}
