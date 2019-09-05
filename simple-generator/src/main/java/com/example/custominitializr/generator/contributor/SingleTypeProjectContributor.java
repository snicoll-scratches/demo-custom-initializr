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

package com.example.custominitializr.generator.contributor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

public class SingleTypeProjectContributor implements ProjectContributor {

	private final MustacheTemplateRenderer templateRenderer;

	private final ProjectDescription projectDescription;

	private final String className;

	public SingleTypeProjectContributor(MustacheTemplateRenderer templateRenderer,
			ProjectDescription projectDescription, String className) {
		this.templateRenderer = templateRenderer;
		this.projectDescription = projectDescription;
		this.className = className;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path file = this.projectDescription.getBuildSystem()
				.getMainSource(projectRoot, this.projectDescription.getLanguage())
				.createSourceFile(this.projectDescription.getPackageName(), this.className);
		Map<String, Object> model = createTemplateModel();
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
			writer.println(this.templateRenderer.render(determineTemplateName(), model));
		}
	}

	private String determineTemplateName() {
		return this.className + "." + this.projectDescription.getLanguage().id();
	}

	private Map<String, Object> createTemplateModel() {
		Map<String, Object> model = new HashMap<>();
		model.put("packageName", this.projectDescription.getPackageName());
		return model;
	}

}
