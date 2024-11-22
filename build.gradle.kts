plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("war")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
//	implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
//	implementation("io.springfox:springfox-boot-starter:3.0.0")
//	implementation("javax.servlet:javax.servlet-api:4.0.1")
//	runtimeOnly("javax.servlet:javax.servlet-api:4.0.1")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.3")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
}

tasks.withType<Test> {
	useJUnitPlatform()
	exclude("**/EmployeeApplicationTests*")
}
