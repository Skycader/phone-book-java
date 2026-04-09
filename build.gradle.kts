plugins {
    java
    application
    id("io.objectbox") version "5.4.1"
}

group = "com.yamigami"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.objectbox:objectbox-java:5.4.1")
    annotationProcessor("io.objectbox:objectbox-processor:5.4.1")

    implementation("io.objectbox:objectbox-windows:5.4.1")
    implementation("io.objectbox:objectbox-linux:5.4.1")
    implementation("io.objectbox:objectbox-macos:5.4.1")

    implementation("org.slf4j:slf4j-simple:2.0.9")
}

application {
    mainClass.set("com.yamigami.phonebook.Main")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}