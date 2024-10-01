plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.jetbrains.kotlin.plugin.noarg") version "1.9.25"
	id("com.adarshr.test-logger") version "3.2.0"
}

group = "inc.evil"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-graphql")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation(platform("io.micrometer:micrometer-tracing-bom:1.3.2"))
	implementation("io.micrometer:micrometer-tracing")
	implementation("io.projectreactor:reactor-core-micrometer:1.1.10")
	implementation("io.r2dbc:r2dbc-proxy:1.1.5.RELEASE")
	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
	implementation("io.projectreactor.kafka:reactor-kafka:1.3.23")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework:spring-jdbc")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.zalando:logbook-spring-boot-webflux-autoconfigure:3.9.0")
	implementation("org.zalando:logbook-spring-boot-autoconfigure:3.9.0")
	implementation("org.zalando:logbook-netty:3.7.0")
	implementation("org.apache.commons:commons-lang3:3.15.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.8.0")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:kafka")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:r2dbc")
	testImplementation("org.springframework.graphql:spring-graphql-test")
	testImplementation("org.awaitility:awaitility")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

//during deploy use: gradle clean build -x test unit-tests
val unitTests = task<Test>("unit-tests") {
	useJUnitPlatform {
		jvmArgs("-Dio.netty.leakDetectionLevel=paranoid")
		excludeTags("integration-test")
	}
}

noArg {
	annotation("inc.evil.bootiful_reactive_kafka.common.annotations.NoArgsConstructor")
	invokeInitializers = true
}
