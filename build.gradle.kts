plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    test {
        kotlin.srcDir("src")
    }
}

dependencies{
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0-RC")
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
