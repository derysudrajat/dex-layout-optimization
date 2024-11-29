plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "id.derysudrajat.dexlayoutops"
    compileSdk = 35

    defaultConfig {
        applicationId = "id.derysudrajat.dexlayoutops"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // In real app, this would use its own release keystore
            signingConfig = signingConfigs.getByName("debug")
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlin.RequiresOptIn"
        )
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

baselineProfile {
    dexLayoutOptimization = true
}

dependencies {

    "baselineProfile"(project(":app:baselineprofile"))
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)

    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.runtime.tracing)
    implementation(libs.constraintlayout)
    implementation(libs.concurrentfutures)
    implementation(libs.core)
    implementation(libs.datastore)
    implementation(libs.google.material)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.guava)
    implementation(libs.lifecycle)
    implementation(libs.profileinstaller)
    implementation(libs.squareup.curtains)
    implementation(libs.tracing)
    implementation(libs.viewmodel)
    androidTestImplementation(libs.benchmark.junit)
}