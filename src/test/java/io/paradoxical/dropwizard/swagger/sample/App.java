package io.paradoxical.dropwizard.swagger.sample;

import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.paradoxical.dropwizard.bundles.admin.AdminBundle;
import io.paradoxical.dropwizard.bundles.admin.AdminResourceEnvironment;
import io.paradoxical.dropwizard.swagger.AdminSwaggerConfiguration;
import io.paradoxical.dropwizard.swagger.AppSwaggerConfiguration;
import io.paradoxical.dropwizard.swagger.SwaggerFilters;
import io.paradoxical.dropwizard.swagger.SwaggerUIConfigurator;
import io.paradoxical.dropwizard.swagger.bundles.SwaggerUIAdminAssetsBundle;
import io.paradoxical.dropwizard.swagger.bundles.SwaggerUIBundle;
import io.swagger.models.Swagger;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.SecuritySchemeDefinition;

import javax.ws.rs.Path;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

public class App extends Application<Config> {
    public static void main(String... args) throws Exception {
        new App().run("server", "conf.yml");
    }


    @Override
    public void initialize(Bootstrap<Config> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());

        bootstrap.addBundle(
                new SwaggerUIBundle(env -> {
                    return new AppSwaggerConfiguration(env) {
                        {
                            setTitle("My API");
                            setDescription("My API");

                            // The package name to look for swagger resources under
                            setResourcePackage(App.class.getPackage().getName());

                            setLicense("Apache 2.0");
                            setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

                            setContact("admin@paradoxical.com");
                            setFilters(SwaggerFilters.withoutAnnotation(AdminOnly.class));

                            setVersion("1.0");
                        }
                    };
                }));

        bootstrap.addBundle(new SwaggerUIAdminAssetsBundle());

        final SwaggerUIConfigurator adminSwaggerConfigurator = new SwaggerUIConfigurator(env -> {
            return new AdminSwaggerConfiguration("/admin") {
                {
                    setTitle("My Admin API");
                    setDescription("My Admin API");

                    // The package name to look for swagger resources under
                    setResourcePackage(App.class.getPackage().getName());

                    setLicense("Apache 2.0");
                    setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

                    setContact("admin@paradoxical.com");

                    setFilters(SwaggerFilters.withAnnotation(AdminOnly.class));

                    setVersion("1.0");
                }
            };
        }, new Swagger() {
            {
                addSecurityDefinition("test", new BasicAuthDefinition());
            }
        });

        final AdminBundle adminBundle =
                AdminBundle.builder()
                           .configureEnvironment(adminSwaggerConfigurator)
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