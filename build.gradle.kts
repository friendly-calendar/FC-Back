import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    kotlin("plugin.jpa") version "1.8.21"
    kotlin("kapt") version "1.7.10"
}

group = "com.friendly"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-security:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.0.4")
    implementation("org.springframework.data:spring-data-envers:3.0.3")
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
    implementation("io.jsonwebtoken:jjwt:0.12.1")
    implementation("io.github.microutils:kotlin-logging:2.0.11")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    compileOnly("org.projectlombok:lombok:1.18.26")
    runtimeOnly("com.h2database:h2:2.1.214")
    runtimeOnly("org.postgresql:postgresql:42.5.4")
    kapt("org.projectlombok:lombok:1.18.26")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
    testImplementation("org.springframework.security:spring-security-test:6.0.2")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("org.modelmapper:modelmapper:2.3.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Delete>("deletePreviousGitHook") {
    val preCommit = "${rootProject.rootDir}/.git/hooks/pre-push"
    if (file(preCommit).exists()) {
        delete(preCommit)
    }
}

tasks.register<Copy>("installGitHook") {
    val gitHooksDir = "${rootProject.rootDir}/.git/hooks"

    dependsOn("deletePreviousGitHook")
    from("${rootProject.rootDir}/hooks/pre-push")
    into(gitHooksDir)
    eachFile {
        fileMode = 777
    }
}

tasks.register("addExecutePermissionToGitHook") {
    dependsOn("installGitHook")

    val osName = System.getProperty("os.name").toLowerCase()
    if (osName.contains("mac") || osName.contains("darwin") || osName.contains("linux")) {
        doLast {
            val gitHooksDir = "${rootProject.rootDir}/.git/hooks"
            val prePushScript = "$gitHooksDir/pre-push"

            if (file(prePushScript).exists()) {
                exec {
                    commandLine("chmod", "+x", prePushScript)
                }
            }
        }
    }
}

tasks.getByName("build").dependsOn("addExecutePermissionToGitHook")
