/*
    ZoneId timezone = ZoneId.of("America/Chicago");
    ZonedDateTime zoneDate = ZonedDateTime.now(timezone);
    LocalDate date = zoneDate.toLocalDate();
    LocalTime time = zoneDate.toLocalTime();
 */
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

version = "v9.0.0-alpha"
group = "nitoribot"
description = "NitoriBot"
plugins {
  kotlin("jvm") version "2.3.10"
  id("maven-publish")
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

  maven("https://libraries.minecraft.net")

  maven("https://code.chipmunk.land/api/packages/kaboomstandardsorganization/maven/")
}

dependencies {
  implementation("org.geysermc.mcprotocollib:protocol:26.2-SNAPSHOT")
  implementation("org.slf4j:slf4j-api:2.0.13")
  implementation("org.tinylog:slf4j-tinylog:2.7.0")
  implementation("org.tinylog:tinylog-impl:2.7.0")
  implementation("org.yaml:snakeyaml:2.2")
  implementation("net.kyori:adventure-text-serializer-ansi:5.2.0")
  implementation("net.kyori:adventure-text-serializer-plain:5.2.0")
  implementation("net.kyori:adventure-text-serializer-legacy:5.2.0")
  implementation("net.kyori:adventure-text-serializer-gson:5.2.0")
  implementation("net.kyori:adventure-text-minimessage:5.2.0")
  implementation("net.dv8tion:JDA:6.4.1")
  implementation("org.jline:jline:4.1.0")
  implementation("land.chipmunk.code.kaboomstandardsorganization.messaginglib:mcprotocollib:3.1.2")
  implementation("com.mojang:brigadier:1.0.500")
}

tasks.jar {
  val dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm:ss a")
  val dateTimeInfo = LocalDateTime.now();

  manifest {
    attributes["Main-Class"] = "land.chipmunk.parker2991.nitoribot.Main"
    attributes("Build-Time" to dateTimeFormatter.format(dateTimeInfo))
    //attributes["Build-Time"] = dateTimeFormatter.format(dateTimeInfo);
    attributes("Enable-Native-Access" to "ALL-UNNAMED")
  }
//"Custom-Attribute-Name" to "YourCustomValue"
  archiveClassifier.set("all")

  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  from(sourceSets.main.get().output)

  dependsOn(configurations.runtimeClasspath)

  from(
    {
      configurations.runtimeClasspath.get().filter {
        it.name.endsWith("jar")
      }.map {
        zipTree(it)
      }
    }
  )
}