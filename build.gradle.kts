layout.buildDirectory = file("./gradleBuild")
version = "v9.0.0-alpha Night of Nights"

plugins {
  id("java-library")
  id("io.github.zucchero-sintattico.typescript-gradle-plugin") version "4.5.7"
}

repositories {    
  gradlePluginPortal()
  
  mavenLocal()

  mavenCentral()
}

dependencies {
  implementation("io.github.zucchero-sintattico.typescript-gradle-plugin:io.github.zucchero-sintattico.typescript-gradle-plugin.gradle.plugin:4.5.7")
}

typescript {
  tsConfig = "tsconfig.json"
  outputDir = "./jsBuild/"
}
