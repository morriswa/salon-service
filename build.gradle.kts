import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.665")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    runtimeOnly("com.mysql:mysql-connector-j")
}

group = "org.morriswa"
version = "0.0.5-SNAPSHOT"
description = "Salon Web Service"
java.sourceCompatibility = JavaVersion.VERSION_17

springBoot  {
    buildInfo ()
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}


springBoot {
    mainClass.set("org.morriswa.salon.SalonService")
}

tasks.getByName<BootJar>("bootJar") {
    this.archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
    this.manifest.attributes["Implementation-Title"] = project.description
    this.manifest.attributes["Implementation-Version"] = project.version
}

