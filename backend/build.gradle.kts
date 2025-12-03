plugins {
	java
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube")
}

group = "com.example.blockrott"
version = "0.0.1-SNAPSHOT"
description = "backendDeblockrott"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	annotationProcessor("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sonar {
    properties {
        // file() resuelve la ruta relativa a la carpeta 'backend', no al root.
        property("sonar.java.binaries", file("build/classes/java/main"))

        // Opcional: Para evitar errores con Lombok o librerias faltantes
        property("sonar.libraries", file("build/libs"))
        property("sonar.libraries", "build/libs")
    }
}
