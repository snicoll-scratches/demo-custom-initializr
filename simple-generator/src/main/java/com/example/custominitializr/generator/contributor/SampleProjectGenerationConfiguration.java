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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import org.springframework.context.annotation.Bean;

/**
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
public class SampleProjectGenerationConfiguration {

	@Bean
	public BuildCustomizer<Build> sampleBuildCustomizer() {
		return (build) -> build.addInternalVersionProperty("test.information", "Hello");
	}

	@Bean
	public SingleTypeProjectContributor singleTypeProjectContributor(MustacheTemplateRenderer templateRenderer,
			ResolvedProjectDescription description) {
		return new SingleTypeProjectContributor(templateRenderer, description, "Test.java");
	}

}