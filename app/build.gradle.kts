plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.petter.remembeer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.petter.remembeer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    //implementation("com.google.firebase:firebase-common-ktx:20.4.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation ("androidx.compose.runtime:runtime-livedata:1.6.1")
    implementation ("androidx.compose.ui:ui-tooling:1.6.1")
    implementation ("androidx.compose.foundation:foundation:1.6.1")
    implementation ("androidx.compose.material:material:1.6.1")
    implementation ("androidx.compose.runtime:runtime:1.6.1")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    implementation ("androidx.compose.ui:ui:1.6.1")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.1")
    implementation ("androidx.compose.material3:material3:1.2.0")

    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-storage")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("com.google.accompanist:accompanist-coil:0.15.0")


    // CameraX
    implementation ("androidx.camera:camera-camera2:1.3.1")
    implementation ("androidx.camera:camera-lifecycle:1.3.1")
    implementation ("androidx.camera:camera-view:1.3.1")
    implementation ("androidx.camera:camera-core:1.3.1")
    implementation ("androidx.camera:camera-video:1.3.1")
    implementation ("androidx.camera:camera-extensions:1.3.1")
    //

    //QR
    implementation ("com.google.zxing:core:3.5.2")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.23.0")


    implementation ("com.google.code.gson:gson:2.10")

    implementation ("io.coil-kt:coil-compose:2.5.0")

    implementation ("com.google.mlkit:vision-common:17.3.0")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    implementation ("com.google.android.gms:play-services-mlkit-text-recognition-common:19.0.0")
    implementation ("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")

    ////DEPENDENCY INJECTION////
    implementation ("io.insert-koin:koin-core:3.4.3")
    implementation ("io.insert-koin:koin-android:3.4.3")
    implementation ("io.insert-koin:koin-androidx-compose:3.4.6")

    ////KOIN KSP////
    implementation ("io.insert-koin:koin-annotations:1.2.2")
    implementation ("io.insert-koin:koin-ksp-compiler:1.2.2")

}