plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    test {
        kotlin.srcDir("src")
    }
}

dependencies{
    testImplementation(kotlin("test"))
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
