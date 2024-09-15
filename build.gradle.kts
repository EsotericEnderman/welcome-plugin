import org.gradle.api.JavaVersion
import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
  java
  `java-library`

  `maven-publish`

  id("io.papermc.paperweight.userdev") version "1.7.2"
  id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
  id("xyz.jpenilla.run-paper") version "2.3.0"

  id("io.github.goooler.shadow") version "8.1.7"
}

val groupStringSeparator = "."
val kebabcaseStringSeparator = "-"
val snakecaseStringSeparator = "_"

fun kebabcase(normalString: String): String {
  return normalString.lowercase().replace(" ", kebabcaseStringSeparator)
}

fun capitalizeFirstLetter(string: String): String {
  return string.first().uppercase() + string.slice(IntRange(1, string.length - 1))
}

fun snakecase(kebabcaseString: String): String {
  return kebabcaseString.lowercase().replace(kebabcaseStringSeparator, snakecaseStringSeparator)
}

fun pascalcase(kebabcaseString: String): String {
  var pascalCaseString = ""

  val splitString = kebabcaseString.split(kebabcaseStringSeparator)

  for (part in splitString) {
    pascalCaseString += capitalizeFirstLetter(part)
  }

  return pascalCaseString
}

description = "A plugin to display a title and a changing subtitle."

val mainProjectAuthor = "Esoteric Enderman"
val projectAuthors = listOfNotNull(mainProjectAuthor)

val topLevelDomain = "dev"

val projectNameString = rootProject.name

group = topLevelDomain + groupStringSeparator + snakecase(kebabcase(mainProjectAuthor)) + groupStringSeparator + snakecase(projectNameString)
version = "0.0.12"

val buildDirectoryString = buildDir.toString()

val projectGroupString = group.toString()
val projectVersionString = version.toString()

val javaVersion = 17
val javaVersionEnumMember = JavaVersion.valueOf("VERSION_" + javaVersion)

val paperApiMinecraftVersion = "1.20"
val paperApiVersion = paperApiMinecraftVersion + "-" + "R0.1-SNAPSHOT"

java {
  sourceCompatibility = javaVersionEnumMember
  targetCompatibility = javaVersionEnumMember

  toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
}

repositories {
  mavenCentral()
}

dependencies {
  paperweight.paperDevBundle(paperApiVersion)
}

tasks {
  build {
    dependsOn(shadowJar)
  }

  shadowJar {
    archiveFileName = projectNameString + "-" + projectVersionString + "." + "jar"
  }

  compileJava {
    options.release = javaVersion
  }

  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
}

bukkitPluginYaml {
  authors = projectAuthors

  main = projectGroupString + groupStringSeparator + pascalcase(projectNameString)
  apiVersion = paperApiMinecraftVersion

  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = projectGroupString
            artifactId = projectNameString
            version = projectVersionString
        }
    }
}

tasks.named("publishMavenJavaPublicationToMavenLocal") {
  dependsOn(tasks.named("build"))
}
