import org.gradle.api.JavaVersion.*

plugins {
    java
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("com.google.cloud.tools.jib") version("2.4.0")
}

group = "com.github.aesteve"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.vertx:vertx-tcp-eventbus-bridge:4.0.0-milestone4")
}

tasks {
    withType<Wrapper> {
        gradleVersion = "6.5"
    }

    withType<JavaCompile> {
        sourceCompatibility = VERSION_13.toString()
        targetCompatibility = VERSION_13.toString()
    }

    shadowJar {
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "com.github.aesteve.vertx.eventbusbridge.TestServer"))
        }
    }
}

jib {
    from {
        image = "adoptopenjdk:14-jre-hotspot"
    }
    to {
        image = "aesteve/tests"
        tags = setOf("mock-eventbus-server")

    }
    container {
        ports = listOf("7542")
    }
}
