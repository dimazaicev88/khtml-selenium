import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    `java-library`
    `maven-publish`
}

group = "khtml"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile(group = "org.seleniumhq.selenium", name = "selenium-java", version = "3.14.0")
    compile("org.testng:testng:6.14.3")
    compile("org.jetbrains.kotlin:kotlin-reflect:1.3.21")
    compile("org.apache.commons:commons-lang3:3.8.1")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}