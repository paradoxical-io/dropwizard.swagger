package io.paradoxical.dropwizard.swagger;

import io.dropwizard.setup.Environment;

import java.util.function.Function;

/**
 * Should return an instance of whatever Resource serves the SwaggerApi
 */
public interface ApiProvider extends Function<Environment, SwaggerApiProvider> {}
