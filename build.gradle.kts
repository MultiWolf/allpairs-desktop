import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  kotlin("jvm")
  id("org.jetbrains.compose")
}

group = "com.fleey.allpairs"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  google()
}

dependencies {
  implementation(compose.desktop.currentOs)
  implementation(compose.materialIconsExtended)
  implementation("io.github.pavelicii:allpairs4j:1.0.1")
  implementation(project("library"))
}

compose.desktop {
  application {
    mainClass = "MainKt"
    
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
      packageName = "allpairs-desktop"
      packageVersion = "1.0.0"
    }
  }
}
