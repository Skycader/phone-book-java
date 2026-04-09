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

// Кодировка для компиляции
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// 🔥 Исправление для run: stdin + кодировка
tasks.named<JavaExec>("run") {
    // Подключаем stdin для интерактивного ввода
    standardInput = System.`in`

    // 🔥 Исправление warning ObjectBox
    jvmArgs("--enable-native-access=ALL-UNNAMED")

    // 🔥 Принудительная кодировка UTF-8
    jvmArgs(
        "-Dfile.encoding=UTF-8",
        "-Dsun.stdout.encoding=UTF-8",
        "-Dsun.stderr.encoding=UTF-8"
    )

    // Дублируем через systemProperty для надёжности
    systemProperty("file.encoding", "UTF-8")
    systemProperty("sun.stdout.encoding", "UTF-8")
    systemProperty("sun.stderr.encoding", "UTF-8")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.yamigami.phonebook.Main"
    }

    // Включаем все зависимости внутрь JAR
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })

    // Исключаем дубликаты метаданных, чтобы избежать ошибок сборки
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
