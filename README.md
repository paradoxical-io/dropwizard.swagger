dropwizard-swagger
========================

[![Build status](https://travis-ci.org/paradoxical-io/dropwizard.swagger.svg?branch=master)](https://travis-ci.org/paradoxical-io/dropwizard.swagger)
[![Maven Central](https://img.shields.io/maven-central/v/io.paradoxical/dropwizard-swagger.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aio.paradoxical%20a%3Adropwizard-swagger)

Wire in swagger for your dropwizard application.
Also supports having a separate swagger api for your admin resource.
This can be useful when creating private admin API's.

Default ui paths are `/swagger/ui` for the UI and `/swagger/api/swagger.{json, yaml}` for the api swagger definition

Currently supports dropwizard 0.9.2 and uses swagger2

## Installation

```
<dependency>
    <groupId>io.paradoxical</groupId>
    <artifactId>dropwizard-swagger</artifactId>
    <version>2.3</version>
</dependency>
```

## Adding swagger and swagger UI

Adding swagger to your dropwizard application is as simple as adding the bundle to your application bootstrap

```
@Override
public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
    // enable swagger for application port
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

                        setContact("admin@paradoxical.io");

                        setVersion("1.0");
                    }
                };
            })));

}
```

## Configuring admin swagger

If you want to configure swagger for the admin resource create an admin resource configurator first.  This encapsulates registration and asset handling on the admin port.
To configure swagger, during initialization add the appropriate bundles.

```
@Override
public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {

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
                   .configureEnvironment(adminConfigurator)
                   .build();

    bootstrap.addBundle(adminBundle);
}
```

you should then be able to browse to the swagger ui on the admin port (8081 by default) under `/admin/swagger/ui`
```
http://localhost:8081/admin/swagger/ui
```


## Overriding the swagger UI paths

If you want to change where swagger lives
(i.e. instead of at the `/swagger/ui` and `/swagger/api/swagger.json`, which is the default)
you can use a custom swagger resource locator your like:


```
@Path("/swagger-api-documentation")
public class CustomSwaggerResourcesLocator extends DefaultSwaggerResourcesLocator {}
```

And use one of the `SwaggerUIConfigurator` constructor that takes in a locator

