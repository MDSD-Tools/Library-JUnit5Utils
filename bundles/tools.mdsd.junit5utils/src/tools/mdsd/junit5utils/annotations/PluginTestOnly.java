package tools.mdsd.junit5utils.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import tools.mdsd.junit5utils.extensions.PluginTestFilterExtension;

@Retention(RUNTIME)
@Target({METHOD, TYPE})
@ExtendWith(PluginTestFilterExtension.class)
public @interface PluginTestOnly {

}
