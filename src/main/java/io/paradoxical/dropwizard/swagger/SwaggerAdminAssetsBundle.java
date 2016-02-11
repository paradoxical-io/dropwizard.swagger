package io.paradoxical.dropwizard.swagger;

public class SwaggerAdminAssetsBundle extends AdminAssetsBundle {
    public SwaggerAdminAssetsBundle() {
        super(SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT, SwaggerPagesResource.DROPWIZARD_SWAGGER_ASSET_ROOT);
    }
}
