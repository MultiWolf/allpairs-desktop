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
  // compose
  implementation(compose.desktop.currentOs)
  implementation(compose.materialIconsExtended)
  // allpairs-core
  implementation("io.github.pavelicii:allpairs4j:1.0.1")
  // poi
  implementation("org.apache.poi:poi:5.2.5")
  implementation("org.apache.poi:poi-ooxml:5.2.5")
  // my libs
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
