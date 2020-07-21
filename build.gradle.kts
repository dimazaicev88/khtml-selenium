plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    `java-library`
}

repositories {
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    implementation("org.seleniumhq.selenium:selenium-java:3.14.0")
    implementation("org.apache.commons:commons-lang3:3.8.1")
    implementation("com.google.code.gson:gson:2.8.6")
    testImplementation("org.mockito:mockito-all:1.9.5")
    implementation("org.testng:testng:7.0.0")
}