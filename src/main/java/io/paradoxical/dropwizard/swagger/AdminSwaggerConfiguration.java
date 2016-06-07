package io.paradoxical.dropwizard.swagger;

import com.google.common.base.CharMatcher;
import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.setup.Environment;

import javax.annotation.Nullable;
import java.util.function.Function;

public class AdminSwaggerConfiguration extends EnvironmentSwaggerConfiguration {

    private static final CharMatcher slashMatcher = CharMatcher.is('/');

    public AdminSwaggerConfiguration() {
        super(null, Environment::getAdminContext);
    }

    public AdminSwaggerConfiguration(final String adminBasePath) {
        this();
        setBasePath("/" + slashMatcher.trimFrom(adminBasePath));
    }

    public AdminSwaggerConfiguration(@Nullable final Environment environment) {
        super(environment, Environment::getAdminContext);
    }
}
