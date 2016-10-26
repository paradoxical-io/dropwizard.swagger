package io.paradoxical.dropwizard.swagger.resources;

import io.dropwizard.views.View;
import lombok.Getter;
import lombok.NonNull;

import javax.annotation.Nonnull;

@Getter
public class SwaggerUIView extends View {

    @Nonnull
    private final String defaultSwaggerJsonPath;

    @Nonnull
    private final boolean useJsonEditor;

    public SwaggerUIView(
            @NonNull @Nonnull final String swaggerViewResourcePath,
            @NonNull @Nonnull final String defaultSwaggerJsonPath,
            final boolean useJsonEditor) {

        super(swaggerViewResourcePath);

        this.defaultSwaggerJsonPath = defaultSwaggerJsonPath;
        this.useJsonEditor = useJsonEditor;
    }
}
