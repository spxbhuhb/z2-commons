/*
 * Copyright © 2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    signing
    `maven-publish`
}

group = "hu.simplexion.z2"

val coroutines_version: String by project
val serialization_version: String by project
val datetime_version: String by project

val publishSnapshotUrl = (System.getenv("Z2_PUBLISH_SNAPSHOT_URL") ?: project.findProperty("z2.publish.snapshot.url"))?.toString()
val publishReleaseUrl = (System.getenv("Z2_PUBLISH_RELEASE_URL") ?: project.findProperty("z2.publish.release.url"))?.toString()
val publishUsername = (System.getenv("Z2_PUBLISH_USERNAME") ?: project.findProperty("z2.publish.username"))?.toString()
val publishPassword = (System.getenv("Z2_PUBLISH_PASSWORD") ?: project.findProperty("z2.publish.password"))?.toString()
val isSnapshot = "SNAPSHOT" in project.version.toString()
val isPublishing = project.properties["z2.publish"] != null || System.getenv("Z2_PUBLISH") != null

fun RepositoryHandler.mavenRepo(repoUrl: String) {
    maven {
        url = project.uri(repoUrl)
        name = "Central"
        isAllowInsecureProtocol = true
        credentials {
            username = publishUsername
            password = publishPassword
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    publishSnapshotUrl?.let { mavenRepo(it) }
    publishReleaseUrl?.let { mavenRepo(it) }
}

kotlin {
    jvm {
        jvmToolchain(11)
    }
    js(IR) {
        browser()
        binaries.library()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetime_version")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}

// ----------------------------------------------------------------
// DO NOT EDIT BELOW THIS, ASK FIRST!
// ----------------------------------------------------------------


if (isPublishing) {
    tasks.withType<Jar> {
        manifest {
            attributes += sortedMapOf(
                "Built-By" to System.getProperty("user.name"),
                "Build-Jdk" to System.getProperty("java.version"),
                "Implementation-Vendor" to "Simplexion Kft.",
                "Implementation-Version" to archiveVersion.get(),
                "Created-By" to GradleVersion.current()
            )
        }
    }

    signing {
        if (project.properties["signing.keyId"] == null) {
            useGpgCmd()
        }
        sign(publishing.publications)
    }

    publishing {

        repositories {
            if (isSnapshot) {
                requireNotNull(publishSnapshotUrl) { throw IllegalStateException("publishing: missing snapshot url, define Z2_PUBLISH_SNAPSHOT_URL") }
                mavenRepo(publishSnapshotUrl)
            } else {
                requireNotNull(publishReleaseUrl) { throw IllegalStateException("publishing: missing release url, define Z2_PUBLISH_RELEASE_URL") }
                mavenRepo(publishReleaseUrl)
            }
        }

        publications.withType<MavenPublication>().all {
            val path = "spxbhuhb/${project.name}"

            pom {
                description.set(project.name)
                name.set(project.name)
                url.set("https://github.com/$path")
                scm {
                    url.set("https://github.com/$path")
                    connection.set("scm:git:git://github.com/$path.git")
                    developerConnection.set("scm:git:ssh://git@github.com/$path.git")
                }
                licenses {
                    license {
                        name.set("proprietary")
                        distribution.set("repo")
                    }
                }
            }
        }
    }

}
