package io.quarkus.registry.client.impl;

import io.quarkus.registry.client.RegistryClientFactory;
import io.quarkus.registry.client.spi.RegistryClientEnvironment;
import io.quarkus.registry.client.spi.RegistryClientFactoryProvider;

public class RegistryClientFactoryProviderImpl implements RegistryClientFactoryProvider {
    @Override
    public RegistryClientFactory newRegistryClientFactory(RegistryClientEnvironment registryClientEnvironment) {
        return new RegistryClientFactoryImpl();
    }
}
