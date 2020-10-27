package tools.mdsd.junit5utils.extensions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.runtime.Platform;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.common.collect.Streams;

import tools.mdsd.junit5utils.annotations.InitializationTaskProvider;
import tools.mdsd.junit5utils.annotations.PluginTestOnly;
import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializerBuilder;
import tools.mdsd.library.standalone.initialization.core.EclipseProjectByClassRegistration;

public class PlatformStandaloneExtension implements BeforeAllCallback, ExecutionCondition {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        final var builder = StandaloneInitializerBuilder.builder();

        var result = context.getElement()
            .filter(Class.class::isInstance)
            .map(Class.class::cast)
            .map(ae -> {
                builder.addCustomTask(new EclipseProjectByClassRegistration(ae));
                return collectInitializationProviders(ae).map(this::retrieveInitializationTask)
                    .collect(Collectors.toList());
            })
            .orElseGet(Collections::emptyList);

        result.forEach(builder::addCustomTask);

        builder.build()
            .init();
    }

    Stream<Method> collectInitializationProviders(Class<?> clazz) {
        if (clazz == null)
            return Stream.empty();

        var selfs = Arrays.asList(clazz.getDeclaredMethods())
            .stream()
            .filter(m -> m.getDeclaredAnnotation(InitializationTaskProvider.class) != null);

        var ifaces = Arrays.asList(clazz.getInterfaces())
            .stream()
            .flatMap(this::collectInitializationProviders);

        var supers = collectInitializationProviders(clazz.getSuperclass());

        return Streams.concat(selfs, ifaces, supers);
    }

    InitializationTask retrieveInitializationTask(Method m) {
        if (!Modifier.isStatic(m.getModifiers())) {
            throw new Error(
                    "Only static methods may be annotation with @" + InitializationTaskProvider.class.getName());
        }
        if (!InitializationTask.class.isAssignableFrom(m.getReturnType())) {
            throw new Error("A static initialization provider method needs to return an instance of "
                    + InitializationTask.class.getName());
        }
        if (m.getParameters().length > 0) {
            throw new Error("Currently no parameters are supported");
        }

        InitializationTask result;
        try {
            m.setAccessible(true);
            result = (InitializationTask) m.invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new Error("Error invoking provider method.", e);
        }

        return result;
    }

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        var element = context.getElement();
        if (element.isPresent()) {
            var annotation = element.get().getAnnotation(PluginTestOnly.class);
            if (annotation != null && !Platform.isRunning()) {
                return ConditionEvaluationResult.disabled("This test case is disabled in standalone-mode. It needs to be executed as plugin test.");
            }
        }
        return ConditionEvaluationResult.enabled("Test enabled by default");
    }

}
