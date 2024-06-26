plugins {
  kotlin("jvm")
  id("org.jetbrains.compose")
}

repositories {
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  google()
}

dependencies {
  // compose
  implementation(compose.desktop.common)
  implementation(compose.material3)
  // protobuf
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-jvm:1.6.3")
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(17)
}