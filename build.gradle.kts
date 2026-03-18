version = "v9.0.0"
group = "nitoribot"
description = "NitoriBot"
plugins {// id 'com.gradleup.shadow' version '9.2.2'
  id("com.gradleup.shadow") version "9.2.2"
  kotlin("jvm") version "2.3.10"
  application
}

kotlin {
  jvmToolchain(25)
}

repositories { 

  mavenCentral()

  maven("https://repo.opencollab.dev/maven-releases/")

  maven("https://repo.opencollab.dev/maven-snapshots/")

  maven("https://jitpack.io/")

  maven("https://repo.opencollab.dev/main/")

  maven("https://maven.maxhenkel.de/repository/public/")
}

kotlin {
  dependencies {
    implementation("org.geysermc.mcprotocollib:protocol:1.21.11-SNAPSHOT")
    implementation("net.lenni0451:LambdaEvents:2.4.2")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("org.tinylog:slf4j-tinylog:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("net.kyori:adventure-text-serializer-ansi:4.26.1")
    implementation("net.kyori:adventure-text-serializer-plain:4.26.1")
    implementation("net.kyori:adventure-text-serializer-legacy:4.26.1")
    implementation("net.kyori:adventure-text-serializer-gson:4.26.1")
    implementation("net.lenni0451:Reflect:1.6.2")
    implementation("org.ow2.asm:asm:9.9.1")
  }
}

application {
  mainClass.set("land.chipmunk.parker2991.nitoribot.Main")
}
