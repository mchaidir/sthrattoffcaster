plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.pi4j:pi4j-core:2.6.1")
    implementation("com.pi4j:pi4j-plugin-raspberrypi:2.6.1")
    implementation("com.pi4j:pi4j-plugin-pigpio:2.6.1")

    implementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}