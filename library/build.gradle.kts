plugins {
  kotlin("jvm")
  id("org.jetbrains.compose")
}

group = "com.fleey.allpairs"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  google()
}

dependencies {
  // compose
  implementation("androidx.compose.runtime:runtime:1.6.5")
  implementation("androidx.compose.ui:ui:1.6.5")
  implementation("androidx.compose.foundation:foundation:1.6.5")
  // protobuf
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-jvm:1.6.3")
  // coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(17)
}