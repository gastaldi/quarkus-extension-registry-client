package io.quarkus.registry.client.impl;

import java.net.URI;

import io.quarkus.registry.RegistryResolutionException;
import io.quarkus.registry.client.RegistryClient;
import io.quarkus.registry.client.RegistryClientFactory;
import io.quarkus.registry.config.RegistryConfig;

public class RegistryClientFactoryImpl implements RegistryClientFactory {
    @Override
    public RegistryClient buildRegistryClient(RegistryConfig registryConfig) throws RegistryResolutionException {
        return new RegistryClientImpl(URI.create("http://localhost:8080"));
    }
}
