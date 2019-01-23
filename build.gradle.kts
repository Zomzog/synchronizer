import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val exposed_version: String by project
val h2_version: String by project
val hikaricp_version: String by project
val ktor_version: String by project

val ktormoshi_version: String by project
val moshi_version: String by project

val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.3.10"
    jacoco
}

group = "synchronizer"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
}

dependencies {

    compile("ch.qos.logback:logback-classic:$logback_version")

    compile("com.ryanharter.ktor:ktor-moshi:$ktormoshi_version")
    implementation("com.squareup.moshi:moshi:$moshi_version")
    implementation("com.squareup.moshi:moshi-adapters:$moshi_version")

    compile("io.ktor:ktor-server-netty:$ktor_version")
    compile("io.ktor:ktor-jackson:$ktor_version")
    compile("io.ktor:ktor-server-core:$ktor_version")
    compile("io.ktor:ktor-server-host-common:$ktor_version")
    compile("io.ktor:ktor-locations:$ktor_version")
    compile("io.ktor:ktor-websockets:$ktor_version")

    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    compile("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")

    compile("org.kodein.di:kodein-di-generic-jvm:5.2.0")

    compile("com.h2database:h2:$h2_version")
    compile("org.jetbrains.exposed:exposed:$exposed_version")
    compile("com.zaxxer:HikariCP:$hikaricp_version")

    testCompile("io.ktor:ktor-server-tests:$ktor_version")
    testCompile("org.assertj:assertj-core:3.11.1")
    testCompile("io.rest-assured:rest-assured:3.2.0")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

// compile bytecode to java 8 (default is java 6)
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

jacoco {
    toolVersion = "0.8.2"
}

tasks.withType<JacocoReport> {
    reports {
        xml.apply {
            isEnabled = true
        }
        html.apply {
            isEnabled = false
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
