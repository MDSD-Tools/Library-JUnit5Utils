package tools.mdsd.junit5utils.extensions;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import org.eclipse.core.runtime.Platform;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import tools.mdsd.junit5utils.annotations.PluginTestOnly;

public class PluginTestFilterExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        if (findAnnotation(context.getElement(), PluginTestOnly.class).isPresent() && !Platform.isRunning()) {
            return ConditionEvaluationResult
                .disabled("This test case is disabled in standalone-mode. It needs to be executed as plugin test.");
        }
        return ConditionEvaluationResult.enabled("Test enabled by default");
    }

}
