package io.paradoxical.dropwizard.swagger.sample;

import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.bundles.admin.AdminBundle;
import io.paradoxical.dropwizard.bundles.admin.AdminEnvironmentConfigurator;
import io.paradoxical.dropwizard.bundles.admin.AdminResourceEnvironment;
import io.paradoxical.dropwizard.swagger.AdminSwaggerConfiguration;
import io.paradoxical.dropwizard.swagger.AppSwaggerConfiguration;
import io.paradoxical.dropwizard.swagger.SwaggerFilters;
import io.paradoxical.dropwizard.swagger.SwaggerUIConfigurator;
import io.paradoxical.dropwizard.swagger.bundles.SwaggerUIAdminAssetsBundle;
import io.paradoxical.dropwizard.swagger.bundles.SwaggerUIBundle;

import javax.ws.rs.Path;

public class App extends Application<Config> {
    public static void main(String ...args) throws Exception {
        new App().run("server", "conf.yml");
    }

    @Override
    public void initialize(Bootstrap<Config> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());

        bootstrap.addBundle(
            new SwaggerUIBundle(
                SwaggerUIConfigurator.forConfig(env -> {
                    return new AppSwaggerConfiguration(env) {
                        {
                            setTitle("My API");
                            setDescription("My API");

                            // The package name to look for swagger resources under
                            setResourcePackage(App.class.getPackage().getName());

                            setLicense("Apache 2.0");
                            setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

                            setContact("admin@paradoxical.com");

                            setVersion("1.0");
                        }
                    };
                })));

        bootstrap.addBundle(new SwaggerUIAdminAssetsBundle());

        final SwaggerUIConfigurator adminConfigurator = SwaggerUIConfigurator.forConfig(env -> {
            return new AdminSwaggerConfiguration("/admin") {
                {
                    setTitle("My Admin API");
                    setDescription("My Admin API");

                    // The package name to look for swagger resources under
                    setResourcePackage(App.class.getPackage().getName());

                    setLicense("Apache 2.0");
                    setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

                    setContact("admin@paradoxical.com");

                    setFilters(SwaggerFilters.withAnnotation(Path.class));

                    setVersion("1.0");
                }
            };
        });

        final AdminBundle adminBundle =
            AdminBundle.builder()
                       .configureEnvironment(AdminEnvironmentConfigurator.forJersey(adminConfigurator::configure))
                       .build();

        bootstrap.addBundle(adminBundle);
    }

    @Override
    public void run(Config configuration, Environment environment) {
        environment.jersey().register(TestResource.class);
        final AdminResourceEnvironment adminResourceEnvironment = AdminResourceEnvironment.getOrCreate(environment);

        adminResourceEnvironment.adminResourceConfig().register(TestResource.class);
    }

}