import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.quarkus)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.allopen)
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://maven.repository.redhat.com/ga/") }
}

dependencies {
    implementation(enforcedPlatform(libs.quarkus.bom))
    implementation(libs.bundles.quarkus.core)
    implementation(libs.bundles.quarkus.rest)
    implementation(libs.bundles.quarkus.data)
    implementation(libs.bundles.kotlin.support)
    runtimeOnly(libs.postgresql)
    testImplementation(libs.bundles.quarkus.test)
    testImplementation(libs.bundles.testcontainers)
    testImplementation(libs.quarkus.jdbc.h2)
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
    annotation("jakarta.persistence.Entity")
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.named<Test>("test") {
    dependsOn("quarkusBuild")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}
