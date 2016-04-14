package io.paradoxical.dropwizard.swagger;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Value
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class AssetsDefinition {
    @Nonnull
    @NonNull
    private final String resourcePath;

    @Nonnull
    @NonNull
    private final String uriPath;

    private final String indexFile;

    @Nonnull
    @NonNull
    private final String assetsName;
}
