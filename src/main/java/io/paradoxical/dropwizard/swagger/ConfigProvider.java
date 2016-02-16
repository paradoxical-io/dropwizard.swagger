package io.paradoxical.dropwizard.swagger;

import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;

import java.util.function.Function;

public interface ConfigProvider extends Function<Environment, BeanConfig> {}
