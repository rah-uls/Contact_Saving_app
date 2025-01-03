plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.firebaseauth"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.firebaseauth"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
apply (plugin = "com.google.gms.google-services")
dependencies {
    implementation ("com.google.firebase:firebase-auth:23.1.0")
    implementation ("com.google.firebase:firebase-analytics:22.1.2")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.firebaseui:firebase-ui-database:8.0.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}