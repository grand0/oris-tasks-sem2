import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("idea")
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

group = "ru.kpfu.itis.gr201.ponomarev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-webmvc:${properties["springVersion"]}")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:9.0.85")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("hello.jar")
    mergeServiceFiles()
    manifest {
        attributes(mapOf("Main-Class" to properties["mainClass"]))
    }
}
