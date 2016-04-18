package io.paradoxical.dropwizard.swagger;

import com.google.common.base.CharMatcher;
import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.setup.Environment;

import javax.annotation.Nullable;
import java.util.function.Function;

public class AdminSwaggerConfiguration extends EnvironmentSwaggerConfiguration {

    private static final CharMatcher slashMatcher = CharMatcher.is('/');

    protected AdminSwaggerConfiguration(
        @Nullable final Environment environment,
        final Function<Environment, MutableServletContextHandler> contextSelector) {
        super(environment, contextSelector);
    }

    protected AdminSwaggerConfiguration(final String adminBasePath) {
        this(null, Environment::getAdminContext);
        setBasePath("/" + slashMatcher.trimFrom(adminBasePath));
    }

    protected AdminSwaggerConfiguration(final Environment environment) {
        this(environment, Environment::getAdminContext);
    }
}
