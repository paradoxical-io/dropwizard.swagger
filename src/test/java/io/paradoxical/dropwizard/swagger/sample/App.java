package io.paradoxical.dropwizard.swagger.sample;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.swagger.SwaggerConfiguration;
import io.paradoxical.dropwizard.swagger.SwaggerUIConfigurator;
import io.paradoxical.dropwizard.swagger.bundles.SwaggerUIBundle;

public class App extends Application<Config> {
    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void initialize(Bootstrap<Config> bootstrap) {
        bootstrap.addBundle(
                new SwaggerUIBundle(
                        SwaggerUIConfigurator.forConfig(env -> {
                            return new SwaggerConfiguration() {
                                {
                                    setTitle("My API");
                                    setDescription("My API");

                                    // The package name to look for swagger resources under
                                    setResourcePackage(App.class.getPackage().getName());

                                    setLicense("Apache 2.0");
                                    setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

                                    setContact("admin@site.com");

                                    setScan(true);

                                    setVersion("1.0");
                                }
                            };
                        })));

    }

    @Override
    public void run(Config configuration, Environment environment) {
        // nothing to do yet
    }

}