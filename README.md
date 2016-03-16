dropwizard-swagger
========================

![Build status](https://travis-ci.org/paradoxical-io/dropwizard.swagger.svg?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/io.paradoxical/dropwizard-swagger.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aio.paradoxical%20a%3Adropwizard-swagger)

Wire in swagger for your dropwizard application. Also supports having a separate swagger api for your admin resource. This can be useful when creating private admin API's.  

Currently supports dropwizard 0.9.1 and uses swagger2

## Installation

```
<dependency>
    <groupId>io.paradoxical</groupId>
    <artifactId>dropwizard-swagger</artifactId>
    <version>1.2</version>
</dependency>
```

## Configuring swagger 

If you want to configure swagger for the admin resource create an admin resource configurator first.  This encapsulates registration and asset handling on the admin port.

```
AdminResourceConfigurator adminResourceConfigurator = 
            AdminResourceConfigurator.builder()
                                     .adminRootPath(ADMIN_ROOT)
                                     .build();
```

To configure swagger, during initialization add the appropriate bundles.

```
@Override
public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
    // enable swagger for public port
    bootstrap.addBundle(new SwaggerAssetsBundle(this::getPublicSwagger));
    
    // set up the configurator (swagger will be enabled on this later)
    bootstrap.addBundle(adminResourceConfigurator);
    
    // add the swagger assets bundle. this must come after the resource configurator
    // is added
    bootstrap.addBundle(new SwaggerAdminAssetsBundle());
}

private BeanConfig getPublicSwagger(Environment environment) {
    ...
}

```

During run you can now manage your admin resource and enable swagger by passing in the swagger config


```
@Override
public void run(ServiceConfiguration config, final Environment env) throws Exception {
    adminResourceConfigurator.enableSwagger(env, getAdminSwaggerScanner());
    
    adminResourceConfigurator.getAdminResourceConfig().register(AdminResource.class);
}

private BeanConfig getAdminSwaggerScanner() {

    final BeanConfig swagConfig = new BeanConfig();
    swagConfig.setTitle("Admin API");
    swagConfig.setDescription("Admin API");
    swagConfig.setLicense("Apache 2.0");
    swagConfig.setResourcePackage(AdminResource.class.getPackage().getName());
    swagConfig.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");
    swagConfig.setContact("admin@site.com");
    swagConfig.setPrettyPrint(true);

    swagConfig.setVersion("1.0.1");

    swagConfig.setBasePath("/admin");

    return swagConfig;
}
```

## Overriding the swagger endpoint path

If you want to change where swagger lives (i.e. instead of at the root, which is the default) you can subclass your pages:

The index page that the swagger ui shows up at
```
@Path("/docs/api")
@Produces(MediaType.TEXT_HTML)
public class DocumentsPage extends SwaggerPagesResource {
}
```

The backing swagger API

```
@Path("/docs/api")
public class DocumentsPageApi extends SwaggerApiResource {
    public DocumentsPageApi(@NonNull final BeanConfig swaggerConfig) {
        super(swaggerConfig);
    }
}
```

Both pages MUST be at the same api path

And register them with either the bundle provider:

```
bootstrap.addBundle(new SwaggerAssetsBundle((ApiProvider) environment -> new DocumentsPageApi(getPublicSwagger(environment)), new DocumentsPage()));
```

Or with the admin provider

```
adminResourceConfigurator.enableSwagger(env, new DocumentsPage(), new DocumentsPageApi(getAdminSwaggerScanner()));
```

