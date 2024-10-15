import org.gradle.api.JavaVersion
import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
  java
  `java-library`

  `maven-publish`

  id("io.papermc.paperweight.userdev") version "1.7.2"
  id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
  id("xyz.jpenilla.run-paper") version "2.3.0"
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

group = topLevelDomain + groupStringSeparator + "enderman"
version = "0.0.13"

val buildDirectoryString = buildDir.toString()

val projectGroupString = group.toString()
val projectVersionString = version.toString()

val javaVersion = 17
val javaVersionEnumMember = JavaVersion.valueOf("VERSION_$javaVersion")

val paperApiMinecraftVersion = "1.20"
val paperApiVersion = "$paperApiMinecraftVersion-R0.1-SNAPSHOT"

java {
  sourceCompatibility = javaVersionEnumMember
  targetCompatibility = javaVersionEnumMember

  toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
}

dependencies {
  paperweight.paperDevBundle(paperApiVersion)
}

tasks {
  compileJava {
    options.release = javaVersion
  }

  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
}

bukkitPluginYaml {
  name = "Welcome"
  description = project.description
  authors = projectAuthors

  version = project.version.toString()
  apiVersion = paperApiMinecraftVersion
  main = projectGroupString + groupStringSeparator + "commissions.minecraft.plugins.welcome" + groupStringSeparator + pascalcase(projectNameString)
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
