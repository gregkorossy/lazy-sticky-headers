import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.jetbrainsCompose)
    //id("module.publication")
    alias(libs.plugins.dokka)
    alias(libs.plugins.nexusPlugin)
}

version = "0.1.0-alpha01"

kotlin {
    jvm(name = "desktop")
    androidTarget {
        publishLibraryVariants("release")
    }

    js(compiler = IR) {
        //nodejs()
        //browser()
        browser {
            webpackTask {
                mainOutputFileName = "lazy-sticky-headers.js"
            }
        }
        binaries.executable()
    }
    // Kotlin/Wasm drawing to a canvas
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        //nodejs()
        browser()
        binaries.library()
    }
    // Building and publishing for iOS target requires a machine running macOS;
    // otherwise, the .klib will not be produced and the compiler warns about that.
    // See https://kotlinlang.org/docs/multiplatform-mobile-understand-project-structure.html#ios-framework
    listOf(
        // By declaring these targets, the iosMain source set will be created automatically
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "lazy-sticky-headers"
            isStatic = true
        }
    }

    /*iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
    mingwX64()
    macosArm64()*/

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.foundation)
                api(compose.runtime)
                //api(compose.material3)
                //api(compose.material)
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }

    //explicitApi()
}

android {
    namespace = "me.gingerninja.lazy"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    buildFeatures {
        buildConfig = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        abortOnError = false
    }
}

/*tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xexplicit-api=strict")
    }
}*/

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(libs.versions.jvmTarget.get())
    }
}

tasks.withType<JavaCompile>().configureEach {
    this.targetCompatibility = libs.versions.jvmTarget.get()
    this.sourceCompatibility = libs.versions.jvmTarget.get()
}

tasks.withType<DokkaTask>().configureEach {
    notCompatibleWithConfigurationCache("https://github.com/Kotlin/dokka/issues/2231")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01)

    coordinates("me.gingerninja.lazy", "sticky-headers", version.toString())

    pom {
        name.set("Lazy Sticky Headers")
        description.set("Lazy Sticky Headers for Compose Multiplatform")
        inceptionYear.set("2024")
        url.set("https://github.com/gregkorossy/lazy-sticky-headers")

        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("gregkorossy")
                name.set("Gergely Kőrössy")
                url.set("https://github.com/gregkorossy/")
            }
        }
        scm {
            url.set("https://github.com/gregkorossy/lazy-sticky-headers")
            connection.set("scm:git:git://github.com/gregkorossy/lazy-sticky-headers.git")
            developerConnection.set("scm:git:ssh://git@github.com/gregkorossy/lazy-sticky-headers.git")
        }
    }

    signAllPublications()
}

/*kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}*/

/* ℹ️ Interesting commands:

- publish to local maven: ./gradlew publishToMavenLocal --no-configuration-cache
- API dump: ./gradlew :sticky-headers:apiDump
- API check: ./gradlew :sticky-headers:apiCheck
- check project: ./gradlew :sticky-headers:check
- Dokka: ./gradlew sticky-headers:dokkaHtml --no-configuration-cache

 */