package io.paradoxical.dropwizard.swagger;

import io.dropwizard.setup.Environment;

import java.util.function.Function;

public interface ApiProvider extends Function<Environment, SwaggerApiResource> {}
