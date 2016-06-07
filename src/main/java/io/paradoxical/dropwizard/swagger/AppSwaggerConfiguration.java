package io.paradoxical.dropwizard.swagger;

import io.dropwizard.setup.Environment;

import javax.annotation.Nullable;

public class AppSwaggerConfiguration extends EnvironmentSwaggerConfiguration {

    public AppSwaggerConfiguration() {
        this(null);
    }

    public AppSwaggerConfiguration(@Nullable final Environment environment) {
        super(environment, Environment::getApplicationContext);
    }
}
