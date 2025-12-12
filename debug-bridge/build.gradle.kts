plugins {
  id("org.jetbrains.kotlin.jvm") version "1.9.23"
  application
}

repositories {
  mavenCentral()
}

tasks.jar {
  manifest {
    attributes["Main-Class"] = "land.chipmunk.parker2991.debugbridge.Main"
  }
}

dependencies {

}

application {
  mainClass = "land.chipmunk.parker2991.debugbridge.Main"
}