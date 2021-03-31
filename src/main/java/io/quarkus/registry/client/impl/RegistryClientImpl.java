package io.quarkus.registry.client.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import io.quarkus.maven.ArtifactCoords;
import io.quarkus.registry.RegistryResolutionException;
import io.quarkus.registry.catalog.ExtensionCatalog;
import io.quarkus.registry.catalog.PlatformCatalog;
import io.quarkus.registry.catalog.json.JsonCatalogMapperHelper;
import io.quarkus.registry.catalog.json.JsonExtensionCatalog;
import io.quarkus.registry.catalog.json.JsonPlatformCatalog;
import io.quarkus.registry.client.RegistryClient;
import io.quarkus.registry.config.RegistryConfig;
import io.quarkus.registry.config.json.JsonRegistryConfig;
import io.quarkus.registry.config.json.JsonRegistryDescriptorConfig;
import io.quarkus.registry.config.json.JsonRegistryNonPlatformExtensionsConfig;
import io.quarkus.registry.config.json.JsonRegistryPlatformsConfig;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RegistryClientImpl implements RegistryClient {

    private final URI registryURL;

    public RegistryClientImpl(URI registryURL) {
        this.registryURL = registryURL;
    }

    @Override
    public RegistryConfig resolveRegistryConfig() throws RegistryResolutionException {
        JsonRegistryConfig config = new JsonRegistryConfig();
        // Add descriptor
        JsonRegistryDescriptorConfig descriptorConfig = new JsonRegistryDescriptorConfig();
        descriptorConfig.setArtifact(new ArtifactCoords(MavenConfig.GROUP_ID, MavenConfig.REGISTRY_ARTIFACT_ID, "json", MavenConfig.VERSION));
        config.setDescriptor(descriptorConfig);
        // Add platforms
        JsonRegistryPlatformsConfig platformsConfig = new JsonRegistryPlatformsConfig();
        platformsConfig.setArtifact(new ArtifactCoords(MavenConfig.GROUP_ID, MavenConfig.PLATFORM_ARTIFACT_ID, "json", MavenConfig.VERSION));
        config.setPlatforms(platformsConfig);
        // Add non-platforms
        JsonRegistryNonPlatformExtensionsConfig nonPlatformExtensionsConfig = new JsonRegistryNonPlatformExtensionsConfig();
        nonPlatformExtensionsConfig.setArtifact(new ArtifactCoords(MavenConfig.GROUP_ID, MavenConfig.NON_PLATFORM_EXTENSIONS_ARTIFACT_ID, "json", MavenConfig.VERSION));
        config.setNonPlatformExtensions(nonPlatformExtensionsConfig);
        return config;
    }

    @Override
    public ExtensionCatalog resolveNonPlatformExtensions(String quarkusCore) throws RegistryResolutionException {
        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(new URIBuilder(registryURL.resolve("/client/non-platform-extensions"))
                                              .addParameter("v", quarkusCore)
                                              .build());
            get.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                    throw new IOException(statusLine.getStatusCode() + " -> " + statusLine.getReasonPhrase());
                }
                return JsonCatalogMapperHelper.deserialize(response.getEntity().getContent(), JsonExtensionCatalog.class);
            }
        } catch (IOException | URISyntaxException e) {
            throw new RegistryResolutionException("Error while fetching non-platform extensions", e);
        }
    }

    @Override
    public ExtensionCatalog resolvePlatformExtensions(ArtifactCoords artifactCoords) throws RegistryResolutionException {
        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(new URIBuilder(registryURL.resolve("/client/extensions"))
                                              .addParameter("c", artifactCoords.toString())
                                              .build());
            get.setHeader("Content-Type", "application/json");
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                    throw new IOException(statusLine.getStatusCode() + " -> " + statusLine.getReasonPhrase());
                }
                return JsonCatalogMapperHelper.deserialize(response.getEntity().getContent(), JsonExtensionCatalog.class);
            }
        } catch (IOException | URISyntaxException e) {
            throw new RegistryResolutionException("Error while fetching non-platform extensions", e);
        }
    }

    @Override
    public PlatformCatalog resolvePlatforms(String quarkusCore) throws RegistryResolutionException {
        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(new URIBuilder(registryURL.resolve("/client/platforms"))
                                              .addParameter("v", quarkusCore)
                                              .build());
            get.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                    throw new IOException(statusLine.getStatusCode() + " -> " + statusLine.getReasonPhrase());
                }
                return JsonCatalogMapperHelper.deserialize(response.getEntity().getContent(), JsonPlatformCatalog.class);
            }
        } catch (IOException | URISyntaxException e) {
            throw new RegistryResolutionException("Error while fetching non-platform extensions", e);
        }
    }
}
