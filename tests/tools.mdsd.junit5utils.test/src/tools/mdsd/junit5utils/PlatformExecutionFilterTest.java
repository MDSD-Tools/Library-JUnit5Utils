package tools.mdsd.junit5utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.core.runtime.Platform;
import org.junit.jupiter.api.Test;

import tools.mdsd.junit5utils.annotations.PluginTestOnly;

class PlatformExecutionFilterTest {
    
    @Test
    @PluginTestOnly
    void thisTestShouldOnlyBeExecutedAsPluginTest() {
        assertTrue(Platform.isRunning());
    }
}
