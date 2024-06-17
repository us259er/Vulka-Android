plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "io.github.vulka.impl.librus"
    compileSdk = libs.versions.android.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.gson)
    implementation(libs.jsoup)
    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(projects.coreApi)
    implementation(projects.databaseLogic)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
