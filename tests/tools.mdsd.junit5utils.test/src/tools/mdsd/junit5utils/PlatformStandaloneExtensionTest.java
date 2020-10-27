package tools.mdsd.junit5utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tools.mdsd.junit5utils.annotations.InitializationTaskProvider;
import tools.mdsd.junit5utils.annotations.PluginTestOnly;
import tools.mdsd.junit5utils.extensions.PlatformStandaloneExtension;
import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;

@ExtendWith(PlatformStandaloneExtension.class)
class PlatformStandaloneExtensionTest {

    static String toBeChangedByInitialization = "FAILED";
    static final String expectedResult = "SUCCESS";
    
    private static AtomicInteger initializationCounter = new AtomicInteger(0);
    
    @InitializationTaskProvider
    static InitializationTask provideTask() {
        return () -> {
            PlatformStandaloneExtensionTest.toBeChangedByInitialization = expectedResult;
            PlatformStandaloneExtensionTest.initializationCounter.incrementAndGet();
        };
    }
    
    @InitializationTaskProvider
    static InitializationTask providePlatformInitializer() {
        return new InitializationTask() {
            
            @Override
            public void initilizationWithoutPlatform() throws StandaloneInitializationException {
            }
            
            @Override
            public void initializationWithPlatform() throws StandaloneInitializationException {
                PlatformStandaloneExtensionTest.toBeChangedByInitialization = expectedResult;
                PlatformStandaloneExtensionTest.initializationCounter.incrementAndGet();
            }
        };
    }
    
    @Test
    void testInitializationIsRunAtAll() {
        assertEquals(expectedResult, toBeChangedByInitialization);
    }
    
    @Test
    void testtestInitializationIsRunJustOnce_1() {
        assertTrue(initializationCounter.compareAndSet(1, 1));
    }
    
    @Test
    void testtestInitializationIsRunJustOnce_2() {
        assertTrue(initializationCounter.compareAndSet(1, 1));
    }
    
    @Test
    @PluginTestOnly
    void thisTestShouldOnlyBeExecutedAsPluginTest() {
        assertTrue(Platform.isRunning());
    }
}
