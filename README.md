dropwizard-swagger
========================

![Build status](https://travis-ci.org/paradoxical-io/dropwizard-swagger.svg?branch=master)

Wire in swagger for your dropwizar application. Also supports having a separate swagger

To configure your admin resource create a resource configurator

```
AdminResourceConfigurator adminResourceConfigurator = 
            AdminResourceConfigurator.builder()
                                     .adminRootPath(ADMIN_ROOT)
                                     .build();
```

During initialization

```
@Override
public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
    bootstrap.addBundle(new SwaggerAssetsBundle(this::getPublicSwagger));
    
    bootstrap.addBundle(adminResourceConfigurator);
    
    bootstrap.addBundle(new SwaggerAdminAssetsBundle());
}
```

During run you can now manage your admin resource and add swagger


```
@Override
public void run(ServiceConfiguration config, final Environment env) throws Exception {
    adminResourceConfigurator.enableSwagger(env, getAdminSwaggerScanner());

    adminResourceConfigurator.register(AdminResource.class);
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