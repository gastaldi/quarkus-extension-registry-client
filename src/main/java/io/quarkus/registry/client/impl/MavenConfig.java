package io.quarkus.registry.client.impl;

interface MavenConfig {

    String GROUP_ID = "io.quarkus.registry";

    String PLATFORM_ARTIFACT_ID = "quarkus-platforms";

    String PLATFORM_EXTENSIONS_ARTIFACT_ID = "quarkus-platform-extensions";

    String NON_PLATFORM_EXTENSIONS_ARTIFACT_ID = "quarkus-non-platform-extensions";

    String REGISTRY_ARTIFACT_ID = "quarkus-registry-descriptor";

    String VERSION = "1.0-SNAPSHOT";
}
