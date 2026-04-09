plugins {
    java
    application
}

group = "com.yamigami"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // ORMLite JDBC
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")

    // SQLite драйвер
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    // Логирование (опционально, можно убрать)
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

application {
    mainClass.set("com.yamigami.phonebook.Main")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}