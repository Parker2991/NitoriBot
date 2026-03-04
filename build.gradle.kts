version = "v9.0.0"
group = "fnfboyfriendbot"
description = "FNFBoyfriendBot"
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
    implementation("org.tinylog:tinylog-api-kotlin:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("org.slf4j:slf4j-api:2.0.12")
  }
}

application {
  mainClass.set("land.chipmunk.parker2991.fnfboyfriendbot.Main")
}