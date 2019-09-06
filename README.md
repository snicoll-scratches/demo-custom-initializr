# Demo steps

Let's start with a simple project with a main class, a logging config and an empty
`SimpleGenerator` that has a `Path generateProject(ProjectDescription description)`.

## Basic generator
Create a `SampleGenerator` that creates a dummy text file at the root of the project
structure. First we create a simple `ProjectContributor`

```
public void contribute(Path projectRoot) throws IOException {
    Path file = Files.createFile(projectRoot.resolve("hello.txt"));
    try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
        writer.println("Test");
    }
}
```

We can then configure the `ProjectGenerator` to register it manually. We configure the
`ProjectAssetGenerator` to generate the project structure in `target/projects`


## More advanced code generation
A new commit brings a contributor for Maven and a contributor for Gradle. It also changes
the `ProjectDescription` to define certain aspect for the build and adds a dependency.

Let's create a `ProjectGenerationContext` and a bean for each. We need to register the
class in `META-INF/spring.factories`.

We can see that both a basic Maven build and a basic Gradle build have been contributed,
that's not really what we want.

## Conditions
Let's showcase how conditions can add something to the `ProjectGenerationContext` when it
makes sense. We can swap the build system and showcase the output.

## Add out-of-the-box contributions
We can add `initializr-generator-spring` to bring all the defaults for a Spring Boot
project. Those conventions require a few extra bits:

* The metadata
* An indent writer strategy
* A renderer of templates

A new commit brings the json file to represent the metadata as well as the required
registration for the 3 beans above. It also complements the `ProjectDescription` with
whatever those need as input (application name and platform version).

## Refine build contributors
Depending on orders, our former build contributors could write the build file or the ones
from the Spring conventions. Let's replace them to showcase how we can customize the
build. 

We can replace the beans in `SampleProjectGenerationConfiguration` to a unique sample:

```
@Bean
public BuildCustomizer<Build> sampleBuildCustomizer() {
    return (build) -> build.properties().version("test.information", "Hello");
}
```

## Prepare custom instance
A new commit brings a new project that has:

* `application.yml` with a very basic metadata
* An empty Spring Boot app

We can run this app and then generate a project against `localhost:8080` from the IDE.