import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val exposed_version: String by project
val h2_version: String by project
val hikaricp_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val kmongo_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.3.10"
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
    compile("io.ktor:ktor-server-netty:$ktor_version")
    compile("io.ktor:ktor-server-core:$ktor_version")
    compile("io.ktor:ktor-server-host-common:$ktor_version")
    compile("io.ktor:ktor-gson:$ktor_version")
    compile("io.ktor:ktor-locations:$ktor_version")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    compile("org.kodein.di:kodein-di-generic-jvm:5.2.0")
    compile("com.h2database:h2:$h2_version")
    compile("org.jetbrains.exposed:exposed:$exposed_version")
    compile("com.zaxxer:HikariCP:$hikaricp_version")
    testCompile("io.ktor:ktor-server-tests:$ktor_version")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

// compile bytecode to java 8 (default is java 6)
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}