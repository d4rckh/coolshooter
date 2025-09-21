plugins {
    id("java")
    
    id("application")
    id("edu.sc.seis.launch4j") version "2.5.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("ch.qos.logback:logback-classic:0.9.26")
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")
}

application {
    mainClass.set("org.coolshooter.Main")
}

tasks.test {
    useJUnitPlatform()
}

launch4j {
    mainClassName = application.mainClass.get()
    jarTask = tasks.named<Jar>("jar").get()
    outfile = "great-shooter.exe"
    jreMinVersion = "17" // minimum Java version required
    bundledJrePath = "jre" // path relative to project root
    bundledJre64Bit = true
}