dropwizard-swagger
========================

![Build status](https://travis-ci.org/paradoxical-io/dropwizard.swagger.svg?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/io.paradoxical/dropwizard-swagger.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aio.paradoxical%20a%3Adropwizard-swagger)

Wire in swagger for your dropwizard application.
Also supports having a separate swagger api for your admin resource.
This can be useful when creating private admin API's.

Default paths are `/swagger/ui` for the UI and `/swagger/api/swagger.{json, yaml}` for the api definition

Currently supports dropwizard 0.9.1 and uses swagger2

## Installation

```
<dependency>
    <groupId>io.paradoxical</groupId>
    <artifactId>dropwizard-swagger</artifactId>
    <version>2.0</version>
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
                return new SwaggerConfiguration () {
                    {
                        setTitle("My API");
                        setDescription("My API");

                        // The package name to look for swagger resources under
                        setResourcePackage(MyApplication.class.getPackage().getName());

                        setLicense("Apache 2.0");
                        setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

                        setContact("admin@site.com");

                        setPrettyPrint(true);

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

    // set up the configurator (swagger will be enabled on this later)
    bootstrap.addBundle(
        AdminResourcesBundle.builder()
                            .swaggerUIConfigurator(SwaggerUIConfigurator.forConfig(env -> {
                                            return new SwaggerConfiguration () {
                                                {
                                                    setTitle("My ADMIN API");
                                                    setDescription("My ADMIN API");

                                                    // The package name to look for swagger resources under
                                                    setResourcePackage(MyApplication.class.getPackage().getName());

                                                    setLicense("Apache 2.0");
                                                    setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

                                                    setContact("admin@site.com");

                                                    setPrettyPrint(true);

                                                    setVersion("1.0");
                                                }
                                            };
                                        })).build());
}
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

