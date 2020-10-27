#  Library-JUnit5Utils

This project contains shared logic to simplify writing JUnit 5 Test Cases for primarily Eclipse Modeling Framework based code. 

## Platform Standalone Extension
Code contained in the package `tools.mdsd.junit5utils` provides fundational support to write plain JUnit Tests (_not_ Plugin-Tests) for Eclipse-Plugins. To leverage the extension support annontate the test class with `@ExtendsWith(PlatformStandaloneExtension.class)`. The extension currently provides the following features:

- `@PluginTestOnly`: annotate your test methods, to explicity show the necessity to be run inside a running eclipse platform. Test methods which are annotated with `@PluginTestOnly` are skipped by the JUnit5 Test runner if `Platform.isRunning()` evaluates to false.

- `@InitializationTaskProvide`: Support standalone initialization using the [StandaloneInitialization](https://github.com/MDSD-Tools/Library-StandaloneInitialization). Annotate static methods which return a custom `InitializationTask` to register the task with the `StandaloneInitializationBuilder`.