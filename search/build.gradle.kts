import com.google.protobuf.gradle.id

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.10"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.protobuf") version "0.9.4"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.poulastaa.kyoku"
version = "1.0.0"
description = "search"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.0.1"
extra["springGrpcVersion"] = "0.12.0"

dependencies {
	// sql
//	runtimeOnly("com.mysql:mysql-connector-j")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// service discovery
//	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

	// config
//	implementation("org.springframework.cloud:spring-cloud-starter-config")

	// redis
	implementation("org.springframework.session:spring-session-data-redis")

	// gRPC
	implementation("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
	implementation("io.grpc:grpc-protobuf")
	implementation("io.grpc:grpc-stub")

	// devtools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// elastic-search
	implementation("co.elastic.clients:elasticsearch-java:8.11.0")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.6")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.grpc:spring-grpc-dependencies:${property("springGrpcVersion")}")
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java"
		}
	}
	generateProtoTasks {
		all().forEach {
			it.plugins {
				id("grpc") {
					option("@generated=omit")
				}
			}
		}
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
