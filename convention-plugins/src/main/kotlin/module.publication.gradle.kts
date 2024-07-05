import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        // Provide artifacts information required by Maven Central
        pom {
            name.set("Lazy Sticky Headers")
            description.set("Lazy Sticky Headers for Compose Multiplatform")
            url.set("https://github.com/gregkorossy/lazy-sticky-headers")

            licenses {
                license {
                    name.set("Apache License, Version 2.0")
                    url.set("https://opensource.org/license/apache-2-0")
                }
            }
            developers {
                developer {
                    id.set("gregkorossy")
                    name.set("Gergely Kőrössy")
                }
            }
            scm {
                url.set("https://github.com/gregkorossy/lazy-sticky-headers")
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
