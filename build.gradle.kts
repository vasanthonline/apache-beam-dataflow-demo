import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.20"
	kotlin("plugin.spring") version "1.6.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven {
		url = uri("https://packages.confluent.io/maven/")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.confluent:kafka-avro-serializer:5.3.2")
	implementation("org.apache.beam:beam-sdks-java-core:2.46.0")
	implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:2.46.0")
	implementation("org.apache.beam:beam-runners-google-cloud-dataflow-java:2.46.0")
	implementation("com.google.api-client:google-api-client:2.0.0")
	implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
	implementation("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")
	implementation("com.google.cloud:google-cloud-storage:2.20.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.register("demofeed", JavaExec::class) {
	classpath(sourceSets["main"].runtimeClasspath)
	mainClass.set("com.example.dataflow.pipeline.DemoFeedPipelineKt")
	systemProperty("env", System.getProperty("env"))
}

tasks.withType<Test> {
	useJUnitPlatform()
}
