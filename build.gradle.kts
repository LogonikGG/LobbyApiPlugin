plugins {
    `java-library`
    id("com.gradleup.shadow") version "8.3.0"
    `maven-publish`
}

group = "ru.logonik"
version = "2.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    val targetJavaVersion = 11
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-parameters"))
    options.isFork = true
    options.forkOptions.executable = "${System.getProperty("java.home")}/bin/javac.exe"
    options.encoding = "UTF-8"

    val targetJavaVersion = 11
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release.set(targetJavaVersion)
    }
}

tasks.shadowJar {
    relocate("co.aikar.commands", "ru.logonik.lobbyapi.acf")
    relocate("co.aikar.locales", "ru.logonik.lobbyapi.locales")

    archiveFileName.set("${project.name}-${project.version}.jar")
    destinationDirectory.set(file("C://Users/Logonik/Desktop/NowUsed/ServerMinecraft1.21.1/plugins"))
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("ru.logonik:SpiGuiRus:1.0-SNAPSHOT")
    implementation("org.jetbrains:annotations:26.0.2")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}