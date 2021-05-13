package tools.mdsd.junit5utils.extensions;

import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import org.eclipse.core.runtime.Platform;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import tools.mdsd.junit5utils.annotations.StandaloneTestOnly;

public class StandaloneTestFilterExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        if (findAnnotation(context.getElement(), StandaloneTestOnly.class).isPresent() && Platform.isRunning()) {
            return ConditionEvaluationResult
                .disabled("This test case is disabled in OSGi mode. It needs to be executed as standalone test.");
        }
        return ConditionEvaluationResult.enabled("Test enabled by default");
    }

}
