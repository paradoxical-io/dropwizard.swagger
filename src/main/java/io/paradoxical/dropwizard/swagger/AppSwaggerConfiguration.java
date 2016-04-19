package io.paradoxical.dropwizard.swagger;

import io.dropwizard.setup.Environment;

public class AppSwaggerConfiguration extends EnvironmentSwaggerConfiguration {

    protected AppSwaggerConfiguration(
        final Environment environment) {
        super(environment, Environment::getApplicationContext);
    }
}
